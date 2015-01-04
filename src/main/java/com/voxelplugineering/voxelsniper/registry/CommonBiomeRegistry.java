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
package com.voxelplugineering.voxelsniper.registry;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.registry.BiomeRegistry;
import com.voxelplugineering.voxelsniper.api.world.biome.Biome;

/**
 * A common biome registry for biomes.
 * 
 * @param <T> The biome type
 */
public class CommonBiomeRegistry<T> implements BiomeRegistry<T>
{

    private final WeakRegistry<T, Biome> registry;
    Biome defaultBiome = null;

    /**
     * Creates a new {@link CommonBiomeRegistry}.
     */
    public CommonBiomeRegistry()
    {
        this.registry = new WeakRegistry<T, Biome>();
        this.registry.setCaseSensitiveKeys(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Biome> getBiome(String name)
    {
        return this.registry.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Biome> getBiome(T biome)
    {
        return this.registry.get(biome);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBiome(String name, T object, Biome biome)
    {
        this.registry.register(name, object, biome);
        if (name.equalsIgnoreCase("plains"))
        {
            this.defaultBiome = biome;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Biome> getBiomes()
    {
        return this.registry.getRegisteredValues();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Biome getDefaultBiome()
    {
        return this.defaultBiome;
    }

}
