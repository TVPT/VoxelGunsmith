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
package com.voxelplugineering.voxelsniper.api;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.thevoxelbox.vsl.api.IGraphCompilerFactory;
import com.thevoxelbox.vsl.classloader.GraphCompilerFactory;
import com.thevoxelbox.vsl.type.Type;
import com.voxelplugineering.voxelsniper.common.command.CommandHandler;
import com.voxelplugineering.voxelsniper.common.event.CommonEventHandler;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.config.Configuration;
import com.voxelplugineering.voxelsniper.logging.CommonLoggingDistributor;
import com.voxelplugineering.voxelsniper.util.BrushCompiler;

/**
 * The Core of VoxelGunsmith, provides access to handlers and validates initialization is done completely and correctly.
 */
public final class Gunsmith
{

    /**
     * The core of the specific implementation.
     */
    private static IVoxelSniper plugin = null;
    /**
     * A proxy for the specific implementations permissions system.
     */
    private static IPermissionProxy permissionProxy = null;
    /**
     * The global brush manager, manages the loading and registration of brushes which are available to all users of a multi-user environment. In a
     * single-user environment this would be the core brush manager for the user.
     */
    private static IBrushManager globalBrushManager = null;
    /**
     * The default brush loader. The default source from which brushes are loaded, typically this is the brush loader used by the global brush
     * manager.
     */
    private static IBrushLoader defaultBrushLoader = null;
    /**
     * The command handler, manages the registration of both commands and handlers, and distributes invocations of the commands to its registered
     * handlers. Also handles automatic command argument validation.
     */
    private static CommandHandler commandHandler = null;
    /**
     * The handler for the default behavior for events.
     */
    private static CommonEventHandler defaultEventHandler = null;
    /**
     * The internal event bus for events within Gunsmith.
     */
    private static EventBus eventBus = null;
    /**
     * The user handler for multi-user environments. Handles the creation of Gunsmith user instances ({@link ISniper}).
     */
    private static ISniperFactory<?> sniperManager = null;
    /**
     * The factory for creating Gunsmith proxies of the materials available in the specific implementation.
     */
    private static IMaterialFactory<?> materialFactory = null;
    /**
     * The factory for creating Gunsmith proxies of worlds (a world being an individual scene).
     */
    private static IWorldFactory worldFactory = null;
    /**
     * The global configuration container for Gunsmith.
     */
    private static IConfiguration configuration = null;
    /**
     * The global log distributor for Gunsmith.
     */
    private static ILoggingDistributor logDistributor = null;
    /**
     * The VSL compiler factory for the Brush Managers to reference.
     */
    private static GraphCompilerFactory compilerFactory = null;

    /**
     * The initialization state of Gunsmith.
     */
    private static boolean isPluginEnabled = false;

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.api.IVoxelSniper} to be used.
     *
     * @param sniper Implementation of IVoxelSniper to be used
     */
    public static void setPlugin(IVoxelSniper sniper)
    {
        checkNotNull(sniper, "Cannot set a null plugin!");
        check();
        Gunsmith.plugin = sniper;
    }

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.api.IPermissionProxy} to be used.
     *
     * @param permissionProxy Implementation of IPermissionProxy to be used, cannot be null
     */
    public static void setPermissionProxy(IPermissionProxy permissionProxy)
    {
        checkNotNull(permissionProxy, "Cannot set a null Permission Proxy!");
        check();
        Gunsmith.permissionProxy = permissionProxy;
    }

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.api.IBrushManager} to be used.
     *
     * @param brushManager Implementation of IBrushManager to be used, cannot be null
     */
    public static void setGlobalBrushManager(IBrushManager brushManager)
    {
        checkNotNull(brushManager, "Cannot set a null BrushManager!");
        check();
        Gunsmith.globalBrushManager = brushManager;
    }

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.api.IBrushLoader} to be used.
     *
     * @param brushLoader Implementation of IBrushLoader to be used, cannot be null
     */
    public static void setDefaultBrushLoader(IBrushLoader brushLoader)
    {
        checkNotNull(brushLoader, "Cannot set a null BrushLoader!");
        check();
        Gunsmith.defaultBrushLoader = brushLoader;
    }

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.common.command.CommandHandler} to be used.
     *
     * @param command Implementation of CommandHandler to be used, cannot be null
     */
    public static void setCommandHandler(CommandHandler command)
    {
        checkNotNull(command, "Cannot set a null CommandHandler!");
        check();
        Gunsmith.commandHandler = command;
    }

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.api.ISniperFactory} to be used.
     *
     * @param manager Implementation of ISniperFactory to be used, cannot be null
     */
    public static void setSniperManager(ISniperFactory<?> manager)
    {
        checkNotNull(manager, "Cannot set a null SniperManager!");
        check();
        Gunsmith.sniperManager = manager;
    }

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.api.IMaterialFactory} to be used.
     *
     * @param factory Implementation of IMaterialFactory to be used, cannot be null
     */
    public static void setMaterialFactory(IMaterialFactory<?> factory)
    {
        checkNotNull(factory, "Cannot set a null MaterialFactory!");
        check();
        Gunsmith.materialFactory = factory;
    }

    /**
     * Sets the implementation of {@link com.voxelplugineering.voxelsniper.api.IWorldFactory} to be used.
     *
     * @param factory Implementation of IWorldFactory to be used, cannot be null
     */
    public static void setWorldFactory(IWorldFactory factory)
    {
        checkNotNull(factory, "Cannot set a null WorldFactory!");
        check();
        Gunsmith.worldFactory = factory;
    }

    /**
     * Gets the instance of the implementation of IVoxelSniper.
     *
     * @return The instance of the implementation of IVoxelSniper
     */
    public static IVoxelSniper getVoxelSniper()
    {
        return plugin;
    }

    /**
     * Returns the initialization state of Gunsmith.
     * 
     * @return the initialization state
     */
    public static boolean isEnabled()
    {
        return isPluginEnabled;
    }

    /**
     * Gets the instance of the implementation of IPermissionProxy.
     *
     * @return The instance of the implementation of IPermissionProxy
     */
    public static IPermissionProxy getPermissionProxy()
    {
        return permissionProxy;
    }

    /**
     * Gets the instance of the implementation of IBrushLoader.
     *
     * @return The instance of the implementation of IBrushLoader
     */
    public static IBrushLoader getDefaultBrushLoader()
    {
        return defaultBrushLoader;
    }

    /**
     * Gets the instance of the implementation of IBrushLoader.
     *
     * @return The instance of the implementation of IBrushLoader
     */
    public static IBrushManager getGlobalBrushManager()
    {
        return globalBrushManager;
    }

    /**
     * Gets the instance of the implementation of EventBus.
     *
     * @return The instance of the implementation of EventBus
     */
    public static EventBus getEventBus()
    {
        return eventBus;
    }

    /**
     * Gets the instance of the implementation of CommandHandler.
     *
     * @return The instance of the implementation of CommandHandler
     */
    public static CommandHandler getCommandHandler()
    {
        return commandHandler;
    }

    /**
     * Gets the instance of the implementation of ISniperFactory.
     *
     * @return The instance of the implementation of ISniperFactory
     */
    public static ISniperFactory<?> getSniperManager()
    {
        return sniperManager;
    }

    /**
     * Gets the instance of the implementation of CommonEventHandler.
     *
     * @return The instance of the implementation of CommonEventHandler
     */
    public static CommonEventHandler getDefaultEventHandler()
    {
        return defaultEventHandler;
    }

    /**
     * Gets the instance of the implementation of IMaterialFactory.
     *
     * @return The instance of the implementation of IMaterialFactory
     */
    public static IMaterialFactory<?> getMaterialFactory()
    {
        return materialFactory;
    }

    /**
     * Gets the instance of the implementation of IWorldFactory.
     *
     * @return The instance of the implementation of IWorldFactory
     */
    public static IWorldFactory getWorldFactory()
    {
        return worldFactory;
    }

    /**
     * Gets the instance of the implementation of IConfiguration.
     *
     * @return The instance of the implementation of IConfiguration
     */
    public static IConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * Returns the global logger for Gunsmith.
     * 
     * @return the logger
     */
    public static ILogger getLogger()
    {
        return logDistributor;
    }

    /**
     * Returns the logging distribution manager for Gunsmith.
     * 
     * @return the logging distributor
     */
    public static ILoggingDistributor getLoggingDistributor()
    {
        return logDistributor;
    }

    /**
     * Returns the compiler factory for compiling VSL scripts.
     * 
     * @return the factory
     */
    public static IGraphCompilerFactory getCompilerFactory()
    {
        return compilerFactory;
    }

    /**
     * Should be called at the start of the initialization process. Sets up default states before the specific implementation registers its overrides.
     */
    public static void beginInit()
    {
        check();

        //Create standard log distributor
        logDistributor = new CommonLoggingDistributor();
        logDistributor.init();
        //TODO register standard gunsmith loggers here

        getLogger().info("Starting Gunsmith initialization process.");

        //Register vsl types for common impl types
        Type.registerType("COMMONBLOCK", "com/voxelplugineering/voxelsniper/common/CommonBlock");
        Type.registerType("COMMONLOCATION", "com/voxelplugineering/voxelsniper/common/CommonLocation");
        Type.registerType("COMMONMATERIAL", "com/voxelplugineering/voxelsniper/common/CommonMaterial");
        Type.registerType("COMMONVECTOR", "com/voxelplugineering/voxelsniper/common/CommonVector");
        Type.registerType("COMMONWORLD", "com/voxelplugineering/voxelsniper/common/CommonWorld");
        Type.registerType("SHAPE", "com/voxelplugineering/voxelsniper/shape/Shape");

        //Create the eventBus for all Gunsmith events
        eventBus = new AsyncEventBus(java.util.concurrent.Executors.newCachedThreadPool());

        defaultEventHandler = new CommonEventHandler();
        eventBus.register(defaultEventHandler);
        //default event handler is registered here so that if a plugin wishes it can unregister the
        //event handler and register its own in its place

        configuration = new Configuration();
        configuration.registerContainer(BaseConfiguration.class);
        //configuration is also setup here so that any values can be overwritten from the specific impl

        //Setup the VSL graph compiler
        compilerFactory = new GraphCompilerFactory();
        compilerFactory.registerCompiler(IBrush.class, new BrushCompiler()); //the compiler for all brushes
    }

    /**
     * Finalize the initialization process. Contains validations to ensure that the initialization was completed correctly and completely.
     */
    public static void finish()
    {
        check();
        if (plugin == null || globalBrushManager == null || defaultBrushLoader == null || permissionProxy == null || materialFactory == null
                || worldFactory == null || sniperManager == null || logDistributor == null)
        {
            isPluginEnabled = false;
            throw new IllegalStateException("VoxelSniper was not properly setup while loading");
        }
        getLogger().info("Gunsmith initialization finalized.");
        isPluginEnabled = true;
    }

    /**
     * Checks that Gunsmith is still in the initialization phase.
     */
    private static void check()
    {
        if (isPluginEnabled)
        {
            throw new IllegalStateException("VoxelSniper is already enabled!");
        }
    }

    /**
     * Stops Gunsmith and dereferences all handlers and managers.
     */
    public static void stop()
    {
        if (!isPluginEnabled)
        {
            throw new IllegalStateException("VoxelSniper has not been enabled yet, cannot stop!");
        }
        isPluginEnabled = false;
        plugin = null;
        defaultBrushLoader = null;
        defaultEventHandler = null;
        if (globalBrushManager != null)
        {
            globalBrushManager.stop();
        }
        globalBrushManager = null;
        eventBus = null;
        commandHandler = null;
        permissionProxy = null;
        if (materialFactory != null)
        {
            materialFactory.stop();
        }
        materialFactory = null;
        worldFactory = null;
        if (sniperManager != null)
        {
            sniperManager.stop();
        }
        sniperManager = null;
        if (logDistributor != null)
        {
            logDistributor.stop();
        }
        logDistributor = null;
    }
}
