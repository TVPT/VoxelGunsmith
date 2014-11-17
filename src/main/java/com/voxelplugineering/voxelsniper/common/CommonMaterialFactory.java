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
package com.voxelplugineering.voxelsniper.common;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.voxelplugineering.voxelsniper.api.IMaterialFactory;

/**
 * A standard factory for creating static copies of {@link CommonMaterial} proxying materials in the underlying implementation.
 * 
 * @param <T> the underlying material type
 */
public class CommonMaterialFactory<T> implements IMaterialFactory<T>
{

    /**
     * The {@link CommonMaterial} instances.
     */
    private Map<String, CommonMaterial<T>> registry;
    /**
     * A special case {@link CommonMaterial} representing air or empty space.
     */
    private CommonMaterial<T> air = null;

    @Override
    public void init()
    {
        this.registry = new HashMap<String, CommonMaterial<T>>();
        this.air = null;
    }

    @Override
    public void stop()
    {

    }

    @Override
    public void restart()
    {
        stop();
        init();
    }

    @Override
    public CommonMaterial<T> getMaterial(String name)
    {
        checkArgument(name.length() != 0, "Name of material cannot be empty");

        return this.registry.get(name.toUpperCase());
    }

    @Override
    public CommonMaterial<T> getAirMaterial()
    {
        return this.air;
    }

    @Override
    public void registerMaterial(String name, CommonMaterial<T> material)
    {
        checkNotNull(material, "Material being registered cannot be null");
        checkArgument(name.length() != 0, "Name of material cannot be empty");
        name = name.toUpperCase();
        this.registry.put(name, material);

        if ("AIR".equalsIgnoreCase(name) || "EMPTY".equalsIgnoreCase(name))
        {
            this.air = material;
        }

    }

}
