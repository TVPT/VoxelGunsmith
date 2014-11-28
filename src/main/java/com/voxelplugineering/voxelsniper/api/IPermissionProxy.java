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

import com.voxelplugineering.voxelsniper.common.CommonWorld;

/**
 * A proxy for the permission system of the specific implementation.
 */
public interface IPermissionProxy
{

    /**
     * Whether the player is an operator, or has a wildcard permission.
     * 
     * @param sniper the user to check, cannot be null
     * @return whether they are an operator
     */
    boolean isOp(final ISniper sniper);

    /**
     * Checks if the user has the given permission node. Supports wildcards as permission nodes are made of a dot-separated sequence of nodes.
     * 
     * @param sniper the user to check, cannot be null
     * @param permission the permission node, cannot be null or empty
     * @return the result of the check
     */
    boolean hasPermission(final ISniper sniper, final String permission);

    /**
     * Checks whether the user has the given node within the given world.
     * 
     * @param sniper the user to check, cannot be null
     * @param world the world to check the permission within, cannot be null
     * @param permission the permission node, cannot be null or empty
     * @return the result of the check
     */
    boolean hasWorldPermission(final ISniper sniper, final CommonWorld world, final String permission);

    /**
     * Checks whether the user has the given node within the given world.
     * 
     * @param sniper the user to check, cannot be null
     * @param worldName the name of the world to check the permission within, cannot be null or empty
     * @param permission the permission node, cannot be null or empty
     * @return the result of the check
     */
    boolean hasWorldPermission(final ISniper sniper, final String worldName, final String permission);

    /**
     * Adds the given permission to the user with a global context.
     * 
     * @param sniper the user to add the permission to, cannot be null
     * @param permission the permission node to add, cannot be null or empty
     */
    void addGlobalPermission(final ISniper sniper, final String permission);

    /**
     * Adds the given permission to the user with a context of only within the given world.
     * 
     * @param sniper the user to add the permission to, cannot be null
     * @param world the world to use as context, cannot be null
     * @param permission the permission to add, cannot be null or empty
     */
    void addWorldPermission(final ISniper sniper, final CommonWorld world, final String permission);

    /**
     * Adds the given permission to the user with a context of only within the given world, referenced by name.
     * 
     * @param sniper the user to add the permission to, cannot be null
     * @param worldName the world to use as context, cannot be null or empty
     * @param permission the permission to add, cannot be null or empty
     */
    void addWorldPermission(final ISniper sniper, final String worldName, final String permission);

    /**
     * Adds the given permission to the user that will be removed if the following happens:
     * <ul>
     * <li>The user logs out</li>
     * <li>The server is reloaded</li>
     * </ul>
     * . The permission is not granted permanently to the user.
     *
     * @param sniper the user to add the transient permission to, cannot be null
     * @param permission the permission to add, cannot be null or empty
     */
    void addTransientGlobalPermission(final ISniper sniper, final String permission);

    /**
     * Adds the given permission to the user that will be removed if the following happens:
     * <ul>
     * <li>The user logs out</li>
     * <li>The server is reloaded</li>
     * </ul>
     * . The permission is not granted permanently to the user.
     *
     * @param sniper the user to add the transient permission to, cannot be null
     * @param world the world to use as context, cannot be null
     * @param permission the permission to add, cannot be null or empty
     */
    void addTransientWorldPermission(final ISniper sniper, final CommonWorld world, final String permission);

    /**
     * Adds the given permission to the user that will be removed if the following happens:
     * <ul>
     * <li>The user logs out</li>
     * <li>The server is reloaded</li>
     * </ul>
     * . The permission is not granted permanently to the user.
     *
     * @param sniper the user to add the transient permission to, cannot be null
     * @param worldName the world to use as context, cannot be null or empty
     * @param permission the permission to add, cannot be null or empty
     */
    void addTransientWorldPermission(final ISniper sniper, final String worldName, final String permission);

    /**
     * Removes the given permission from the user with a global context.
     * 
     * @param sniper the user to remove the permission from, cannot be null
     * @param permission the permission node to remove, cannot be null or empty
     */
    void removeGlobalPermission(final ISniper sniper, final String permission);

    /**
     * Removes the given permission from the user with a context of only within the given world.
     * 
     * @param sniper the user to remove the permission from, cannot be null
     * @param world the world to use as context, cannot be null
     * @param permission the permission to remove, cannot be null or empty
     */
    void removeWorldPermission(final ISniper sniper, final CommonWorld world, final String permission);

    /**
     * Removes the given permission from the user with a context of only within the given world, referenced by name.
     * 
     * @param sniper the user to remove the permission from, cannot be null
     * @param worldName the world to use as context, cannot be null or empty
     * @param permission the permission to remove, cannot be null or empty
     */
    void removeWorldPermission(final ISniper sniper, final String worldName, final String permission);

    /**
     * Removes the given permission from the user with a context that the permission was granted as a transient permission. Transient permissions are
     * not permanent and may be lost if the following happens:
     * <ul>
     * <li>The server is reloaded</li>
     * <li>The user logs out</li>
     * </ul>
     * .
     *
     * @param sniper the user to remove the transient permission from, cannot be null
     * @param permission the permission to remove, cannot be null or empty
     */
    void removeTransientGlobalPermission(final ISniper sniper, final String permission);

    /**
     * Removes the given permission from the user with a context that the permission was granted as a transient permission. Transient permissions are
     * not permanent and may be lost if the following happens:
     * <ul>
     * <li>The server is reloaded</li>
     * <li>The user logs out</li>
     * </ul>
     * .
     *
     * @param sniper the user to remove the transient permission from, cannot be null
     * @param world the world to use as context, cannot be null
     * @param permission the permission to remove, cannot be null or empty
     */
    void removeTransientWorldPermission(final ISniper sniper, final CommonWorld world, final String permission);

    /**
     * Removes the given permission from the user with a context that the permission was granted as a transient permission. Transient permissions are
     * not permanent and may be lost if the following happens:
     * <ul>
     * <li>The server is reloaded</li>
     * <li>The user logs out</li>
     * </ul>
     * .
     *
     * @param sniper the user to remove the transient permission from, cannot be null
     * @param worldName the world to use as context, cannot be null or empty
     * @param permission the permission to remove, cannot be null or empty
     */
    void removeTransientWorldPermission(final ISniper sniper, final String worldName, final String permission);

}
