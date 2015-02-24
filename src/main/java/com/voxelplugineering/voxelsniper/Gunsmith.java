package com.voxelplugineering.voxelsniper;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.alias.AliasOwner;
import com.voxelplugineering.voxelsniper.api.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.api.config.Configuration;
import com.voxelplugineering.voxelsniper.api.event.bus.EventBus;
import com.voxelplugineering.voxelsniper.api.expansion.Expansion;
import com.voxelplugineering.voxelsniper.api.expansion.ExpansionManager;
import com.voxelplugineering.voxelsniper.api.logging.LoggingDistributor;
import com.voxelplugineering.voxelsniper.api.service.Service;
import com.voxelplugineering.voxelsniper.api.service.ServiceManager;
import com.voxelplugineering.voxelsniper.api.service.ServiceProvider;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormatProxy;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.config.ConfigurationManager;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.event.bus.AsyncEventBus;
import com.voxelplugineering.voxelsniper.event.handler.CommonEventHandler;
import com.voxelplugineering.voxelsniper.logging.CommonLoggingDistributor;
import com.voxelplugineering.voxelsniper.util.Pair;

public class Gunsmith implements ServiceManager, ExpansionManager
{

    // BEGIN static accessors

    public static ServiceManager getServiceManager()
    {
        return Holder.INSTANCE;
    }

    public static ExpansionManager getExpansionManager()
    {
        return Holder.INSTANCE;
    }
    
    public static LoggingDistributor getLogger()
    {
        return (LoggingDistributor) Holder.INSTANCE.getService("logger").orNull();
    }

    public static Configuration getConfiguration()
    {
        return (Configuration) Holder.INSTANCE.getService("config").orNull();
    }

    public static TextFormatProxy getTextFormatProxy()
    {
        return (TextFormatProxy) Holder.INSTANCE.getService("formatProxy").orNull();
    }

    private static class Holder
    {

        private static final Gunsmith INSTANCE = new Gunsmith();
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

    private void initServiceManager()
    {
        this.providers = new EnumMap<ServiceProvider.Type, List<ServiceProvider>>(ServiceProvider.Type.class);
        this.stoppedServices = Lists.newArrayList();
        this.state = State.STOPPED;
        this.services = Maps.newHashMap();
        this.initHooks = Maps.newHashMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init()
    {
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
        System.out.println("Starting Gunsmith initialization process. ("
                + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(new Date(System.currentTimeMillis())) + ")");

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
            // search for builders in the order of first core, then plaform,
            // then expansions
            for (ServiceProvider provider : this.providers.get(ServiceProvider.Type.CORE))
            {
                detectBuilders(provider);
            }
            if (this.providers.containsKey(ServiceProvider.Type.PLATFORM))
            {
                for (ServiceProvider provider : this.providers.get(ServiceProvider.Type.PLATFORM))
                {
                    detectBuilders(provider);
                }
            }
            if (this.providers.containsKey(ServiceProvider.Type.EXPANSION))
            {
                for (ServiceProvider provider : this.providers.get(ServiceProvider.Type.EXPANSION))
                {
                    detectBuilders(provider);
                }
            }

            this.state = State.STARTING_INITIALIZING;
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
                    return Integer.signum(o1.getPriority()-o2.getPriority());
                }
            });
            for(Service service: toInit)
            {
                service.start();
                if(this.initHooks.containsKey(service.getName()))
                {
                    for(Pair<Object, Method> hook: this.initHooks.get(service.getName()))
                    {
                        try
                        {
                            hook.getValue().invoke(hook.getKey(), service);
                        }
                        catch(Exception e)
                        {
                            System.err.println("Error calling init hook for " + service.getName() + " from " + hook.getValue().toGenericString());
                            e.printStackTrace();
                        }
                    }
                }
            }
            if(!this.stoppedServices.isEmpty())
            {
                String unbuilt = "";
                for(String stopped: this.stoppedServices)
                {
                    unbuilt += " " + stopped;
                }
                getLogger().warn("Finished initialization process with the following services unbuilt:" + unbuilt);
            }
            
            this.state = State.RUNNING;
        }
    }

    private void detectBuilders(ServiceProvider provider)
    {
        for (Method m : provider.getClass().getMethods())
        {
            if (m.isAnnotationPresent(ServiceProvider.Builder.class) && validate(m))
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
        }
        for (Method m : provider.getClass().getMethods())
        {
            if (m.isAnnotationPresent(ServiceProvider.InitHook.class) && m.getParameterTypes().length == 1 && m.getParameterTypes()[0] == Service.class)
            {
                ServiceProvider.InitHook hook = m.getAnnotation(ServiceProvider.InitHook.class);
                List<Pair<Object, Method>> target = null;
                if(this.initHooks.containsKey(hook.value()))
                {
                    target = this.initHooks.get(hook.value());
                }
                else
                {
                    this.initHooks.put(hook.value(), target = Lists.newArrayList());
                }
                target.add(new Pair<Object, Method>(provider, m));
            }
        }
    }
    
    private boolean validate(Method m)
    {
        if (m.getParameterTypes().length == 0 && Service.class.isAssignableFrom(m.getReturnType()))
        {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop()
    {
        if (this.state != State.RUNNING)
        {
            throw new IllegalStateException();
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
                    return Integer.signum(o2.getPriority()-o1.getPriority());
                }
            });
            for(Service service: toStop)
            {
                service.stop();
                this.stoppedServices.add(service.getName());
            }
            this.services.clear();

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
    public Optional<Service> getService(String service)
    {
        return Optional.fromNullable(this.services.get(service));
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

    //BEGIN core service provider
    
    private class CoreServiceProvider extends ServiceProvider
    {

        public CoreServiceProvider()
        {
            super(ServiceProvider.Type.CORE);
        }

        @Override
        public void registerNewServices(ServiceManager manager)
        {
            manager.registerService("logger");
            manager.registerService("config");
            manager.registerService("formatProxy");
            manager.registerService("eventBus");
            manager.registerService("aliasRegistry");
        }

        @Builder("logger")
        public Service buildLogger()
        {
            return new CommonLoggingDistributor();
        }

        @Builder("config")
        public Service buildConfig()
        {
            return new ConfigurationManager();
        }

        @Builder("formatProxy")
        public Service buildFormatProxy()
        {
            return new TextFormatProxy.TrivialTextFormatProxy();
        }

        @Builder("eventBus")
        public Service buildEventBus()
        {
            return new AsyncEventBus();
        }
        
        @Builder("aliasRegistry")
        public Service buildAliasRegistry()
        {
            return new AliasHandler(new AliasOwner.GunsmithAliasOwner());
        }

        @InitHook("eventBus")
        public void initEventBus(Service service)
        {
            CommonEventHandler defaultEventHandler = new CommonEventHandler();
            ((EventBus) service).register(defaultEventHandler);
        }

        @InitHook("config")
        public void initConfig(Service service)
        {
            Configuration configuration = (Configuration) service;
            configuration.registerContainer(BaseConfiguration.class);
            configuration.registerContainer(VoxelSniperConfiguration.class);
        }
        
        @InitHook("aliasRegistry")
        public void initAlias(Service service)
        {
            ((AliasHandler) service).registerTarget("brush");
        }

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

}
