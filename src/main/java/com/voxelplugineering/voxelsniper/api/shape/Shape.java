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

import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Represents a shape.
 */
public interface Shape
{

    /**
     * Gets the width.
     * 
     * @return The width
     */
    int getWidth();

    /**
     * Gets the height
     * 
     * @return The height
     */
    int getHeight();

    /**
     * Gets the length.
     * 
     * @return The length
     */
    int getLength();

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

    /**
     * Gets the volume of the shape.
     * 
     * @return The volume
     */
    int getVolume();

}
