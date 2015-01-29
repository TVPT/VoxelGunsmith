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

import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.Gunsmith;
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
    private Biome defaultBiome = null;
    private String defaultBiomeName = Gunsmith.getConfiguration().get("defaultBiomeName", String.class).or("plains");

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
        if (name.equalsIgnoreCase(this.defaultBiomeName))
        {
            this.defaultBiome = biome;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Biome[] getBiomes()
    {
        Set<Map.Entry<T, Biome>> biomeSet = this.registry.getRegisteredValues();
        Biome[] biomes = new Biome[biomeSet.size()];
        int i = 0;
        for (Map.Entry<T, Biome> e : biomeSet)
        {
            biomes[i++] = e.getValue();
        }
        return biomes;
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
