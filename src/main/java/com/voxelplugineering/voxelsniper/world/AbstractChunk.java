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

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.registry.WeakWrapper;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;

import java.util.Optional;

/**
 * An abstract chunk.
 * 
 * @param <T> The type of the underlying chunk.
 */
public abstract class AbstractChunk<T> extends WeakWrapper<T>implements Chunk
{

    private final World world;

    /**
     * Sets up an {@link AbstractChunk}.
     * 
     * @param chunk The chunk to wrap
     * @param world The parent world
     */
    public AbstractChunk(T chunk, World world)
    {
        super(chunk);
        this.world = world;
    }

    @Override
    public Optional<Block> getBlock(Location location)
    {
        checkNotNull(location);
        if (location.getWorld() != this.world)
        {
            return Optional.empty();
        }
        return getBlock(location.getFlooredX(), location.getFlooredY(), location.getFlooredZ());
    }

    @Override
    public Optional<Block> getBlock(Vector3i vector)
    {
        checkNotNull(vector);
        return getBlock(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public void setBlock(MaterialState material, Location location, boolean update)
    {
        checkNotNull(material);
        checkNotNull(location);
        if (location.getWorld() != this.world)
        {
            return;
        }
        setBlock(material, location.getFlooredX(), location.getFlooredY(), location.getFlooredZ(), update);
    }

    @Override
    public void setBlock(MaterialState material, Vector3i vector, boolean update)
    {
        checkNotNull(material);
        checkNotNull(vector);
        setBlock(material, vector.getX(), vector.getY(), vector.getZ(), update);
    }

    @Override
    public World getWorld()
    {
        return this.world;
    }

}
