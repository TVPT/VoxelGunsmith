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

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.world.material.Material;

/**
 * Represents a material mask and shape combined.
 */
public interface MaterialShape extends Shape
{

    /**
     * Gets the underlying shape.
     * 
     * @return The shape
     */
    Shape getShape();

    /**
     * Gets the material for the given location.
     * 
     * @param x The X position
     * @param y The y position
     * @param z The Z position
     * @param relative Whether to offset by the origin
     * @return The material
     */
    Optional<Material> getMaterial(int x, int y, int z, boolean relative);

    /**
     * Sets the material at the given location.
     * 
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     * @param relative Whether to offset by the origin
     * @param material The new material
     */
    void setMaterial(int x, int y, int z, boolean relative, Material material);

    /**
     * Floods the shape with the given material
     * 
     * @param material The new material
     */
    void flood(Material material);

    /**
     * Resets the shape to the default material.
     */
    void reset();

}