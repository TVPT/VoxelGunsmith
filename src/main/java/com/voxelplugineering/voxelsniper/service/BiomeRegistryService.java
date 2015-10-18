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
package com.voxelplugineering.voxelsniper.service;

import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.registry.WeakRegistry;
import com.voxelplugineering.voxelsniper.service.registry.BiomeRegistry;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.world.biome.Biome;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A common biome registry for biomes.
 * 
 * @param <T> The biome type
 */
public class BiomeRegistryService<T> extends AbstractService implements BiomeRegistry<T>
{

    private WeakRegistry<T, Biome> registry;
    private Biome defaultBiome;
    private String defaultBiomeName;

    /**
     * Creates a new {@link BiomeRegistryService}.
     * 
     * @param context The context
     */
    public BiomeRegistryService(Context context)
    {
        super(context);
    }

    @Override
    protected void _init()
    {
        this.registry = new WeakRegistry<T, Biome>();
        this.registry.setCaseSensitiveKeys(false);
        this.defaultBiomeName = BaseConfiguration.defaultBiomeName;
    }

    @Override
    protected void _shutdown()
    {
        this.registry = null;
        this.defaultBiomeName = null;
    }

    @Override
    public Optional<Biome> getBiome(String name)
    {
        check("getBiome");
        return this.registry.get(name);
    }

    @Override
    public Optional<Biome> getBiome(T biome)
    {
        check("getBiome");
        return this.registry.get(biome);
    }

    @Override
    public void registerBiome(String name, T object, Biome biome)
    {
        check("registerBiome");
        this.registry.register(name, object, biome);
        if (name.equalsIgnoreCase(this.defaultBiomeName))
        {
            this.defaultBiome = biome;
        }
    }

    @Override
    public Iterable<Biome> getBiomes()
    {
        check("getBiomes");
        List<Biome> biomes = Lists.newArrayList();
        for (Map.Entry<T, Biome> e : this.registry.getRegisteredValues())
        {
            biomes.add(e.getValue());
        }
        return biomes;
    }

    @Override
    public Biome getDefaultBiome()
    {
        check("getDefaultBiome");
        return this.defaultBiome;
    }

}
