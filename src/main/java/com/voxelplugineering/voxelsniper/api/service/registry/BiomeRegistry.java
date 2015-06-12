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
package com.voxelplugineering.voxelsniper.api.service.registry;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.service.Service;
import com.voxelplugineering.voxelsniper.api.world.biome.Biome;

/**
 * A registry for biome type.s
 * 
 * @param <T> The biome type
 */
public interface BiomeRegistry<T> extends Service
{

    /**
     * Gets the {@link Biome} with the given name.
     * 
     * @param name The name
     * @return The biome
     */
    Optional<Biome> getBiome(String name);

    /**
     * Gets the {@link Biome} which represents the given underlying type.
     * 
     * @param biome The underlying biome
     * @return The gunsmith biome
     */
    Optional<Biome> getBiome(T biome);

    /**
     * Registers a biome type.
     * 
     * @param name The name
     * @param object The underlying biome object
     * @param biome The gunsmith biome object
     */
    void registerBiome(String name, T object, Biome biome);

    /**
     * Gets a collection of all registered biomes.
     * 
     * @return The biomes
     */
    Iterable<Biome> getBiomes();

    /**
     * Gets the default biome.
     * 
     * @return The default biome
     */
    Biome getDefaultBiome();

}
