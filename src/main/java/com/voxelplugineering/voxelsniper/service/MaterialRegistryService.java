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
import com.voxelplugineering.voxelsniper.service.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.world.material.Material;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
     * 
     * @param context The context
     */
    public MaterialRegistryService(Context context)
    {
        super(context);
    }

    @Override
    protected void _init()
    {
        this.registry = new WeakRegistry<T, Material>();
        this.registry.setCaseSensitiveKeys(false);
        this.defaultMaterialName = BaseConfiguration.defaultMaterialName;
    }

    @Override
    protected void _shutdown()
    {
        this.registry = null;
        this.defaultMaterialName = null;
    }

    @Override
    public Material getAirMaterial()
    {
        return this.air;
    }

    @Override
    public Optional<Material> getMaterial(String name)
    {
        check("getMaterial");
        return this.registry.get(name);
    }

    @Override
    public Optional<Material> getMaterial(T material)
    {
        check("getMaterial");
        return this.registry.get(material);
    }

    @Override
    public void registerMaterial(String name, T object, Material material)
    {
        check("registerMaterial");
        this.registry.register(name, object, material);
        if (name.equalsIgnoreCase(this.defaultMaterialName))
        {
            this.air = material;
        }
    }

    @Override
    public Collection<Material> getMaterials()
    {
        check("getMaterials");
        return Lists.newArrayList(this.registry.values());
    }

}
