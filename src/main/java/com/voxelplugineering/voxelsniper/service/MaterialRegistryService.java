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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.registry.WeakRegistry;

/**
 * A standard material registry for materials.
 * 
 * @param <T> The underlying material type
 */
public class MaterialRegistryService<T> extends AbstractService implements MaterialRegistry<T>
{

    private WeakRegistry<T, Material> registry;
    private Material air;
    private String defaultMaterialName;

    /**
     * Creates a new {@link MaterialRegistryService}.
     */
    public MaterialRegistryService()
    {
        super(5);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "materialRegistry";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init()
    {
        this.registry = new WeakRegistry<T, Material>();
        this.registry.setCaseSensitiveKeys(false);
        this.defaultMaterialName = Gunsmith.getConfiguration().get("defaultMaterialName", String.class).or("air");
        Gunsmith.getLogger().info("Initialized MaterialRegistry service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroy()
    {
        this.registry = null;
        this.defaultMaterialName = null;
        Gunsmith.getLogger().info("Stopped MaterialRegistry service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Material getAirMaterial()
    {
        return this.air;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Material> getMaterial(String name)
    {
        check();
        return this.registry.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Material> getMaterial(T material)
    {
        check();
        return this.registry.get(material);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerMaterial(String name, T object, Material material)
    {
        check();
        this.registry.register(name, object, material);
        if (name.equalsIgnoreCase(this.defaultMaterialName))
        {
            this.air = material;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Material> getMaterials()
    {
        check();
        Set<Map.Entry<T, Material>> entries = this.registry.getRegisteredValues();
        List<Material> mats = Lists.newArrayList();
        for (Map.Entry<T, Material> entry : entries)
        {
            mats.add(entry.getValue());
        }
        return mats;
    }

}
