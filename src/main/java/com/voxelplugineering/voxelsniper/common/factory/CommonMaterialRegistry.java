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
package com.voxelplugineering.voxelsniper.common.factory;

import com.voxelplugineering.voxelsniper.api.IMaterialRegistry;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;

/**
 * A standard factory for creating static copies of {@link CommonMaterial} proxying materials in the underlying implementation.
 * 
 * @param <T> the underlying material type
 */
public class CommonMaterialRegistry<T> extends WeakRegistry<T, CommonMaterial<T>> implements IMaterialRegistry<T>
{

    /**
     * A special case {@link CommonMaterial} representing air or empty space.
     */
    private CommonMaterial<T> air = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public CommonMaterial<T> getAirMaterial()
    {
        return this.air;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(String name, T key, CommonMaterial<T> value)
    {
        super.register(name, key, value);

        if ("AIR".equalsIgnoreCase(name) || "EMPTY".equalsIgnoreCase(name))
        {
            this.air = value;
        }

    }

}
