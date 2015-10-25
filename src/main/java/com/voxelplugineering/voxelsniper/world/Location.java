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

import com.voxelplugineering.voxelsniper.util.math.Vector3d;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Represents a location within a world.
 */
public interface Location
{

    /**
     * Gets the World that this location is within.
     * 
     * @return The world
     */
    World getWorld();

    /**
     * Gets the position of this location as a vector.
     * 
     * @return The position
     */
    Vector3i getFlooredPosition();

    /**
     * The x-axis position within the world.
     * 
     * @return The X position.
     */
    double getX();

    /**
     * The y-axis position within the world.
     * 
     * @return The Y position.
     */
    double getY();

    /**
     * The z-axis position within the world.
     * 
     * @return The Z position.
     */
    double getZ();

    /**
     * Returns the x-axis position of this location rounded down to the nearest integer increment.
     * 
     * @return The integer position
     */
    int getFlooredX();

    /**
     * Returns the y-axis position of this location rounded down to the nearest integer increment.
     * 
     * @return The integer position
     */
    int getFlooredY();

    /**
     * Returns the z-axis position of this location rounded down to the nearest integer increment.
     * 
     * @return The integer position
     */
    int getFlooredZ();

    /**
     * Returns a new location representing this location offset by the given vector.
     * 
     * @param vector The vector offset
     * @return The new location
     */
    Location add(Vector3i vector);

    /**
     * Returns a new location representing this location offset by the given location.
     * 
     * @param location The vector offset
     * @return The new location
     */
    Location add(Location location);

    /**
     * Returns a new location representing this location offset by the given vector.
     * 
     * @param vector The vector offset
     * @return The new location
     */
    Location add(Vector3d vector);

    /**
     * Returns a new {@link CommonLocation} which represents this position offset by the given
     * amounts.
     * 
     * @param x The X offset
     * @param y The Y offset
     * @param z The Z offset
     * @return The new {@link CommonLocation}
     */
    Location add(double x, double y, double z);

    /**
     * Returns a new {@link CommonLocation} representing the same location.
     * 
     * @return A newly created CommonLocation
     */
    Location clone();

    /**
     * Returns the vector part of this location.
     * 
     * @return The vector
     */
    Vector3d toVector();

    /**
     * Gets a String representation of this location.
     * 
     * @return The string
     */
    @Override
    String toString();

}
