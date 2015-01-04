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

import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.util.math.Vector3d;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Representation of a location within a world.
 */
public final class CommonLocation implements Cloneable, Location
{

    /**
     * The world that the location is within.
     */
    private final World world;
    private final double x;
    private final double y;
    private final double z;

    /**
     * Creates a new CommonLocation for use in the API.
     *
     * @param world the world, cannot be null
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public CommonLocation(World world, double x, double y, double z)
    {
        checkNotNull(world, "World cannot be null");
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public World getWorld()
    {
        return this.world;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getX()
    {
        return this.x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getY()
    {
        return this.y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getZ()
    {
        return this.z;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFlooredX()
    {
        return (int) Math.floor(this.x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFlooredY()
    {
        return (int) Math.floor(this.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFlooredZ()
    {
        return (int) Math.floor(this.z);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location add(double x, double y, double z)
    {
        return new CommonLocation(this.world, this.x + x, this.y + y, this.z + z);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location add(Vector3i vector)
    {
        return new CommonLocation(this.world, this.x + vector.getX(), this.y + vector.getY(), this.z + vector.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location add(Vector3d vector)
    {
        return new CommonLocation(this.world, this.x + vector.getX(), this.y + vector.getY(), this.z + vector.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location add(Location location)
    {
        return new CommonLocation(this.world, this.x + location.getX(), this.y + location.getY(), this.z + location.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location clone()
    {
        return new CommonLocation(this.world, this.x, this.y, this.z);
    }

    /**
     * Returns a String representation of this location. The format is as follows:
     * <p>
     * CommonLocation ({this.world.getName()}: {this.x}, {this.y}, {this.z})
     * 
     * @return the string representation
     */
    public String toString()
    {
        return "CommonLocation (" + this.world.getName() + ": " + this.x + ", " + this.y + ", " + this.z + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3d toVector()
    {
        return new Vector3d(this.x, this.y, this.z);
    }
}
