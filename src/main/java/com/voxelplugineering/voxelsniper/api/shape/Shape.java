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
package com.voxelplugineering.voxelsniper.api.shape;

import com.voxelplugineering.voxelsniper.core.util.math.Vector3i;

/**
 * Represents a shape.
 */
public interface Shape
{

    /**
     * Returns the width of this shape (x-axis size). Note that this is not the width of the region
     * set, but rather the width of the total possible volume.
     * 
     * @return the width
     */
    int getWidth();

    /**
     * Returns the height of this shape (y-axis size). Note that this is not the height of the
     * region set, but rather the height of the total possible volume.
     * 
     * @return the height
     */
    int getHeight();

    /**
     * Returns the length of this shape (z-axis size). Note that this is not the length of the
     * region set, but rather the length of the total possible volume.
     * 
     * @return the length
     */
    int getLength();

    /**
     * Gets if this shape supports set/unset operations.
     * 
     * @return Supports changes
     */
    boolean isMutable();

    //TODO split mutable and immutable shapes into two types

    /**
     * Gets if the shape covers the given position.
     * 
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     * @param relative Whether to offset by the origin
     * @return If the shape covers the point
     */
    boolean get(int x, int y, int z, boolean relative);

    /**
     * Gets the origin of this shape.
     * 
     * @return The origin
     */
    Vector3i getOrigin();

    /**
     * Sets the shape to cover the given position.
     * 
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     * @param relative Whether to offset by the origin
     */
    void set(int x, int y, int z, boolean relative);

    /**
     * Unsets the shape at the given position.
     * 
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     * @param relative Whether to offset by the origin
     */
    void unset(int x, int y, int z, boolean relative);

    /**
     * Returns a copy of this shape.
     * 
     * @return The shape
     */
    Shape clone();

}
