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
package com.voxelplugineering.voxelsniper.world;

import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;

import com.google.common.base.Optional;

/**
 * Represents a volume of space with discrete block data within it.
 */
public interface BlockVolume
{

    /**
     * Gets the block at the given xyz coordinates.
     * 
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     * @return The block
     */
    Optional<Block> getBlock(int x, int y, int z);

    /**
     * Gets the block at the given location. or {@link Optional#absent()} if the location is not in
     * this volume.
     * 
     * @param location The location
     * @return The block
     */
    Optional<Block> getBlock(Location location);

    /**
     * Gets the block at the given vector position within this volume.
     * 
     * @param vector The position
     * @return The block
     */
    Optional<Block> getBlock(Vector3i vector);

    /**
     * Sets the block at the given xyz coordinates to the given material.
     * 
     * @param material The material
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     */
    void setBlock(MaterialState material, int x, int y, int z, boolean update);

    /**
     * Sets the block at the given location to the given material.
     * 
     * @param material The material
     * @param location The location
     */
    void setBlock(MaterialState material, Location location, boolean update);

    /**
     * Sets the block at the given vector position to the given material.
     * 
     * @param material The material
     * @param vector The position
     */
    void setBlock(MaterialState material, Vector3i vector, boolean update);

}
