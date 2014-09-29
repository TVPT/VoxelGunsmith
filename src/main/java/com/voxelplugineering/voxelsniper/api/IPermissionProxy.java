package com.voxelplugineering.voxelsniper.api;

import com.voxelplugineering.voxelsniper.common.CommonWorld;

public interface IPermissionProxy
{

    boolean isOp(final ISniper sniper);

    boolean hasPermission(final ISniper sniper, final String permission);

    boolean hasWorldPermission(final ISniper sniper, final CommonWorld world, final String permission);

    boolean hasWorldPermission(final ISniper sniper, final String worldName, final String permission);

    void addGlobalPermission(final ISniper sniper, final String permission);

    void addWorldPermission(final ISniper sniper, final CommonWorld world, final String permission);

    void addWorldPermission(final ISniper sniper, final String worldName, final String permission);

    void addTransientGlobalPermission(final ISniper sniper, final String permission);

    void addTransientWorldPermission(final ISniper sniper, final CommonWorld world, final String permission);

    void addTransientWorldPermission(final ISniper sniper, final String worldName, final String permission);

    void removeGlobalPermission(final ISniper sniper, final String permission);

    void removeWorldPermission(final ISniper sniper, final CommonWorld world, final String permission);

    void removeWorldPermission(final ISniper sniper, final String worldName, final String permission);

    void removeTransientGlobalPermission(final ISniper sniper, final String permission);

    void removeTransientWorldPermission(final ISniper sniper, final CommonWorld world, final String permission);

    void removeTransientWorldPermission(final ISniper sniper, final String worldName, final String permission);

}
