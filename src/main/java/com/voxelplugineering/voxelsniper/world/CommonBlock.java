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

import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;

import java.util.Optional;

/**
 * A combination location and material representation of a single voxel. The location is immutable.
 */
public class CommonBlock implements Block
{

    private final Location location;
    private final MaterialState material;

    /**
     * Creates a new CommonBlock with the given location and material.
     * 
     * @param location The location, cannot be null
     * @param material The material, cannot be null
     */
    public CommonBlock(Location location, MaterialState material)
    {
        this.location = checkNotNull(location, "Location cannot be null");
        this.material = checkNotNull(material, "Material cannot be null");
    }

    @Override
    public final Location getLocation()
    {
        return this.location;
    }

    @Override
    public MaterialState getMaterial()
    {
        return this.material;
    }

    @Override
    public World getWorld()
    {
        return this.location.getWorld();
    }

    @Override
    public Vector3i getPosition()
    {
        return this.location.getFlooredPosition();
    }

    @Override
    public Optional<Block> withOffset(Vector3i offset)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
