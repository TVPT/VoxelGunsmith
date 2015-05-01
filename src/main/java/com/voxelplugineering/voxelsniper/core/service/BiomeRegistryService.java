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
package com.voxelplugineering.voxelsniper.core.service;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.voxelplugineering.voxelsniper.api.registry.BiomeRegistry;
import com.voxelplugineering.voxelsniper.api.world.biome.Biome;
import com.voxelplugineering.voxelsniper.core.Gunsmith;
import com.voxelplugineering.voxelsniper.core.registry.WeakRegistry;

/**
 * A common biome registry for biomes.
 * 
 * @param <T> The biome type
 */
public class BiomeRegistryService<T> extends AbstractService implements BiomeRegistry<T>
{

    private WeakRegistry<T, Biome> registry;
    private Biome defaultBiome = null;
    private String defaultBiomeName;

    /**
     * Creates a new {@link BiomeRegistryService}.
     */
    public BiomeRegistryService()
    {
        super(BiomeRegistry.class, 12);
    }

    @Override
    public String getName()
    {
        return "biomeRegistry";
    }

    @Override
    protected void init()
    {
        this.registry = new WeakRegistry<T, Biome>();
        this.registry.setCaseSensitiveKeys(false);
        this.defaultBiomeName = Gunsmith.getConfiguration().get("defaultBiomeName", String.class).or("plains");
        Gunsmith.getLogger().info("Initialized BiomeRegistry service");
    }


    @Override
    protected void destroy()
    {
        this.registry = null;
        this.defaultBiomeName = null;
        Gunsmith.getLogger().info("Stopped BiomeRegistry service");
    }

    @Override
    public Optional<Biome> getBiome(String name)
    {
        check();
        return this.registry.get(name);
    }

    @Override
    public Optional<Biome> getBiome(T biome)
    {
        check();
        return this.registry.get(biome);
    }

    @Override
    public void registerBiome(String name, T object, Biome biome)
    {
        check();
        this.registry.register(name, object, biome);
        if (name.equalsIgnoreCase(this.defaultBiomeName))
        {
            this.defaultBiome = biome;
        }
    }

    @Override
    public Iterable<Biome> getBiomes()
    {
        check();
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
        return this.defaultBiome;
    }

}
