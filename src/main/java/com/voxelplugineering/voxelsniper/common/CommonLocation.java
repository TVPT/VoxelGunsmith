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

/**
 * Representation of a location within a world.
 */
public final class CommonLocation implements Cloneable
{
    /**
     * The world that the location is within.
     */
    private final CommonWorld world;

    private final double x;
    private final double y;
    private final double z;

    /**
     * Creates a new CommonLocation for use in the API.
     *
     * @param world the world
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public CommonLocation(CommonWorld world, double x, double y, double z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the world for the location.
     * 
     * @return the world
     */
    public CommonWorld getWorld()
    {
        return this.world;
    }

    /**
     * The x-axis position within the world.
     * 
     * @return the X position.
     */
    public double getX()
    {
        return this.x;
    }

    /**
     * The y-axis position within the world.
     * 
     * @return the Y position.
     */
    public double getY()
    {
        return this.y;
    }

    /**
     * The z-axis position within the world.
     * 
     * @return the Z position.
     */
    public double getZ()
    {
        return this.z;
    }

    /**
     * Returns the x-axis position of this location rounded down to the nearest integer increment.
     * 
     * @return the integer position
     */
    public int getFlooredX()
    {
        return (int) Math.floor(this.x);
    }

    /**
     * Returns the y-axis position of this location rounded down to the nearest integer increment.
     * 
     * @return the integer position
     */
    public int getFlooredY()
    {
        return (int) Math.floor(this.y);
    }

    /**
     * Returns the z-axis position of this location rounded down to the nearest integer increment.
     * 
     * @return the integer position
     */
    public int getFlooredZ()
    {
        return (int) Math.floor(this.z);
    }

    /**
     * Returns a new {@link CommonLocation} which represents this position offset by the given amounts.
     * 
     * @param x the X offset
     * @param y the Y offset
     * @param z the Z offset
     * @return the new {@link CommonLocation}
     */
    public CommonLocation add(double x, double y, double z)
    {
        return new CommonLocation(this.world, this.x + x, this.y + y, this.z + z);
    }

    /**
     * Returns a new {@link CommonLocation} representing the same location.
     *
     * @return a newly created CommonLocation
     */
    @Override
    public CommonLocation clone()
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
}
