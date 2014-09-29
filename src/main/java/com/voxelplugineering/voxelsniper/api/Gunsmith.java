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
