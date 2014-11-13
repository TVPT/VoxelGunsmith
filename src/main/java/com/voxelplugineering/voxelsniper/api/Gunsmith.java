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

public final class Gunsmith
{

    private static IVoxelSniper plugin = null;
    private static IPermissionProxy permissionProxy = null;
    private static IBrushManager globalBrushManager = null;
    private static IBrushLoader defaultBrushLoader = null;
    private static CommandHandler commandHandler = null;
    private static CommonEventHandler defaultEventHandler = null;
    private static EventBus eventBus = null;
    private static ISniperManager<?> sniperManager = null;

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

    public static void setSniperManager(ISniperManager<?> manager)
    {
        checkNotNull(manager, "Cannot set a null SniperManager!");
        check();
        Gunsmith.sniperManager = manager;
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

    /**
     * DO NOT STORE A NON-WEAK REFERENCE TO THIS!!!
     * 
     * @return
     */
    public static EventBus getEventBus()
    {
        return eventBus;
    }

    public static CommandHandler getCommandHandler()
    {
        return commandHandler;
    }

    public static ISniperManager<?> getSniperManager()
    {
        return sniperManager;
    }

    public CommonEventHandler getDefaultEventHandler()
    {
        return defaultEventHandler;
    }

    public static void beginInit()
    {
        check();
        eventBus = new EventBus();
        defaultEventHandler = new CommonEventHandler();
        eventBus.register(defaultEventHandler);
        //default event handler is registered here so that if a plugin wishes it can unregister the
        //event handler and register its own in its place
    }

    public static void finish()
    {
        check();
        if (plugin == null || globalBrushManager == null || defaultBrushLoader == null || permissionProxy == null)
        {
            isPluginEnabled = false;
            throw new IllegalStateException("VoxelSniper was not properly setup while loading");
        }
        isPluginEnabled = true;
    }

    private static void check()
    {
        if (isPluginEnabled)
        {
            throw new IllegalStateException("VoxelSniper is already enabled!");
        }
    }

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
