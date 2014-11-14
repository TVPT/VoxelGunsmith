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

import com.google.common.eventbus.EventBus;
import com.voxelplugineering.voxelsniper.common.command.CommandHandler;
import com.voxelplugineering.voxelsniper.common.event.CommonEventHandler;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.config.Configuration;

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
     * The initialization state of Gunsmith.
     */
    private static boolean isPluginEnabled = false;

    public static void setPlugin(IVoxelSniper sniper)
    {
        checkNotNull(sniper, "Cannot set a null plugin!");
        check();
        Gunsmith.plugin = sniper;
    }

    public static void setPermissionProxy(IPermissionProxy permissionProxy)
    {
        checkNotNull(permissionProxy, "Cannot set a null Permission Proxy!");
        check();
        Gunsmith.permissionProxy = permissionProxy;
    }

    public static void setGlobalBrushManager(IBrushManager brushManager)
    {
        checkNotNull(brushManager, "Cannot set a null BrushManager!");
        check();
        Gunsmith.globalBrushManager = brushManager;
    }

    public static void setDefaultBrushLoader(IBrushLoader brushLoader)
    {
        checkNotNull(brushLoader, "Cannot set a null BrushLoader!");
        check();
        Gunsmith.defaultBrushLoader = brushLoader;
    }

    public static void setCommandHandler(CommandHandler command)
    {
        checkNotNull(command, "Cannot set a null CommandHandler!");
        check();
        Gunsmith.commandHandler = command;
    }

    public static void setSniperManager(ISniperFactory<?> manager)
    {
        checkNotNull(manager, "Cannot set a null SniperManager!");
        check();
        Gunsmith.sniperManager = manager;
    }

    public static void setMaterialFactory(IMaterialFactory<?> factory)
    {
        checkNotNull(factory, "Cannot set a null MaterialFactory!");
        check();
        Gunsmith.materialFactory = factory;
    }

    public static void setWorldFactory(IWorldFactory factory)
    {
        checkNotNull(factory, "Cannot set a null WorldFactory!");
        check();
        Gunsmith.worldFactory = factory;
    }

    public static IVoxelSniper getVoxelSniper()
    {
        return plugin;
    }

    public static IPermissionProxy getPermissionProxy()
    {
        return permissionProxy;
    }

    public static IBrushLoader getDefaultBrushLoader()
    {
        return defaultBrushLoader;
    }

    public static IBrushManager getGlobalBrushManager()
    {
        return globalBrushManager;
    }

    public static EventBus getEventBus()
    {
        return eventBus;
    }

    public static CommandHandler getCommandHandler()
    {
        return commandHandler;
    }

    public static ISniperFactory<?> getSniperManager()
    {
        return sniperManager;
    }

    public static CommonEventHandler getDefaultEventHandler()
    {
        return defaultEventHandler;
    }

    public static IMaterialFactory<?> getMaterialFactory()
    {
        return materialFactory;
    }

    public static IWorldFactory getWorldFactory()
    {
        return worldFactory;
    }

    public static IConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * Should be called at the start of the initialization process. Sets up default states before the specific implementation registeres its
     * overrides.
     */
    public static void beginInit()
    {
        check();
        eventBus = new EventBus();
        defaultEventHandler = new CommonEventHandler();
        eventBus.register(defaultEventHandler);
        //default event handler is registered here so that if a plugin wishes it can unregister the
        //event handler and register its own in its place

        configuration = new Configuration();
        configuration.registerContainer(new BaseConfiguration());
        //configuration is also setup here so that any values can be overwritten from the specific impl
    }

    /**
     * Finalize the initialization process. Contains validations to ensure that the initialization was completed correctly and completely.
     */
    public static void finish()
    {
        check();
        if (plugin == null || globalBrushManager == null || defaultBrushLoader == null || permissionProxy == null || materialFactory == null
                || worldFactory == null)
        {
            isPluginEnabled = false;
            throw new IllegalStateException("VoxelSniper was not properly setup while loading");
        }
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
     * Stops gunsmith and dereferences all handlers and managers.
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
        materialFactory = null;
        worldFactory = null;
        if (sniperManager != null)
        {
            sniperManager.stop();
        }
        sniperManager = null;
    }

}

/*
 * TODO:
 * ------------------------------------------
 * -Logging with Log4j preferably
 * -Configuration handled properly
 * -comment everything please!!!! (especially the interfaces and Common utility classes)
 * -allow naming loaders and selecting specific loaders when loading brushes eg. global:ball or name:other_persons_brush
 * 
 * 
 * 
 */
