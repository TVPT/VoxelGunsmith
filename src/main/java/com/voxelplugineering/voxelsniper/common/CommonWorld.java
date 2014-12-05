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
package com.voxelplugineering.voxelsniper.common;

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.api.IMaterialRegistry;
import com.voxelplugineering.voxelsniper.common.factory.WeakWrapper;

/**
 * The representation of a world or scene of voxels. X and Z axes are assumed to be the horizontal axes and Y the vertical axis.
 */
public abstract class CommonWorld<T> extends WeakWrapper<T>
{
    
    /**
     * The material registry for this world.
     */
    private IMaterialRegistry<?> materials;

    /**
     * Create a world.
     * 
     * @param world the underlying world
     * @param materials the material registry for this world
     */
    public CommonWorld(T world, IMaterialRegistry<?> materials)
    {
        super(world);
        this.materials = materials;
    }
    
    /**
     * Returns the material registry for this world.
     * 
     * @return the material registry
     */
    public IMaterialRegistry<?> getMaterialRegistry()
    {
        return this.materials;
    }

    /**
     * Returns the name of the world.
     * 
     * @return the name
     */
    public abstract String getName();

    /**
     * Gets the chunk at the given coordinates.
     * 
     * @param x the chunk's x-axis position
     * @param y the chunk's y-axis position
     * @param z the chunk's z-axis position
     * @return the chunk
     */
    public abstract CommonChunk<?> getChunkAt(int x, int y, int z);

    /**
     * Returns the chunk at the given x, z coordinates. Assumes no y-axis arrangement of chunks (uses 0 for the y-axis). This method exists to better
     * support the minecraft philosophy of chunk arrangement, without restricting the implementation from using y-axis chunks.
     * 
     * @param x the chunk's x-axis position
     * @param z the chunk's z-axis position
     * @return the chunk
     */
    public CommonChunk<?> getChunkAt(int x, int z)
    {
        return getChunkAt(x, 0, z);
    }

    /**
     * Returns the chunk at the given location
     * 
     * @param location the location
     * @return the block
     */
    public CommonBlock getBlockAt(CommonLocation location)
    {
        checkNotNull(location, "Location cannot be null");
        return getBlockAt(location.getFlooredX(), location.getFlooredY(), location.getFlooredZ());
    }

    /**
     * Returns the block at the given coordinates.
     * 
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the block
     */
    public abstract CommonBlock getBlockAt(int x, int y, int z);

    /**
     * Sets the block at the given location to the given material.
     * 
     * @param location the location
     * @param material the new material
     */
    public void setBlockAt(CommonLocation location, CommonMaterial<?> material)
    {
        checkNotNull(location, "Location cannot be null");
        checkNotNull(material, "Material cannot be null");
        setBlockAt(location.getFlooredX(), location.getFlooredY(), location.getFlooredZ(), material);
    }

    /**
     * Sets the voxel at the given coordinates to the given material.
     * 
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @param material the new material
     */
    public abstract void setBlockAt(int x, int y, int z, CommonMaterial<?> material);
    
    /**
     * Sets the biome at the given location.
     * TODO this should probably have a y value as well even though minecraft doesn't use it for its biomes, another platform could, or minecraft could in the future.
     * 
     * @param x the x position to set
     * @param z the y position to set
     * @param biomeName the new biome
     */
    public abstract void setBiomeAt(int x, int z, CommonBiome<?> biomeName);

}
