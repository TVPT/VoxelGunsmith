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
package com.voxelplugineering.voxelsniper.core.world;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.core.util.math.Vector3i;

/**
 * A combination location and material representation of a single voxel. The
 * location is immutable.
 */
public class CommonBlock implements Block
{

    /**
     * The location of this voxel.
     */
    private final Location location;
    /**
     * The material of the voxel.
     */
    private Material material;

    /**
     * Creates a new CommonBlock with the given location and material
     * 
     * @param location the location, cannot be null
     * @param material the material, cannot be null
     */
    public CommonBlock(Location location, Material material)
    {
        checkNotNull(location, "Location cannot be null");
        checkNotNull(material, "Material cannot be null");
        this.location = location;
        this.material = material;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Location getLocation()
    {
        return this.location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Material getMaterial()
    {
        return this.material;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public World getWorld()
    {
        return this.location.getWorld();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3i getPosition()
    {
        return this.location.getFlooredPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Block> withOffset(Vector3i offset)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
