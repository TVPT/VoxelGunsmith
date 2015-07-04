/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 The Voxel Plugineering Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.voxelplugineering.voxelsniper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.service.Builder;
import com.voxelplugineering.voxelsniper.service.InitHook;
import com.voxelplugineering.voxelsniper.service.InitPhase;
import com.voxelplugineering.voxelsniper.service.PostInit;
import com.voxelplugineering.voxelsniper.service.PreStop;
import com.voxelplugineering.voxelsniper.service.Service;
import com.voxelplugineering.voxelsniper.service.config.Configuration;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.Contextable;

/**
 * A manager for service initialization and destruction.
 */
public class ServiceManager implements Contextable
{

    private enum State
    {
        STOPPED, STARTING, RUNNING, STOPPING
    }

    private static Comparator<TargettedMethod> TARGET_COMPARATOR = new Comparator<TargettedMethod>()
    {

        @Override
        public int compare(TargettedMethod a, TargettedMethod b)
        {
            return Integer.signum(a.priority - b.priority);
        }

    };

    private final Map<Class<? extends Contextable>, TargettedMethod> builders;
    private final Map<Class<? extends Contextable>, List<TargettedMethod>> inithooks;
    private final List<TargettedMethod> postInit;
    private final List<TargettedMethod> preStop;

    protected State state;
    protected List<Service> services;
    protected Context context;
    protected Configuration conf;

    /**
     * Creates a new {@link ServiceManager}.
     */
    public ServiceManager()
    {
        this.builders = Maps.newHashMap();
        this.inithooks = Maps.newHashMap();
        this.services = Lists.newArrayList();
        this.postInit = Lists.newArrayList();
        this.preStop = Lists.newArrayList();
        this.state = State.STOPPED;
    }

    /**
     * Gets the context of this service manager.
     * 
     * @return The context
     */
    public Context getContext()
    {
        return this.context;
    }

    /**
     * Gets whether this service manager has completed the initialization stage and is running.
     * 
     * @return Is running
     */
    public boolean isRunning()
    {
        return this.state == State.RUNNING;
    }

    /**
     * Starts all registered services.
     */
    public void start()
    {
        if (this.state != State.STOPPED)
        {
            throw new IllegalStateException("Attempted to launch an already started service manager");
        }
        this.state = State.STARTING;
        this.context = new Context();
        this.context.put(this);

        List<TargettedMethod> builds = new ArrayList<TargettedMethod>(this.builders.values());
        Collections.sort(builds, new Comparator<TargettedMethod>()
        {

            @Override
            public int compare(TargettedMethod a, TargettedMethod b)
            {
                return Integer.signum(a.priority - b.priority);
            }

        });

        for (TargettedMethod m : builds)
        {
            try
            {
                Contextable obj = (Contextable) m.method.invoke(m.obj, this.context);
                System.out.println("Built " + m.target.getSimpleName());
                this.context.put(obj);
                if (obj instanceof Service)
                {
                    ((Service) obj).start();
                    this.services.add((Service) obj);
                }
                if (this.conf == null && obj instanceof Configuration)
                {
                    this.conf = (Configuration) obj;
                }
                if (m.phase == InitPhase.EARLY)
                {
                    List<TargettedMethod> inits = this.inithooks.get(m.target);
                    Collections.sort(inits, TARGET_COMPARATOR);
                    for (TargettedMethod tm : inits)
                    {
                        tm.method.invoke(tm.obj, this.context, this.context.get(tm.target).get());
                        System.out.println("Called init hook " + tm.method.toGenericString());
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }

        List<TargettedMethod> inits = new ArrayList<TargettedMethod>();
        for (Map.Entry<Class<? extends Contextable>, List<TargettedMethod>> entry : this.inithooks.entrySet())
        {
            if (!this.context.has(entry.getKey()))
            {
                continue;
            }
            if (this.builders.get(entry.getKey()).phase == InitPhase.EARLY)
            {
                continue;
            }
            inits.addAll(entry.getValue());

        }
        Collections.sort(inits, TARGET_COMPARATOR);

        for (TargettedMethod m : inits)
        {
            try
            {
                m.method.invoke(m.obj, this.context, this.context.get(m.target).get());
                System.out.println("Called init hook " + m.method.toGenericString());
            } catch (Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }

        for (TargettedMethod m : this.postInit)
        {
            try
            {
                m.method.invoke(m.obj, this.context);
                System.out.println("Called post init hook " + m.method.toGenericString());
            } catch (Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * Shuts down all running services.
     */
    public void shutdown()
    {
        for (TargettedMethod m : this.preStop)
        {
            try
            {
                m.method.invoke(m.obj, this.context);
                System.out.println("Called pre stop hook " + m.method.toGenericString());
            } catch (Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
        for (Service serv : this.services)
        {
            if (serv.isInitialized())
            {
                serv.shutdown();
            }
        }
    }

    /**
     * Registers {@link Builder}s and {@link InitHook}s from the given class.
     */
    public void register(Object serviceProvider)
    {
        if (this.state != State.STOPPED)
        {
            throw new IllegalStateException("Attempted to register service providers while manager was running.");
        }
        detectBuilders(serviceProvider);
        detectInitHooks(serviceProvider);
        detectPostInit(serviceProvider);
        detectPreStop(serviceProvider);

    }

    private void detectPostInit(Object serviceProvider)
    {
        Class<?> cls = serviceProvider.getClass();
        for (Method m : cls.getDeclaredMethods())
        {
            if (!m.isAnnotationPresent(PostInit.class) || m.getParameterTypes().length != 1
                    || !Context.class.isAssignableFrom(m.getParameterTypes()[0]))
            {
                continue;
            }
            PostInit anno = m.getAnnotation(PostInit.class);
            this.postInit.add(new TargettedMethod(m, anno, serviceProvider));
        }
    }

    private void detectPreStop(Object serviceProvider)
    {
        Class<?> cls = serviceProvider.getClass();
        for (Method m : cls.getDeclaredMethods())
        {
            if (!m.isAnnotationPresent(PreStop.class) || m.getParameterTypes().length != 1
                    || !Context.class.isAssignableFrom(m.getParameterTypes()[0]))
            {
                continue;
            }
            PreStop anno = m.getAnnotation(PreStop.class);
            this.preStop.add(new TargettedMethod(m, anno, serviceProvider));
        }
    }

    private void detectBuilders(Object serviceProvider)
    {
        Class<?> cls = serviceProvider.getClass();
        for (Method m : cls.getDeclaredMethods())
        {
            if (!m.isAnnotationPresent(Builder.class))
            {
                continue;
            }
            if (m.getParameterTypes().length != 1 || !Context.class.isAssignableFrom(m.getParameterTypes()[0]))
            {
                continue;
            }
            Builder builder = m.getAnnotation(Builder.class);
            if (!m.getReturnType().isAssignableFrom(builder.target()))
            {
                continue;
            }
            this.builders.put(builder.target(), new TargettedMethod(m, builder, serviceProvider));
        }
    }

    private void detectInitHooks(Object serviceProvider)
    {
        Class<?> cls = serviceProvider.getClass();
        for (Method m : cls.getDeclaredMethods())
        {
            if (!m.isAnnotationPresent(InitHook.class))
            {
                continue;
            }
            InitHook builder = m.getAnnotation(InitHook.class);
            if (m.getParameterTypes().length != 2 || !builder.target().isAssignableFrom(m.getParameterTypes()[1])
                    || !Context.class.isAssignableFrom(m.getParameterTypes()[0]))
            {
                continue;
            }
            List<TargettedMethod> list;
            if (this.inithooks.containsKey(builder.target()))
            {
                list = this.inithooks.get(builder.target());
            } else
            {
                list = Lists.newArrayList();
                this.inithooks.put(builder.target(), list);
            }
            list.add(new TargettedMethod(m, builder, serviceProvider));
        }
    }

}

class TargettedMethod
{

    public final Method method;
    public Class<? extends Contextable> target;
    public Class<?> annotation;
    public final Object obj;
    public final int priority;
    public InitPhase phase;

    public TargettedMethod(Method m, Builder annotation, Object obj)
    {
        this.method = m;
        this.target = annotation.target();
        this.obj = obj;
        this.priority = annotation.priority();
        this.phase = annotation.initPhase();
        this.annotation = annotation.getClass();
    }

    public TargettedMethod(Method m, InitHook annotation, Object obj)
    {
        this.method = m;
        this.target = annotation.target();
        this.obj = obj;
        this.priority = annotation.priority();
        this.phase = null;
        this.annotation = annotation.getClass();
    }

    public TargettedMethod(Method m, PostInit annotation, Object obj)
    {
        this.method = m;
        this.obj = obj;
        this.priority = annotation.priority();
        this.annotation = annotation.getClass();
    }

    public TargettedMethod(Method m, PreStop annotation, Object obj)
    {
        this.method = m;
        this.obj = obj;
        this.priority = annotation.priority();
        this.annotation = annotation.getClass();
    }

}
