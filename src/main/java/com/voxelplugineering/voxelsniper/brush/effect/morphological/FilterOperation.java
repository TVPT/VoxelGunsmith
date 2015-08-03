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
package com.voxelplugineering.voxelsniper.brush.effect.morphological;

import com.voxelplugineering.voxelsniper.world.World;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;

import com.google.common.base.Optional;

/**
 * 
 */
public interface FilterOperation
{

    /**
     * Returns the name of this operation as it would be labeled as a brush.
     */
    String getName();

    /**
     * Performs one step of the operation at some position in a structuring element with respect to
     * the material at that position. The order of the positions hit should not be assumed, and the
     * number of positions to be checked is arbitrary.
     * 
     * x, y and z represent a location in the world, w. dx, dy and dz are all distances relative to
     * the origin of the structuring element. m is the material in the world at that position.
     * 
     * Returns true if no more checks could possibly change the result. Returns false if the above
     * does not hold.
     * 
     * TODO: Add argument for type of border-check to perform when outside the world's extent.
     */
    boolean checkPosition(int x, int y, int z, int dx, int dy, int dz, World w, MaterialState m);

    /**
     * Provides a material as a result of the morphological operation.
     */
    Optional<MaterialState> getResult();

    /**
     * Clears any memory relating to the previous operation performed or being performed to a state
     * similar to a newly constructed operation.
     * 
     * Reset should be called each time after deciding a result to avoid side-effects.
     */
    void reset();

}
