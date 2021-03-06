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

import com.voxelplugineering.voxelsniper.service.Builder;
import com.voxelplugineering.voxelsniper.service.InitHook;
import com.voxelplugineering.voxelsniper.service.InitPhase;
import com.voxelplugineering.voxelsniper.service.PostInit;
import com.voxelplugineering.voxelsniper.service.PreStop;
import com.voxelplugineering.voxelsniper.service.Service;
import com.voxelplugineering.voxelsniper.service.config.Configuration;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.Contextable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A manager for service initialization and destruction.
 */
public class ServiceManager implements Contextable
{

    /**
     * Represents the various states of the service manager.
     */
    private enum State
    {
        STOPPED,
        STARTING,
        RUNNING,
        STOPPING
    }

    private State state;
    private List<Service> services;
    private Context context;
    private Configuration conf;

    private final Map<Class<? extends Contextable>, TargettedMethod> builders;
    private final Map<Class<? extends Contextable>, List<TargettedMethod>> inithooks;
    private final List<TargettedMethod> postInit;
    private final List<TargettedMethod> preStop;

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
        // TODO: reduce NPath complexity
        if (this.state != State.STOPPED)
        {
            throw new IllegalStateException("Attempted to launch an already started service manager");
        }
        this.state = State.STARTING;

        // Send in dev warning
        GunsmithLogger.getLogger().warn("============================================================");
        GunsmithLogger.getLogger().warn("YOU ARE USING AN IN DEVELOPMENT VERSION OF VOXELSNIPER");
        GunsmithLogger.getLogger().warn("        THERE MAY BE BUGS (please report them)");
        GunsmithLogger.getLogger().warn("============================================================");

        this.context = new Context();
        this.context.put(this);

        List<TargettedMethod> builds = new ArrayList<TargettedMethod>(this.builders.values());
        Collections.sort(builds, (a, b) -> {
            return Integer.signum(a.priority - b.priority);
        });

        for (TargettedMethod m : builds)
        {
            try
            {
                // Build service
                Contextable obj = (Contextable) m.method.invoke(m.object, this.context);
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
                // If the service must be initialized early then call its init hooks at this point,
                // this allows services to be initialized before other services are built
                if (m.phase == InitPhase.EARLY)
                {
                    List<TargettedMethod> inits = this.inithooks.get(m.target);
                    Collections.sort(inits, (a, b) -> {
                        return Integer.signum(a.priority - b.priority);
                    });
                    for (TargettedMethod tm : inits)
                    {
                        tm.method.invoke(tm.object, this.context, this.context.get(tm.target).get());
                    }
                }
            } catch (Exception e)
            {
                GunsmithLogger.getLogger().error(e, "Error loading " + m.target.getName() + " from " + m.method.toGenericString());
                this.inithooks.remove(m.target);
            }
        }

        // Initialize remaining services
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
        Collections.sort(inits, (a, b) -> {
            return Integer.signum(a.priority - b.priority);
        });

        for (TargettedMethod m : inits)
        {
            try
            {
                m.method.invoke(m.object, this.context, this.context.get(m.target).get());
            } catch (Exception e)
            {
                GunsmithLogger.getLogger().error(e, "Error calling init hook");
            }
        }

        // Call post init hooks
        for (TargettedMethod m : this.postInit)
        {
            try
            {
                m.method.invoke(m.object, this.context);
            } catch (Exception e)
            {
                GunsmithLogger.getLogger().error(e, "Error calling post init hook");
            }
        }
        this.state = State.RUNNING;
    }

    /**
     * Shuts down all running services.
     */
    public void shutdown()
    {
        this.state = State.STOPPING;
        // Call the pre stop hooks
        for (TargettedMethod m : this.preStop)
        {
            try
            {
                m.method.invoke(m.object, this.context);
            } catch (Exception e)
            {
                GunsmithLogger.getLogger().error(e, "Error calling pre stop hook");
            }
        }
        // Shutdown services
        for (Service serv : this.services)
        {
            if (serv.isInitialized())
            {
                serv.shutdown();
            }
        }
        this.state = State.STOPPED;
    }

    /**
     * Registers {@link Builder}s and {@link InitHook}s from the given class.
     *
     * @param serviceProvider The new service provider
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
        while (cls != null && cls != Object.class)
        {
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
            cls = cls.getSuperclass();
        }
    }

    private void detectPreStop(Object serviceProvider)
    {
        Class<?> cls = serviceProvider.getClass();
        while (cls != null && cls != Object.class)
        {
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
            cls = cls.getSuperclass();
        }
    }

    private void detectBuilders(Object serviceProvider)
    {
        Class<?> cls = serviceProvider.getClass();
        while (cls != null && cls != Object.class)
        {
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
            cls = cls.getSuperclass();
        }
    }

    private void detectInitHooks(Object serviceProvider)
    {
        Class<?> cls = serviceProvider.getClass();
        while (cls != null && cls != Object.class)
        {
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
            cls = cls.getSuperclass();
        }
    }

    /**
     * Represents an annotated method within a service provider.
     */
    private static class TargettedMethod
    {

        private final Method method;
        private Class<? extends Contextable> target;
        private final Object object;
        private final int priority;
        private InitPhase phase;

        public TargettedMethod(Method m, Builder anno, Object obj)
        {
            this.method = m;
            this.target = anno.target();
            this.object = obj;
            this.priority = anno.priority();
            this.phase = anno.initPhase();
        }

        public TargettedMethod(Method m, InitHook anno, Object obj)
        {
            this.method = m;
            this.target = anno.target();
            this.object = obj;
            this.priority = anno.priority();
            this.phase = null;
        }

        public TargettedMethod(Method m, PostInit anno, Object obj)
        {
            this.method = m;
            this.object = obj;
            this.priority = anno.priority();
        }

        public TargettedMethod(Method m, PreStop anno, Object obj)
        {
            this.method = m;
            this.object = obj;
            this.priority = anno.priority();
        }

    }

}
