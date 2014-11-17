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
package com.voxelplugineering.voxelsniper.api;

import com.voxelplugineering.voxelsniper.common.CommonMaterial;

/**
 * A factory for {@link CommonMaterial}s wrapping a Material from the underlying implementation.
 * 
 * @param <T> The material class of the specific implementation
 */
public interface IMaterialFactory<T> extends IManager
{

    /**
     * Get the material with the given name.
     * 
     * @param name the name of the material
     * @return the associated {@link CommonMaterial}
     */
    CommonMaterial<T> getMaterial(String name);

    /**
     * Registers a material from the specific implementation with this factory.
     * 
     * @param name the name of the material
     * @param material the material object from the underlying implementation.
     */
    void registerMaterial(String name, CommonMaterial<T> material);

    /**
     * Returns the {@link CommonMaterial} representation of air/empty space in the underlying implementation.
     * 
     * @return the material of air
     */
    CommonMaterial<T> getAirMaterial();

}
