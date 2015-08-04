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
package com.voxelplugineering.voxelsniper.shape.csg;

import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Represents an Ellipsoidal primitive shape.
 */
public class EllipsoidShape extends OffsetShape
{

    private double rx;
    private double ry;
    private double rz;

    /**
     * Creates a new {@link EllipsoidShape}.
     * 
     * @param rx The X radius
     * @param ry The Y radius
     * @param rz The Z radius
     * @param origin The origin
     */
    public EllipsoidShape(double rx, double ry, double rz, Vector3i origin)
    {
        super(origin);
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    @Override
    public boolean get(int x, int y, int z, boolean relative)
    {
        if (!relative)
        {
            x -= getOrigin().getX();
            y -= getOrigin().getY();
            z -= getOrigin().getZ();
        }
        return (x / this.rx) * (x / this.rx) + (y / this.ry) * (y / this.ry) + (z / this.rz) * (z / this.rz) <= 1;
    }

    @Override
    public int getWidth()
    {
        return (int) (this.rx * 2 + 1);
    }

    @Override
    public int getHeight()
    {
        return (int) (this.ry * 2 + 1);
    }

    @Override
    public int getLength()
    {
        return (int) (this.rz * 2 + 1);
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
    public EllipsoidShape clone()
    {
        return new EllipsoidShape(this.rx, this.ry, this.rz, getOrigin());
    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    @Override
    public void fillFrom(Shape shape)
    {
        throw new UnsupportedOperationException();
    }

}
