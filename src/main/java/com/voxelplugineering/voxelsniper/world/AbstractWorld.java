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

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.registry.WeakWrapper;
import com.voxelplugineering.voxelsniper.service.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.shape.ComplexMaterialShape;
import com.voxelplugineering.voxelsniper.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.world.biome.Biome;
import com.voxelplugineering.voxelsniper.world.material.Material;

/**
 * An abstract world.
 * 
 * @param <T> The specific world type
 */
public abstract class AbstractWorld<T> extends WeakWrapper<T> implements World
{

    private final MaterialRegistry<?> mats;

    /**
     * Sets up the {@link AbstractWorld}.
     * 
     * @param value The underlying world object
     */
    public AbstractWorld(Context context, T value)
    {
        super(value);
        this.mats = context.getRequired(MaterialRegistry.class);
    }

    @Override
    public Optional<Block> getBlock(Location location)
    {
        checkNotNull(location);
        if (location.getWorld() != this)
        {
            return Optional.absent();
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
    public void setBlock(Material material, Location location)
    {
        checkNotNull(material);
        checkNotNull(location);
        if (location.getWorld() != this)
        {
            return;
        }
        setBlock(material, location.getFlooredX(), location.getFlooredY(), location.getFlooredZ());
    }

    @Override
    public void setBlock(Material material, Vector3i vector)
    {
        checkNotNull(material);
        checkNotNull(vector);
        setBlock(material, vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public Optional<Chunk> getChunk(Vector3i vector)
    {
        checkNotNull(vector);
        return getChunk(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public Optional<Biome> getBiome(Location location)
    {
        checkNotNull(location);
        if (location.getWorld() != this)
        {
            return Optional.absent();
        }
        return getBiome(location.getFlooredX(), location.getFlooredY(), location.getFlooredZ());
    }

    @Override
    public Optional<Biome> getBiome(Vector3i vector)
    {
        checkNotNull(vector);
        return getBiome(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public void setBiome(Biome biome, Location location)
    {
        checkNotNull(biome);
        checkNotNull(location);
        if (location.getWorld() != this)
        {
            return;
        }
        setBiome(biome, location.getFlooredX(), location.getFlooredY(), location.getFlooredZ());
    }

    @Override
    public void setBiome(Biome biome, Vector3i vector)
    {
        checkNotNull(biome);
        checkNotNull(vector);
        setBiome(biome, vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public MaterialShape getShapeFromWorld(Location origin, Shape shape)
    {
        checkNotNull(origin);
        checkNotNull(shape);
        MaterialShape mat = new ComplexMaterialShape(shape, this.mats.getAirMaterial());
        for (int x = 0; x < shape.getWidth(); x++)
        {
            int ox = x + origin.getFlooredX() - shape.getOrigin().getX();
            for (int y = 0; y < shape.getHeight(); y++)
            {
                int oy = y + origin.getFlooredY() - shape.getOrigin().getY();
                for (int z = 0; z < shape.getLength(); z++)
                {
                    int oz = z + origin.getFlooredZ() - shape.getOrigin().getZ();
                    if (shape.get(x, y, z, false))
                    {
                        Optional<Block> block = getBlock(ox, oy, oz);
                        if (!block.isPresent())
                        {
                            mat.unset(x, y, z, false);
                        } else
                        {
                            mat.setMaterial(x, y, z, false, block.get().getMaterial());
                        }
                    }
                }
            }
        }
        return mat;
    }

}
