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

import com.voxelplugineering.voxelsniper.common.CommonVector;

public class Shape
{

    private byte[][][] shape;
    private CommonVector origin;
    private int width;
    private int height;
    private int length;

    public Shape(int width, int height, int length)
    {
        this(width, height, length, 0, 0, 0);
    }

    public Shape(int width, int height, int length, int ox, int oy, int oz)
    {
        this.shape = new byte[width][length][height / 8 + 1];
        this.width = width;
        this.height = height;
        this.length = length;
        this.origin = new CommonVector(ox, oy, oz);
    }

    public void setOrigin(CommonVector v)
    {
        this.origin = v;
    }

    public CommonVector getOrigin()
    {
        return this.origin;
    }

    public void set(int x, int y, int z)
    {
        if (x >= width || x < 0 || y >= height || y < 0 || z >= length || z < 0)
        {
            throw new ArrayIndexOutOfBoundsException("Tried to set point outside of the shape. (" + x + ", " + y + ", " + z + ")");
        }
        this.shape[x][z][y / 8] = (byte) (this.shape[x][z][y / 8] | (byte) (1 << (y % 8)));
    }

    public void unset(int x, int y, int z)
    {
        if (x >= width || x < 0 || y >= height || y < 0 || z >= length || z < 0)
        {
            throw new ArrayIndexOutOfBoundsException("Tried to set point outside of the shape. (" + x + ", " + y + ", " + z + ")");
        }
        this.shape[x][z][y / 8] = (byte) (this.shape[x][z][y / 8] & (byte) ~(1 << (y % 8)));
    }

    public boolean get(int x, int y, int z)
    {
        if (x >= width || x < 0 || y >= height || y < 0 || z >= length || z < 0)
        {
            throw new ArrayIndexOutOfBoundsException("Tried to set point outside of the shape. (" + x + ", " + y + ", " + z + ")");
        }
        return ((this.shape[x][z][y / 8] >> y % 8) & 1) == 1;
    }

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

    public void add(Shape s)
    {
        if (width != s.width || height != s.height || length != s.length)
        {
            resize(Math.max(s.width, width), Math.max(s.height, height), Math.max(s.length, length));
            s.resize(Math.max(s.width, width), Math.max(s.height, height), Math.max(s.length, length));
        }
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

    public void subtract(Shape s)
    {
        if (width != s.width || height != s.height || length != s.length)
        {
            resize(Math.max(s.width, width), Math.max(s.height, height), Math.max(s.length, length));
            s.resize(Math.max(s.width, width), Math.max(s.height, height), Math.max(s.length, length));
        }
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

    public void intersect(Shape s)
    {
        if (width != s.width || height != s.height || length != s.length)
        {
            resize(Math.max(s.width, width), Math.max(s.height, height), Math.max(s.length, length));
            s.resize(Math.max(s.width, width), Math.max(s.height, height), Math.max(s.length, length));
        }
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

    public void xor(Shape s)
    {
        if (width != s.width || height != s.height || length != s.length)
        {
            resize(Math.max(s.width, width), Math.max(s.height, height), Math.max(s.length, length));
            s.resize(Math.max(s.width, width), Math.max(s.height, height), Math.max(s.length, length));
        }
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

}
