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
package com.voxelplugineering.voxelsniper.shape;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Represents a single material applied to an entire shape.
 */
public class SingleMaterialShape implements MaterialShape
{

    private final Shape shape;
    private final Material material;

    /**
     * Creates a new {@link SingleMaterialShape}.
     * 
     * @param shape The shape
     * @param material The material
     */
    public SingleMaterialShape(Shape shape, Material material)
    {
        this.shape = shape;
        this.material = material;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth()
    {
        return this.shape.getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight()
    {
        return this.shape.getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength()
    {
        return this.shape.getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean get(int x, int y, int z, boolean relative)
    {
        return this.shape.get(x, y, z, relative);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3i getOrigin()
    {
        return this.shape.getOrigin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(int x, int y, int z, boolean relative)
    {
        this.shape.set(x, y, z, relative);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unset(int x, int y, int z, boolean relative)
    {
        this.shape.unset(x, y, z, relative);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape getShape()
    {
        return this.shape;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Material> getMaterial(int x, int y, int z, boolean relative)
    {
        return Optional.of(this.material);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaterial(int x, int y, int z, boolean b, Material material)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flood(Material material)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset()
    {
        //totally unneeded...
    }

    /**
     * Creates a copy of this {@link SingleMaterialShape}.
     * 
     * @return The copy
     */
    public MaterialShape clone()
    {
        return new SingleMaterialShape(this.shape.clone(), this.material);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVolume()
    {
        return this.shape.getVolume();
    }

}