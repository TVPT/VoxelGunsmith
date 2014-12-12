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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import com.voxelplugineering.voxelsniper.common.CommonVector;

/**
 * Represent a 3-d voxel shape.
 */
public class Shape
{

    /**
     * The shape. Dimensions are [x][z][y/8].
     */
    private byte[][][] shape;
    /**
     * The origin location of the shape.
     */
    private CommonVector origin;
    /**
     * The width of the shape (x-axis).
     */
    private int width;
    /**
     * The height of the shape (y-axis).
     */
    private int height;
    /**
     * The length of the shape (z-axis).
     */
    private int length;

    /**
     * Creates a new shape. The shape is initially all unset. The origin is set to (0, 0, 0).
     * 
     * @param width the width
     * @param height the height
     * @param length the length
     */
    public Shape(int width, int height, int length)
    {
        this(width, height, length, 0, 0, 0);
    }

    /**
     * Creates a new shape. The shape is initially all unset.
     * 
     * @param width the width
     * @param height the height
     * @param length the length
     * @param ox the origin x position
     * @param oy the origin y position
     * @param oz the origin z position
     */
    public Shape(int width, int height, int length, int ox, int oy, int oz)
    {
        this.shape = new byte[width][length][height / 8 + 1];
        this.width = width;
        this.height = height;
        this.length = length;
        this.origin = new CommonVector(ox, oy, oz);
    }

    /**
     * Creates a new shape. The shape is initially all unset.
     * 
     * @param width the width
     * @param height the height
     * @param length the length
     * @param origin the origin
     */
    public Shape(int width, int height, int length, CommonVector origin)
    {
        this.shape = new byte[width][length][height / 8 + 1];
        this.width = width;
        this.height = height;
        this.length = length;
        this.origin = origin;
    }

    /**
     * Returns the width of this shape (x-axis size).
     * 
     * @return the width
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Returns the height of this shape (y-axis size).
     * 
     * @return the height
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Returns the length of this shape (z-axis size).
     * 
     * @return the length
     */
    public int getLength()
    {
        return this.length;
    }

    /**
     * Sets the origin of this shape.
     * 
     * @param origin the new origin
     */
    public void setOrigin(CommonVector origin)
    {
        checkNotNull(origin, "Origin cannot be null");
        this.origin = origin;
    }

    /**
     * Returns the origin of this shape.
     * 
     * @return the origin
     */
    public CommonVector getOrigin()
    {
        return this.origin;
    }

    /**
     * Sets the given position in the shape. This position is relative to the lowest corner of the shape.
     * 
     * @param x the x position to set
     * @param y the y position to set
     * @param z the z position to set
     * @param relative if the position being retrieved should be offset of the origin of this shape
     */
    public void set(int x, int y, int z, boolean relative)
    {
        if (relative)
        {
            x += this.origin.getX();
            y += this.origin.getY();
            z += this.origin.getZ();
        }
        if (x >= this.width || x < 0 || y >= this.height || y < 0 || z >= this.length || z < 0)
        {
            throw new ArrayIndexOutOfBoundsException("Tried to set point outside of the shape. (" + x + ", " + y + ", " + z + ")");
        }
        this.shape[x][z][y / 8] = (byte) (this.shape[x][z][y / 8] | (byte) (1 << (y % 8)));
    }

    /**
     * Unsets the given position in the shape. This position is relative to the lowest corner of the shape.
     * 
     * @param x the x position to unset
     * @param y the y position to unset
     * @param z the z position to unset
     * @param relative if the position being retrieved should be offset of the origin of this shape
     */
    public void unset(int x, int y, int z, boolean relative)
    {
        if (relative)
        {
            x += this.origin.getX();
            y += this.origin.getY();
            z += this.origin.getZ();
        }
        if (x >= this.width || x < 0 || y >= this.height || y < 0 || z >= this.length || z < 0)
        {
            throw new ArrayIndexOutOfBoundsException("Tried to set point outside of the shape. (" + x + ", " + y + ", " + z + ")");
        }
        this.shape[x][z][y / 8] = (byte) (this.shape[x][z][y / 8] & (byte) ~(1 << (y % 8)));
    }

    /**
     * Returns the state of a position in the shape.
     * 
     * @param x the x position to return
     * @param y the y position to return
     * @param z the z position to return
     * @param relative if the position being retrieved should be offset of the origin of this shape
     * @return the state of the position
     */
    public boolean get(int x, int y, int z, boolean relative)
    {
        if (relative)
        {
            x += this.origin.getX();
            y += this.origin.getY();
            z += this.origin.getZ();
        }
        if (x >= this.width || x < 0 || y >= this.height || y < 0 || z >= this.length || z < 0)
        {
            throw new ArrayIndexOutOfBoundsException("Tried to set point outside of the shape. (" + x + ", " + y + ", " + z + ")");
        }
        return ((this.shape[x][z][y / 8] >> y % 8) & 1) == 1;
    }

    /**
     * Resizes the shape relative to the current size. TODO: just realized this needs rework
     * 
     * @param dx the x difference to apply
     * @param dy the y difference to apply
     * @param dz the z difference to apply
     */
    public void resizeRelative(int dx, int dy, int dz)
    {
        resize(this.width + Math.abs(dx), this.height + Math.abs(dy), this.length + Math.abs(dz));
        shift(dx < 0 ? -dx : 0, dy < 0 ? -dy : 0, dz < 0 ? -dz : 0);
    }

    /**
     * Changes the size to the given values. Attempts to retain as much of the shape as possible.
     * 
     * @param w the new width
     * @param h the new height
     * @param l the new length
     */
    public void resize(int w, int h, int l)
    {
        byte[][][] newShape = new byte[w][l][h / 8 + 1];
        for (int x = 0; x < w && x < this.width; x++)
        {
            for (int z = 0; z < l && z < this.length; z++)
            {
                for (int y = 0; y < h && y < this.height; y++)
                {
                    newShape[x][z][y / 8] = (byte) (newShape[x][z][y / 8] | (this.shape[x][z][y / 8] & (1 << (y % 8))));
                }
            }
        }
        this.shape = newShape;
        this.width = w;
        this.height = h;
        this.length = l;
    }

    /**
     * Shifts the shape relative to the origin. (Does not shift the origin but rather shifts the shape within the bounds).
     * 
     * @param dx the x shift
     * @param dy the y shift
     * @param dz the z shift
     */
    public void shift(int dx, int dy, int dz)
    {
        if (dx == 0 && dy == 0 && dz == 0)
        {
            return;
        }
        byte[][][] newShape = new byte[this.width][this.length][this.height / 8 + 1];
        for (int x = 0; x < this.width; x++)
        {
            int nx = x + dx;
            if (nx < 0 || nx >= this.width)
            {
                continue;
            }
            for (int z = 0; z < this.length; z++)
            {
                int nz = z + dz;
                if (nz < 0 || nz >= this.length)
                {
                    continue;
                }
                for (int y = 0; y < this.height; y++)
                {
                    int ny = y + dy;
                    if (ny < 0 || ny >= this.height)
                    {
                        continue;
                    }
                    int i = this.shape[x][z][y / 8] >> (y % 8) & 1;
                    if (i == 1)
                    {
                        newShape[nx][nz][ny / 8] = (byte) (newShape[nx][nz][ny / 8] | (1 << (ny % 8)));
                    } else
                    {
                        newShape[nx][nz][ny / 8] = (byte) (newShape[nx][nz][ny / 8] & ~(1 << (ny % 8)));
                    }
                }
            }
        }
        this.origin = new CommonVector(this.origin.getX() + dx, this.origin.getY() + dy, this.origin.getZ() + dz);
    }

    /**
     * Matches the sizes between this shape and the given shape.
     * 
     * @param other the shape to match with
     */
    protected void matchSize(Shape other)
    {
        checkNotNull(other, "Cannot match size with a null shape.");
        int dx = (int) (other.origin.getX() - this.origin.getX());
        int dy = (int) (other.origin.getY() - this.origin.getY());
        int dz = (int) (other.origin.getZ() - this.origin.getZ());
        other.resizeRelative(dx, dy, dz);
        this.resizeRelative(-dx, -dy, -dz);
    }

    /**
     * Performs a CSG add operation between this shape and the given shape. First ensures that sizes and origins are matched between the two shapes.
     * 
     * @param s the shape to add
     */
    public void add(Shape s)
    {
        checkNotNull(s, "Cannot operate with a null shape.");
        matchSize(s);
        for (int x = 0; x < this.width; x++)
        {
            for (int z = 0; z < this.length; z++)
            {
                for (int y = 0; y < this.height / 8 + 1; y++)
                {
                    this.shape[x][z][y] = (byte) (this.shape[x][z][y] | s.shape[x][z][y]);
                }
            }
        }
    }

    /**
     * Performs a CSG subtract operation between this shape and the given shape. First ensures that sizes and origins are matched between the two
     * shapes.
     * 
     * @param s the shape to subtract from this shape
     */
    public void subtract(Shape s)
    {
        checkNotNull(s, "Cannot operate with a null shape.");
        matchSize(s);
        for (int x = 0; x < this.width; x++)
        {
            for (int z = 0; z < this.length; z++)
            {
                for (int y = 0; y < this.height / 8 + 1; y++)
                {
                    this.shape[x][z][y] = (byte) (this.shape[x][z][y] | ~s.shape[x][z][y]);
                }
            }
        }
    }

    /**
     * Performs a CSG intersection operation between this shape and the given shape. First ensures that sizes and origins are matched between the two
     * shapes.
     * 
     * @param s the shape to intersect with
     */
    public void intersect(Shape s)
    {
        checkNotNull(s, "Cannot operate with a null shape.");
        matchSize(s);
        for (int x = 0; x < this.width; x++)
        {
            for (int z = 0; z < this.length; z++)
            {
                for (int y = 0; y < this.height / 8 + 1; y++)
                {
                    this.shape[x][z][y] = (byte) (this.shape[x][z][y] & s.shape[x][z][y]);
                }
            }
        }
    }

    /**
     * Performs a CSG xor operation between this shape and the given shape. First ensures that sizes and origins are matched between the two shapes.
     * 
     * @param s the shape to xor against
     */
    public void xor(Shape s)
    {
        checkNotNull(s, "Cannot operate with a null shape.");
        matchSize(s);
        for (int x = 0; x < this.width; x++)
        {
            for (int z = 0; z < this.length; z++)
            {
                for (int y = 0; y < this.height / 8 + 1; y++)
                {
                    this.shape[x][z][y] = (byte) ((this.shape[x][z][y] & ~s.shape[x][z][y]) | (~this.shape[x][z][y] & s.shape[x][z][y]));
                }
            }
        }
    }

    /**
     * Inverts the values of this shape.
     */
    public void invert()
    {
        for (int x = 0; x < this.width; x++)
        {
            for (int z = 0; z < this.length; z++)
            {
                for (int y = 0; y < this.height / 8 + 1; y++)
                {
                    this.shape[x][z][y] = (byte) (~this.shape[x][z][y]);
                }
            }
        }
    }

    /**
     * Returns an array of {@link CommonVector} these vectors represent all set positions of this shape.
     * 
     * @return the set positions
     */
    public CommonVector[] getShape()
    {
        List<CommonVector> points = new ArrayList<CommonVector>();
        for (int x = 0; x < this.width; x++)
        {
            for (int y = 0; y < this.height; y++)
            {
                for (int z = 0; z < this.length; z++)
                {
                    if (get(x, y, z, false))
                    {
                        points.add(new CommonVector(x - this.origin.getX(), y - this.origin.getY(), z - this.origin.getZ()));
                    }
                }
            }
        }
        return points.toArray(new CommonVector[points.size()]);
    }

    /**
     * Flattens this shape. The resultant shape has a height of 1 with the position at each x,z position set if any block in a column above that point
     * was set. As a side effect the origin is set to have a y value of 0.
     */
    public void flatten()
    {
        for (int x = 0; x < this.width; x++)
        {
            for (int z = 0; z < this.length; z++)
            {
                for (int y = 0; y < this.height; y++)
                {
                    if (get(x, y, z, false))
                    {
                        set(x, 0, z, false);
                        break;
                    }
                }
            }
        }
        resize(this.width, 1, this.length);
        setOrigin(new CommonVector(this.origin.getX(), 0, this.origin.getZ()));
    }

    /**
     * Creates a new Shape identical to this one.
     * 
     * @return the duplicate shape
     */
    @Override
    public Shape clone()
    {
        Shape newShape = new Shape(this.width, this.height, this.length, this.origin);
        for (int x = 0; x < this.width; x++)
        {
            for (int y = 0; y < this.height; y++)
            {
                for (int z = 0; z < this.length; z++)
                {
                    if (get(x, y, z, false))
                    {
                        newShape.set(x, y, z, false);
                    }
                }
            }
        }
        return newShape;
    }
}
