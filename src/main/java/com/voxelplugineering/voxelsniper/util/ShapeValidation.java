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
package com.voxelplugineering.voxelsniper.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * A set of utilities for validating various shapes.
 */
public class ShapeValidation
{

    /**
     * Checks whether the given shape is a disc.
     * 
     * @param shape The shape
     * @return Is a disc
     */
    public static boolean isDisc(Shape shape)
    {
        checkNotNull(shape);
        if (shape.getHeight() != 1)
        {
            return false;
        }
        double rx = (shape.getWidth() - 1) / 2d;
        double rz = (shape.getWidth() - 1) / 2d;
        Vector3i orig = shape.getOrigin();
        for (int x = 0; x < shape.getWidth(); x++)
        {
            int dx = x - orig.getX();
            for (int z = 0; z < shape.getLength(); z++)
            {
                int dz = z - orig.getZ();
                if (shape.get(x, 0, z, false) ^ ((dx / rx) * (dx / rx) + (dz / rz) * (dz / rz) <= 1))
                {
                    return false;
                }
            }
        }
        return true;
    }

}
