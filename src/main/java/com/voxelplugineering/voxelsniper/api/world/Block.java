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
package com.voxelplugineering.voxelsniper.api.world;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.core.util.math.Vector3i;

/**
 * Represents a material at a location in a world.
 */
public interface Block
{

    /**
     * Gets the world that this block is within.
     * 
     * @return The world
     */
    World getWorld();

    /**
     * Gets the location of this block.
     * 
     * @return The location
     */
    Location getLocation();

    /**
     * Gets the position of this block within the world.
     * 
     * @return The position
     */
    Vector3i getPosition();

    /**
     * Gets the material currently at the location of this block. Unless overridden by the specific
     * implementation this material may be out of date to the underlying world.
     * 
     * @return The material
     */
    Material getMaterial();

    /**
     * Gets the block at the given offset relative to this block.
     * 
     * @param offset The offset
     * @return The block, if available
     */
    Optional<Block> withOffset(Vector3i offset);

    //TODO block states?

}
