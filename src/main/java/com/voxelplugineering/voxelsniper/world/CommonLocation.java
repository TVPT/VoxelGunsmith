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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
    private final Vector3d position;

    /**
     * Creates a new {@link CommonLocation}.
     * 
     * @param world the world, cannot be null
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public CommonLocation(World world, double x, double y, double z)
    {
        this(world, new Vector3d(x, y, z));
    }

    /**
     * Creates a new {@link CommonLocation}.
     * 
     * @param world The world
     * @param position The position
     */
    public CommonLocation(World world, Vector3d position)
    {
        this.world = checkNotNull(world, "World cannot be null");
        this.position = checkNotNull(position, "Position cannot be null");
    }

    @Override
    public World getWorld()
    {
        return this.world;
    }

    @Override
    public Vector3i getFlooredPosition()
    {
        return new Vector3i(this.position);
    }

    @Override
    public double getX()
    {
        return this.position.getX();
    }

    @Override
    public double getY()
    {
        return this.position.getY();
    }

    @Override
    public double getZ()
    {
        return this.position.getZ();
    }

    @Override
    public int getFlooredX()
    {
        return (int) Math.floor(this.position.getX());
    }

    @Override
    public int getFlooredY()
    {
        return (int) Math.floor(this.position.getY());
    }

    @Override
    public int getFlooredZ()
    {
        return (int) Math.floor(this.position.getZ());
    }

    @Override
    public Location add(double x, double y, double z)
    {
        return new CommonLocation(this.world, this.position.add(x, y, z));
    }

    @Override
    public Location add(Vector3i v)
    {
        return new CommonLocation(this.world, this.position.getX() + v.getX(), this.position.getY() + v.getY(), this.position.getZ() + v.getZ());
    }

    @Override
    public Location add(Vector3d vector)
    {
        return new CommonLocation(this.world, this.position.add(vector));
    }

    @Override
    public Location add(Location location)
    {
        checkArgument(location.getWorld().equals(this.world), "Worlds must match when adding locations");
        return new CommonLocation(this.world, this.position.add(location.toVector()));
    }

    @Override
    public Location clone()
    {
        return new CommonLocation(this.world, this.position);
    }

    @Override
    public String toString()
    {
        return "CommonLocation (" + this.world.getName() + ": " + this.position.toString() + " )";
    }

    @Override
    public Vector3d toVector()
    {
        return this.position;
    }

}
