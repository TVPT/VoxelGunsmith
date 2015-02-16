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
package com.voxelplugineering.voxelsniper.api.permissions;

import com.voxelplugineering.voxelsniper.api.entity.living.Player;

/**
 * A proxy for the permission system of the specific implementation. TODO: group
 * support within gunsmith
 */
public interface PermissionProxy
{

    /**
     * Whether the player is an operator, or has a wildcard permission.
     * 
     * @param sniper the user to check, cannot be null
     * @return whether they are an operator
     */
    boolean isOp(final Player sniper);

    /**
     * Checks if the user has the given permission node. Supports wildcards as
     * permission nodes are made of a dot-separated sequence of nodes.
     * 
     * @param sniper the user to check, cannot be null
     * @param permission the permission node, cannot be null or empty
     * @return the result of the check
     */
    boolean hasPermission(final Player sniper, final String permission);

}
