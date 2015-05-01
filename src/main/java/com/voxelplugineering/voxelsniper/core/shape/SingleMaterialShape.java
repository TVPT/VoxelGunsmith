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
package com.voxelplugineering.voxelsniper.core.shape;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.core.util.math.Vector3i;

/**
 * Represents a single material applied to an entire shape.
 */
public class SingleMaterialShape implements MaterialShape
{

    private Shape shape;
    private Material material;

    /**
     * Creates a new {@link SingleMaterialShape}.
     * 
     * @param shape The shape
     * @param material The material
     */
    public SingleMaterialShape(Shape shape, Material material)
    {
        this.shape = checkNotNull(shape);
        this.material = checkNotNull(material);
    }

    @Override
    public int getWidth()
    {
        return this.shape.getWidth();
    }

    @Override
    public int getHeight()
    {
        return this.shape.getHeight();
    }

    @Override
    public int getLength()
    {
        return this.shape.getLength();
    }

    @Override
    public boolean isMutable()
    {
        return true;
    }

    @Override
    public boolean get(int x, int y, int z, boolean relative)
    {
        return this.shape.get(x, y, z, relative);
    }

    @Override
    public Vector3i getOrigin()
    {
        return this.shape.getOrigin();
    }

    @Override
    public void set(int x, int y, int z, boolean relative)
    {
        if (!this.shape.isMutable())
        {
            this.shape = new ComplexShape(this.shape);
        }
        this.shape.set(x, y, z, relative);
    }

    @Override
    public void unset(int x, int y, int z, boolean relative)
    {
        if (!this.shape.isMutable())
        {
            this.shape = new ComplexShape(this.shape);
        }
        this.shape.unset(x, y, z, relative);
    }

    @Override
    public Shape getShape()
    {
        return this.shape;
    }

    @Override
    public Optional<Material> getMaterial(int x, int y, int z, boolean relative)
    {
        if (!get(x, y, z, relative))
        {
            return Optional.absent();
        }
        return Optional.of(this.material);
    }

    @Override
    public void setMaterial(int x, int y, int z, boolean b, Material material)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flood(Material material)
    {
        this.material = material;
    }

    @Override
    public void reset()
    {
        // totally unneeded...
    }

    /**
     * Creates a copy of this {@link SingleMaterialShape}.
     * 
     * @return The copy
     */
    @Override
    public MaterialShape clone()
    {
        return new SingleMaterialShape(this.shape.clone(), this.material);
    }

    @Override
    public Material getDefaultMaterial()
    {
        return this.material;
    }

    @Override
    public byte[] getLowerMaterialData()
    {
        return new byte[this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength()];
    }

    @Override
    public byte[] getUpperMaterialData()
    {
        return null;
    }

    @Override
    public boolean hasExtraData()
    {
        return false;
    }

    @Override
    public Map<Short, Material> getMaterialsDictionary()
    {
        Map<Short, Material> ret = Maps.newHashMap();
        ret.put((short) 0, this.material);
        return ret;
    }

    @Override
    public int getMaxMaterialId()
    {
        return 0;
    }

    @Override
    public void setDefaultMaterial(Material material)
    {
        this.material = material;
    }

}
