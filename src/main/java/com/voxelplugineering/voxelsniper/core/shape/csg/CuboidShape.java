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
package com.voxelplugineering.voxelsniper.core.shape.csg;

import com.voxelplugineering.voxelsniper.core.util.math.Vector3i;

/**
 * Represents a Cuboidal primitive shape.
 */
public class CuboidShape extends OffsetShape
{

    private int w;
    private int h;
    private int l;

    /**
     * Creates a new {@link CuboidShape}.
     * 
     * @param w The width
     * @param h The height
     * @param l The length
     * @param origin The origin
     */
    public CuboidShape(int w, int h, int l, Vector3i origin)
    {
        super(origin);
        this.w = w;
        this.h = h;
        this.l = l;
    }

    @Override
    public boolean get(int x, int y, int z, boolean relative)
    {
        if (relative)
        {
            x -= getOrigin().getX();
            y -= getOrigin().getY();
            z -= getOrigin().getZ();
        }
        return x >= 0 && x < this.w && y >= 0 && y < this.h && z >= 0 && z < this.l;
    }

    @Override
    public int getWidth()
    {
        return this.w;
    }

    @Override
    public int getHeight()
    {
        return this.h;
    }

    @Override
    public int getLength()
    {
        return this.l;
    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    @Override
    public void set(int x, int y, int z, boolean relative)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unset(int x, int y, int z, boolean relative)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public CuboidShape clone()
    {
        return new CuboidShape(this.w, this.h, this.l, getOrigin());
    }

}
