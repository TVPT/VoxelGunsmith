package com.voxelplugineering.voxelsniper.api;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Gunsmith
{

    private static IVoxelSniper plugin;
    private static IPermissionProxy permissionProxy;
    private static IBrushManager brushManager;
    private static boolean isPluginEnabled = false;

    public static void setPlugin(IVoxelSniper sniper)
    {
        checkNotNull(plugin, "Cannot set a null plugin!");
        checkArgument(!plugin.isEnabled(), "Cannot set a plugin that is already enabled!");
        check();
        Gunsmith.plugin = sniper;
    }

    public static void setPermissionProxy(IPermissionProxy permissionProxy)
    {
        checkNotNull(permissionProxy, "Cannot set a null Permission Proxy!");
        check();
        Gunsmith.permissionProxy = permissionProxy;
    }

    public static void setBrushManager(IBrushManager brushManager)
    {
        checkNotNull(brushManager, "Cannot set a null BrushManager!");
        check();
        Gunsmith.brushManager = brushManager;
    }

    public static IVoxelSniper getVoxelSniper()
    {
        return plugin;
    }

    public static IPermissionProxy getPermissionProxy()
    {
        return permissionProxy;
    }

    public static IBrushManager getBrushManager()
    {
        return brushManager;
    }

    public static void finish()
    {
        isPluginEnabled = true;
    }

    private static void check()
    {
        if (isPluginEnabled)
        {
            throw new IllegalStateException("VoxelSniper is already enabled!");
        }
    }
}
