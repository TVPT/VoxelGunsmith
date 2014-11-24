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

/**
 * Represents a single change.
 */
public abstract class Change
{

    private final int x;
    private final int y;
    private final int z;

    /**
     * Creates a new change at the given location.
     * 
     * @param x the x position of the change
     * @param y the y position of the change
     * @param z the z position of the change
     */
    public Change(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x position of this change.
     * 
     * @return the x position
     */
    public int getX()
    {
        return this.x;
    }

    /**
     * Returns the y position of this change.
     * 
     * @return the y position
     */
    public int getY()
    {
        return this.y;
    }

    /**
     * Returns the z position of this change.
     * 
     * @return the z position
     */
    public int getZ()
    {
        return this.z;
    }

    /**
     * Returns a new Change representing the inverse operation of this change.
     * 
     * @return the inverse change
     */
    public abstract Change getInverse();

    /**
     * Returns a copy of this change.
     * 
     * @return the clone
     */
    public abstract Change clone();

}
