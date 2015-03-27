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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.config.Configuration;
import com.voxelplugineering.voxelsniper.api.event.bus.EventBus;
import com.voxelplugineering.voxelsniper.api.expansion.Expansion;
import com.voxelplugineering.voxelsniper.api.expansion.ExpansionManager;
import com.voxelplugineering.voxelsniper.api.logging.LoggingDistributor;
import com.voxelplugineering.voxelsniper.api.permissions.PermissionProxy;
import com.voxelplugineering.voxelsniper.api.platform.PlatformProxy;
import com.voxelplugineering.voxelsniper.api.registry.BiomeRegistry;
import com.voxelplugineering.voxelsniper.api.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.api.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.api.registry.WorldRegistry;
import com.voxelplugineering.voxelsniper.api.service.Service;
import com.voxelplugineering.voxelsniper.api.service.ServiceManager;
import com.voxelplugineering.voxelsniper.api.service.ServiceProvider;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceFactory;
import com.voxelplugineering.voxelsniper.api.service.scheduler.Scheduler;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormatParser;
import com.voxelplugineering.voxelsniper.api.world.queue.OfflineUndoHandler;
import com.voxelplugineering.voxelsniper.api.world.queue.UndoQueue;
import com.voxelplugineering.voxelsniper.service.CommandHandlerService;
import com.voxelplugineering.voxelsniper.util.Pair;

/**
 * The core service and expansion manager.
 */
public class Gunsmith implements ServiceManager, ExpansionManager
{

    // BEGIN static accessors

    /**
     * Gets the {@link ServiceManager} which is in change of the registration,
     * initialization, and shutdown of all {@link Service}s.
     * 
     * @return The service manager
     */
    public static ServiceManager getServiceManager()
    {
        return Holder.INSTANCE;
    }

    /**
     * Gets the {@link ExpansionManager} which is in change of the registration
     * of all {@link Expansion}s.
     * 
     * @return The expansion manager
     */
    public static ExpansionManager getExpansionManager()
    {
        return Holder.INSTANCE;
    }

    /**
     * Gets the logging service, which distributes logged messages to all
     * registered sources.
     * 
     * @return The logger
     */
    public static LoggingDistributor getLogger()
    {
        return (LoggingDistributor) Holder.INSTANCE.getService("logger").orNull();
    }

    /**
     * Gets the {@link Configuration} service which is in change of the storing
     * of custom configuration values for use throughout the system.
     * 
     * @return The configuration service
     */
    public static Configuration getConfiguration()
    {
        return (Configuration) Holder.INSTANCE.getService("config").orNull();
    }

    /**
     * Gets the {@link TextFormatParser} service which provides the platform
     * specific formatting codes.
     * 
     * @return The text format proxy
     */
    public static TextFormatParser getTextFormatProxy()
    {
        return (TextFormatParser) Holder.INSTANCE.getService("formatProxy").orNull();
    }

    /**
     * Gets the main thread of the system.
     * 
     * @return The main thread
     */
    public static Thread getMainThread()
    {
        return Holder.INSTANCE.mainThread;
    }

    /**
     * Gets the system {@link ClassLoader}.
     * 
     * @return The class loader
     */
    public static ClassLoader getClassLoader()
    {
        return Holder.INSTANCE.classloader;
    }

    /**
     * Gets the {@link MaterialRegistry} service which holds the registry of
     * global materials not specific to any world.
     * 
     * @param <T> The material type
     * @return The material registry
     */
    @SuppressWarnings("unchecked")
    public static <T> MaterialRegistry<T> getMaterialRegistry()
    {
        return (MaterialRegistry<T>) Holder.INSTANCE.getService("materialRegistry").orNull();
    }

    /**
     * Gets the {@link PlatformProxy} service which provides access to many
     * platform specific values.
     * 
     * @return The platform proxy
     */
    public static PlatformProxy getPlatformProxy()
    {
        return (PlatformProxy) Holder.INSTANCE.getService("platformProxy").orNull();
    }

    /**
     * Gets the {@link PermissionProxy} which access to permissions for specific
     * users.
     * 
     * @return The permission proxy
     */
    public static PermissionProxy getPermissionsProxy()
    {
        return (PermissionProxy) Holder.INSTANCE.getService("permissionProxy").orNull();
    }

    /**
     * Gets the {@link Scheduler} service which handles the registration and
     * running of synchronous and asynchronous tasks.
     * 
     * @return The scheduler
     */
    public static Scheduler getScheduler()
    {
        return (Scheduler) Holder.INSTANCE.getService("scheduler").orNull();
    }

    /**
     * Gets the {@link WorldRegistry} service which is a registry for all worlds
     * within the system.
     * 
     * @param <T> The world type
     * @return The world registry
     */
    @SuppressWarnings("unchecked")
    public static <T> WorldRegistry<T> getWorldRegistry()
    {
        return (WorldRegistry<T>) Holder.INSTANCE.getService("worldRegistry").orNull();
    }

    /**
     * Gets the {@link PlayerRegistry} which is a registry for all users within
     * the system.
     * 
     * @param <T> The player type
     * @return The player registry
     */
    @SuppressWarnings("unchecked")
    public static <T> PlayerRegistry<T> getPlayerRegistry()
    {
        return (PlayerRegistry<T>) Holder.INSTANCE.getService("playerRegistry").orNull();
    }

    /**
     * Gets {@link OfflineUndoHandler} service which handles user's
     * {@link UndoQueue}s after they have disconnected from the system.
     * 
     * @return The offline undo handler
     */
    public static OfflineUndoHandler getOfflineUndoHandler()
    {
        return (OfflineUndoHandler) Holder.INSTANCE.getService("offlineUndoHandler").orNull();
    }

    /**
     * Gets the {@link BrushManager} service which holds all brushes global to
     * all system users.
     * 
     * @return The global brush manager
     */
    public static BrushManager getGlobalBrushManager()
    {
        return (BrushManager) Holder.INSTANCE.getService("globalBrushManager").orNull();
    }

    /**
     * Gets the {@link AliasHandler} service which holds aliases global to all
     * system users.
     * 
     * @return The alias handler
     */
    public static AliasHandler getGlobalAliasHandler()
    {
        return (AliasHandler) Holder.INSTANCE.getService("aliasRegistry").orNull();
    }

    /**
     * Gets the {@link EventBus} service which handles the registration of event
     * handlers and the distribution of posted events.
     * 
     * @return The event bus
     */
    public static EventBus getEventBus()
    {
        return (EventBus) Holder.INSTANCE.getService("eventBus").orNull();
    }

    /**
     * Gets the {@link BiomeRegistry} service which holds biomes.
     * 
     * @param <T> The biome type
     * @return The biome registry
     */
    @SuppressWarnings("unchecked")
    public static <T> BiomeRegistry<T> getBiomeRegistry()
    {
        return (BiomeRegistry<T>) Holder.INSTANCE.getService("biomeRegistry").orNull();
    }

    /**
     * Gets the {@link CommandHandlerService} service.
     * 
     * @return The command handler
     */
    public static CommandHandlerService getCommandHandler()
    {
        return (CommandHandlerService) Holder.INSTANCE.getService("commandHandler").orNull();
    }

    /**
     * Gets the Persistence service.
     * 
     * @return The service
     */
    public static DataSourceFactory getPersistence()
    {
        return (DataSourceFactory) Holder.INSTANCE.getService("persistence").orNull();
    }

    /**
     * Gets whether the system has finished initialization and is currently
     * running.
     * 
     * @return Is system running
     */
    public static boolean isEnabled()
    {
        return Holder.INSTANCE.state == State.RUNNING;
    }

    private static class Holder
    {

        protected static final Gunsmith INSTANCE = new Gunsmith();
    }

    private Thread mainThread;
    private ClassLoader classloader;

    private Gunsmith()
    {
        this.mainThread = Thread.currentThread();
        this.classloader = Gunsmith.class.getClassLoader();
        initServiceManager();
        initExpansionManager();
    }

    // BEGIN ServiceManager

    private enum State
    {
        STOPPED, STARTING_REGISTRATION, STARTING_BUILDING, STARTING_INITIALIZING, RUNNING, STOPPING;
    }

    private Map<ServiceProvider.Type, List<ServiceProvider>> providers;
    private Map<String, List<Pair<Object, Method>>> initHooks;
    private Map<String, Service> services;
    private List<String> stoppedServices;
    private State state;
    private boolean testing = false;
    private ServiceProvider testingProvider = null;
    private Map<String, Service> testingServices = null;

    private void initServiceManager()
    {
        this.providers = new EnumMap<ServiceProvider.Type, List<ServiceProvider>>(ServiceProvider.Type.class);
        this.stoppedServices = Lists.newArrayList();
        this.state = State.STOPPED;
        this.services = Maps.newHashMap();
        this.initHooks = Maps.newHashMap();
    }

    /**
     * A method annotation cache to assist with locating methods during startup.
     */
    @SuppressWarnings("rawtypes")
    private static class StartupHelper
    {

        private Map<Class<?>, Map<Class, List<Method>>> cache;

        public StartupHelper()
        {
            this.cache = new MapMaker().weakKeys().makeMap();
        }

        /**
         * Builds the cache for the given class.
         * 
         * @param target The target class
         */
        public void build(Class<?> target)
        {
            if (this.cache.containsKey(target))
            {
                return;
            }
            Map<Class, List<Method>> anno = new MapMaker().weakKeys().makeMap();
            for (Method m : target.getMethods())
            {
                for (Annotation a : m.getAnnotations())
                {
                    if (!anno.containsKey(a.annotationType()))
                    {
                        anno.put(a.annotationType(), Lists.<Method>newArrayList());
                    }
                    anno.get(a.annotationType()).add(m);
                }
            }
            this.cache.put(target, anno);
        }

        /**
         * Gets all methods from the target class with the given annotation.
         * 
         * @param target The target class
         * @param annotation The requested annotation
         * @return All methods with the given annotation
         */
        public List<Method> get(Class<?> target, Class<?> annotation)
        {
            if (!annotation.isAnnotation())
            {
                return new ArrayList<Method>(0);
            }
            build(target);
            List<Method> methods = this.cache.get(target).get(annotation);
            if (methods == null)
            {
                return new ArrayList<Method>(0);
            }
            return methods;
        }

        /**
         * Gets all methods from the target class with the given annotation.
         * Returns all methods which also pass the given function.
         * 
         * @param target The target class
         * @param annotation The requested annotation
         * @param valid A function to check method validity
         * @return All applicable methods
         */
        public List<Method> get(Class<?> target, Class<?> annotation, Function<Method, Boolean> valid)
        {
            List<Method> methods = get(target, annotation);
            for (Iterator<Method> iter = methods.iterator(); iter.hasNext();)
            {
                if (!valid.apply(iter.next()))
                {
                    iter.remove();
                }
            }
            return methods;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeServices()
    {
        StartupHelper startup = new StartupHelper();
        if (this.state != State.STOPPED)
        {
            throw new IllegalStateException();
        }
        synchronized (this.services)
        {
            this.services.clear();
        }
        this.stoppedServices.clear();
        this.providers.clear();
        registerServiceProvider(new CoreServiceProvider());
        this.state = State.STARTING_REGISTRATION;
        String startTime = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(new Date(System.currentTimeMillis()));
        System.out.printf("Starting Gunsmith initialization process. (%s)\n", startTime);

        Function<Method, Boolean> noParamTest = new Function<Method, Boolean>()
        {

            @Override
            public Boolean apply(Method input)
            {
                return input.getParameterTypes().length == 0;
            }

        };

        // load expansions

        synchronized (this.expansions)
        {
            for (Expansion ex : this.expansions)
            {
                ex.init();
            }
        }

        for (ServiceProvider.Type type : this.providers.keySet())
        {
            for (ServiceProvider provider : this.providers.get(type))
            {
                provider.registerNewServices(this);
            }
        }

        this.state = State.STARTING_BUILDING;
        synchronized (this.services)
        {
            // search for builders in the order of first core, then platform,
            // then expansions
            for (ServiceProvider provider : this.providers.get(ServiceProvider.Type.CORE))
            {
                detectBuilders(provider, startup);
            }
            if (this.providers.containsKey(ServiceProvider.Type.PLATFORM))
            {
                for (ServiceProvider provider : this.providers.get(ServiceProvider.Type.PLATFORM))
                {
                    detectBuilders(provider, startup);
                }
            }
            if (this.providers.containsKey(ServiceProvider.Type.EXPANSION))
            {
                for (ServiceProvider provider : this.providers.get(ServiceProvider.Type.EXPANSION))
                {
                    detectBuilders(provider, startup);
                }
            }

            this.state = State.STARTING_INITIALIZING;
            for (ServiceProvider.Type type : this.providers.keySet())
            {
                for (ServiceProvider provider : this.providers.get(type))
                {
                    for (Method m : startup.get(provider.getClass(), ServiceProvider.PreInit.class, noParamTest))
                    {
                        try
                        {
                            m.invoke(provider, new Object[0]);
                        } catch (Exception e)
                        {
                            System.err.println("Error calling pre init hook from " + m.toGenericString());
                            e.printStackTrace();
                        }
                    }
                }
            }
            List<Service> toInit = Lists.newArrayList();
            for (String name : this.services.keySet())
            {
                toInit.add(this.services.get(name));
            }
            Collections.sort(toInit, new Comparator<Service>()
            {

                @Override
                public int compare(Service o1, Service o2)
                {
                    return Integer.signum(o1.getPriority() - o2.getPriority());
                }
            });
            for (Service service : toInit)
            {
                try
                {
                    service.start();
                } catch (Exception e)
                {
                    System.err.println(e.getMessage());
                    service.stop();
                    this.services.remove(service.getName());
                    continue;
                }
                if (this.initHooks.containsKey(service.getName()))
                {
                    for (Pair<Object, Method> hook : this.initHooks.get(service.getName()))
                    {
                        try
                        {
                            hook.getValue().invoke(hook.getKey(), service);
                        } catch (Exception e)
                        {
                            System.err.println("Error calling init hook for " + service.getName() + " from " + hook.getValue().toGenericString());
                            e.printStackTrace();
                        }
                    }
                }
            }
            for (ServiceProvider.Type type : this.providers.keySet())
            {
                for (ServiceProvider provider : this.providers.get(type))
                {

                    for (Method m : startup.get(provider.getClass(), ServiceProvider.PostInit.class, noParamTest))
                    {
                        try
                        {
                            m.invoke(provider, new Object[0]);
                        } catch (Exception e)
                        {
                            System.err.println("Error calling post init hook from " + m.toGenericString());
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (!this.stoppedServices.isEmpty())
            {
                String unbuilt = "";
                for (String stopped : this.stoppedServices)
                {
                    unbuilt += " " + stopped;
                }
                getLogger().warn("Finished initialization process with the following services unbuilt:" + unbuilt);
            } else
            {
                getLogger().info("Finished Gunsmith initialization process.");
            }

            this.state = State.RUNNING;
        }
    }

    private void detectBuilders(ServiceProvider provider, StartupHelper startup)
    {

        Function<Method, Boolean> builderTest = new Function<Method, Boolean>()
        {

            @Override
            public Boolean apply(Method input)
            {
                return input.getParameterTypes().length == 0 && Service.class.isAssignableFrom(input.getReturnType());
            }

        };
        for (Method m : startup.get(provider.getClass(), ServiceProvider.Builder.class, builderTest))
        {
            ServiceProvider.Builder builder = m.getAnnotation(ServiceProvider.Builder.class);
            if (this.stoppedServices.contains(builder.value()) || this.services.containsKey(builder.value()))
            {
                try
                {
                    this.services.put(builder.value(), (Service) m.invoke(provider, new Object[0]));
                    this.stoppedServices.remove(builder.value());
                } catch (Exception e)
                {
                    System.err.println("Failed to build " + builder.value() + " from " + m.toGenericString());
                    e.printStackTrace();
                }
            }
        }
        Function<Method, Boolean> initTest = new Function<Method, Boolean>()
        {

            @Override
            public Boolean apply(Method input)
            {
                return input.getParameterTypes().length == 1 && input.getParameterTypes()[0] == Service.class;
            }

        };

        for (Method m : startup.get(provider.getClass(), ServiceProvider.InitHook.class, initTest))
        {
            ServiceProvider.InitHook hook = m.getAnnotation(ServiceProvider.InitHook.class);
            List<Pair<Object, Method>> target = null;
            if (this.initHooks.containsKey(hook.value()))
            {
                target = this.initHooks.get(hook.value());
            } else
            {
                this.initHooks.put(hook.value(), target = Lists.newArrayList());
            }
            target.add(new Pair<Object, Method>(provider, m));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopServices()
    {
        if (this.state != State.RUNNING)
        {
            throw new IllegalStateException();
        }
        Gunsmith.getLogger().info("Starting Gunsmith destruction process");
        for (ServiceProvider.Type type : this.providers.keySet())
        {
            for (ServiceProvider provider : this.providers.get(type))
            {
                for (Method m : provider.getClass().getMethods())

                {
                    if (m.isAnnotationPresent(ServiceProvider.PreStop.class) && m.getParameterTypes().length == 0)
                    {
                        try
                        {
                            m.invoke(provider, new Object[0]);
                        } catch (Exception e)
                        {
                            System.err.println("Error calling stop hook from " + m.toGenericString());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        synchronized (this.services)
        {
            this.state = State.STOPPING;
            List<Service> toStop = Lists.newArrayList();
            for (String name : this.services.keySet())
            {
                toStop.add(this.services.get(name));
            }
            Collections.sort(toStop, new Comparator<Service>()
            {

                @Override
                public int compare(Service o1, Service o2)
                {
                    return Integer.signum(o2.getPriority() - o1.getPriority());
                }
            });
            for (Service service : toStop)
            {
                service.stop();
                this.stoppedServices.add(service.getName());
            }
            this.services.clear();
            System.out.println("Gunsmith successfully shutdown");
            this.state = State.STOPPED;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerServiceProvider(ServiceProvider provider)
    {
        List<ServiceProvider> target = null;
        if (this.providers.containsKey(provider.getType()))
        {
            target = this.providers.get(provider.getType());
        } else
        {
            this.providers.put(provider.getType(), target = Lists.newArrayList());
        }
        if (!target.contains(provider))
        {
            target.add(provider);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerService(String service)
    {
        if (this.state != State.STARTING_REGISTRATION)
        {
            throw new IllegalStateException();
        }
        if (!this.stoppedServices.contains(service))
        {
            this.stoppedServices.add(service);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasService(String service)
    {
        return this.services.containsKey(service) && this.services.get(service).isStarted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Service> getService(String name)
    {
        Service service = this.services.get(name);
        if (service == null && this.testing)
        {
            return Optional.fromNullable(createServiceForTesting(name));
        }
        return Optional.fromNullable(service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopService(Service service)
    {
        synchronized (this.services)
        {
            service.stop();
            this.services.remove(service.getName());
            this.stoppedServices.add(service.getName());
        }
    }

    private Service createServiceForTesting(String name)
    {
        if (!this.testing)
        {
            return null;
        }
        if (this.testingServices.containsKey(name))
        {
            return this.testingServices.get(name);
        }
        Service service = null;
        for (Method m : this.testingProvider.getClass().getMethods())
        {
            if (m.isAnnotationPresent(ServiceProvider.Builder.class))
            {
                if (m.getParameterTypes().length == 0 && Service.class.isAssignableFrom(m.getReturnType()))
                {
                    ServiceProvider.Builder builder = m.getAnnotation(ServiceProvider.Builder.class);
                    if (builder.value().equals(name))
                    {
                        try
                        {
                            service = (Service) m.invoke(this.testingProvider, new Object[0]);
                        } catch (Exception e)
                        {
                            System.err.println("Failed to build for testing " + builder.value() + " from " + m.toGenericString());
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
        if (service == null)
        {
            return null;
        }
        service.start();
        for (Method m : this.testingProvider.getClass().getMethods())
        {
            if (m.isAnnotationPresent(ServiceProvider.InitHook.class) && m.getParameterTypes().length == 1
                    && m.getParameterTypes()[0] == Service.class)
            {
                ServiceProvider.InitHook hook = m.getAnnotation(ServiceProvider.InitHook.class);
                if (hook.value().equals(name))
                {
                    try
                    {
                        m.invoke(this.testingProvider, service);
                    } catch (Exception e)
                    {
                        System.err.println("Failed to init for testing " + hook.value() + " from " + m.toGenericString());
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        this.testingServices.put(name, service);
        return service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTesting(ServiceProvider provider)
    {
        if (this.testing)
        {
            return;
        }
        this.testing = true;
        this.testingProvider = provider;
        this.testingServices = Maps.newHashMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTesting()
    {
        return this.testing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Service> getServices()
    {
        return this.services.values();
    }

    // BEGIN ExpansionManager
    private List<Expansion> expansions;

    private void initExpansionManager()
    {
        this.expansions = Lists.newArrayList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerExpansion(Expansion ex)
    {
        synchronized (this.expansions)
        {
            if (!this.expansions.contains(ex))
            {
                this.expansions.add(ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Expansion> getExpansions()
    {
        return this.expansions;
    }

}
