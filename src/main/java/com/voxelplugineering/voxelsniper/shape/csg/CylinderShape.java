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
import com.voxelplugineering.voxelsniper.util.Direction;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Represents a Cylindrical primitive shape.
 * 
 * <p>The axis code in this is half arsed at best and will not support anything other than the
 * cardinal axes TODO improve.</p>
 */
public class CylinderShape extends OffsetShape
{

    private double rx;
    private int height;
    private double rz;
    private final Direction axis;

    /**
     * Creates a new {@link CylinderShape}.
     * 
     * @param rx The X radius
     * @param height The height
     * @param rz The Z radius
     * @param origin The origin
     */
    public CylinderShape(double rx, int height, double rz, Vector3i origin)
    {
        super(origin);
        this.rx = rx;
        this.height = height;
        this.rz = rz;
        this.axis = Direction.UP;
    }

    /**
     * Creates a new {@link CylinderShape}.
     * 
     * @param rx The X radius
     * @param height The height
     * @param rz The Z radius
     * @param origin The origin
     * @param axis The axis to orient the cylinder around
     */
    public CylinderShape(double rx, int height, double rz, Vector3i origin, Direction axis)
    {
        super(origin);
        this.rx = rx;
        this.height = height;
        this.rz = rz;
        this.axis = axis;
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
        if (this.axis.getModY() == 1)
        {
            return (x / this.rx) * (x / this.rx) + (z / this.rz) * (z / this.rz) <= 1 && y < this.height && y >= 0;
        }
        if (this.axis.getModX() == 1)
        {
            return (y / this.rx) * (y / this.rx) + (z / this.rz) * (z / this.rz) <= 1 && x < this.height && x >= 0;
        }
        if (this.axis.getModZ() == 1)
        {
            return (x / this.rx) * (x / this.rx) + (y / this.rz) * (y / this.rz) <= 1 && z < this.height && z >= 0;
        }
        return false;
    }

    @Override
    public int getWidth()
    {
        if (this.axis.getModY() == 1 || this.axis.getModZ() == 1)
        {
            return (int) (this.rx * 2 + 1);
        }
        if (this.axis.getModX() == 1)
        {
            return this.height;
        }
        return (int) (this.rx * 2 + 1);
    }

    @Override
    public int getHeight()
    {
        if (this.axis.getModX() == 1)
        {
            return (int) (this.rx * 2 + 1);
        }
        if (this.axis.getModZ() == 1)
        {
            return (int) (this.rz * 2 + 1);
        }
        return this.height;
    }

    @Override
    public int getLength()
    {
        if (this.axis.getModY() == 1 || this.axis.getModX() == 1)
        {
            return (int) (this.rz * 2 + 1);
        }
        if (this.axis.getModZ() == 1)
        {
            return this.height;
        }
        return (int) (this.rz * 2 + 1);
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
    public CylinderShape clone()
    {
        return new CylinderShape(this.rx, this.height, this.rz, getOrigin());
    }

    /**
     * Returns a string representation of this shape.
     * 
     * @return The string
     */
    @Override
    public String toString()
    {
        return String.format("CylinderShape: ( rx=%.2f, height=%d, rz=%.2f, origin=%s )", this.rx, this.height, this.rz, this.getOrigin().toString());
    }

    @Override
    public void fillFrom(Shape shape)
    {
        throw new UnsupportedOperationException();
    }

}
