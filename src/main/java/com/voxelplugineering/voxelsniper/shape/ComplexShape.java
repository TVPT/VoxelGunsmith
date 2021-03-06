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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.util.math.Vector3i;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a 3-d voxel shape.
 */
public class ComplexShape implements Shape
{

    /**
     * The shape. Dimensions are [x][z][y/8].
     */
    private byte[][][] shape;

    private Vector3i origin;
    private int width;
    private int height;
    private int length;

    /**
     * Creates a new shape. The shape is initially all unset. The origin is set to (0, 0, 0).
     * 
     * @param width The width
     * @param height The height
     * @param length The length
     */
    public ComplexShape(int width, int height, int length)
    {
        this(width, height, length, 0, 0, 0);
    }

    /**
     * Creates a new shape. The shape is initially all unset.
     * 
     * @param width The width
     * @param height The height
     * @param length The length
     * @param ox The origin x position
     * @param oy The origin y position
     * @param oz The origin z position
     */
    public ComplexShape(int width, int height, int length, int ox, int oy, int oz)
    {
        this.shape = new byte[width][length][height / 8 + 1];
        this.width = width;
        this.height = height;
        this.length = length;
        this.origin = new Vector3i(ox, oy, oz);
    }

    /**
     * Creates a new shape. The shape is initially all unset.
     * 
     * @param width The width
     * @param height The height
     * @param length The length
     * @param origin The origin
     */
    public ComplexShape(int width, int height, int length, Vector3i origin)
    {
        this.shape = new byte[width][length][height / 8 + 1];
        this.width = width;
        this.height = height;
        this.length = length;
        this.origin = origin;
    }

    /**
     * Creates a new {@link ComplexShape} initialized with the given shape.
     * 
     * @param shape The shape to mirror
     */
    public ComplexShape(Shape shape)
    {
        this.shape = new byte[shape.getWidth()][shape.getLength()][shape.getHeight() / 8 + 1];
        this.width = shape.getWidth();
        this.height = shape.getHeight();
        this.length = shape.getLength();
        this.origin = shape.getOrigin();
        for (int x = 0; x < shape.getWidth(); x++)
        {
            for (int y = 0; y < shape.getHeight(); y++)
            {
                for (int z = 0; z < shape.getLength(); z++)
                {
                    if (shape.get(x, y, z, false))
                    {
                        set(x, y, z, false);
                    }
                }
            }
        }
    }

    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getHeight()
    {
        return this.height;
    }

    @Override
    public int getLength()
    {
        return this.length;
    }

    @Override
    public boolean isMutable()
    {
        return true;
    }

    /**
     * Sets the origin of this shape.
     * 
     * @param origin The new origin
     */
    public void setOrigin(Vector3i origin)
    {
        this.origin = checkNotNull(origin, "Origin cannot be null");
    }

    @Override
    public Vector3i getOrigin()
    {
        return this.origin;
    }

    @Override
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

    @Override
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

    @Override
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
            throw new ArrayIndexOutOfBoundsException("Tried to get point outside of the shape. (" + x + ", " + y + ", " + z + ")");
        }
        return ((this.shape[x][z][y / 8] >> y % 8) & 1) == 1;
    }

    /**
     * Resizes the shape relative to the current size.
     * 
     * @param dx The x difference to apply
     * @param dy The y difference to apply
     * @param dz The z difference to apply
     */
    public void grow(int dx, int dy, int dz)
    {
        resize(this.width + Math.abs(dx), this.height + Math.abs(dy), this.length + Math.abs(dz), this.origin.getX() + (dx < 0 ? -dx : 0),
                this.origin.getY() + (dy < 0 ? -dy : 0), this.origin.getZ() + (dz < 0 ? -dz : 0));
    }

    /**
     * Resizes the shape relative to the current size.
     * 
     * @param dx The x difference to apply
     * @param dy The y difference to apply
     * @param dz The z difference to apply
     */
    public void shrink(int dx, int dy, int dz)
    {
        resize(this.width - Math.abs(dx), this.height - Math.abs(dy), this.length - Math.abs(dz), this.origin.getX() - (dx < 0 ? -dx : 0),
                this.origin.getY() - (dy < 0 ? -dy : 0), this.origin.getZ() - (dz < 0 ? -dz : 0));
    }

    /**
     * Resizes the lower edge of the shape.
     * 
     * @param fndx The x difference to apply
     * @param fndy The y difference to apply
     * @param fndz The z difference to apply
     */
    public void resizeNegative(int fndx, int fndy, int fndz)
    {
        grow(fndx > 0 ? -fndx : 0, fndy > 0 ? -fndy : 0, fndz > 0 ? -fndz : 0);
        shrink(fndx < 0 ? fndx : 0, fndy < 0 ? fndy : 0, fndz < 0 ? fndz : 0);
    }

    /**
     * Resizes the upper edge of the shape.
     * 
     * @param fdx The x difference to apply
     * @param fdy The y difference to apply
     * @param fdz The z difference to apply
     */
    public void resizePositive(int fdx, int fdy, int fdz)
    {
        grow(fdx > 0 ? fdx : 0, fdy > 0 ? fdy : 0, fdz > 0 ? fdz : 0);
        shrink(fdx < 0 ? -fdx : 0, fdy < 0 ? -fdy : 0, fdz < 0 ? -fdz : 0);
    }

    /**
     * Changes the size to the given values. Attempts to retain as much of the shape as possible.
     * 
     * @param w The new width
     * @param h The new height
     * @param l The new length
     */
    private void resize(int w, int h, int l, int ox, int oy, int oz)
    {
        byte[][][] newShape = new byte[w][l][h / 8 + 1];
        for (int x = 0; x < w && x < this.width; x++)
        {
            int xx = x + ox - this.origin.getX();
            for (int z = 0; z < l && z < this.length; z++)
            {
                int zz = z + oz - this.origin.getZ();
                for (int y = 0; y < h && y < this.height; y++)
                {
                    int yy = y + oy - this.origin.getY();
                    if (xx < this.width && xx >= 0 && yy < this.height && yy >= 0 && zz < this.length && zz >= 0)
                    {
                        newShape[x][z][y
                                / 8] = (byte) (newShape[x][z][y / 8] | ((this.shape[xx][zz][yy / 8] & (1 << (yy % 8))) >> (yy % 8) << (y % 8)));
                    }
                }
            }
        }

        this.shape = newShape;
        this.width = w;
        this.height = h;
        this.length = l;
        if (ox != this.origin.getX() || oy != this.origin.getY() || oz != this.origin.getZ())
        {
            this.origin = new Vector3i(ox, oy, oz);
        }
    }

    /**
     * Matches the sizes between this shape and the given shape.
     * 
     * @param other The shape to match with
     */
    public void matchSize(Shape other)
    {
        checkNotNull(other, "Cannot match size with a null shape.");
        int dx = other.getWidth() - this.getWidth();
        int dy = other.getHeight() - this.getHeight();
        int dz = other.getLength() - this.getLength();
        int odx = other.getOrigin().getX() - this.origin.getX();
        int ody = other.getOrigin().getY() - this.origin.getY();
        int odz = other.getOrigin().getZ() - this.origin.getZ();
        int fdx = dx - odx;
        int fdy = dy - ody;
        int fdz = dz - odz;
        int fndx = dx - fdx;
        int fndy = dy - fdy;
        int fndz = dz - fdz;
        this.resizePositive(fdx, fdy, fdz);
        this.resizeNegative(fndx, fndy, fndz);
    }

    /**
     * Combines the bounding boxes of this shape with the given shape. the result will be a shape
     * whose bounds includes the bounds of both shapes.
     * 
     * @param other The shape to combine with
     */
    public void combineSizes(Shape other)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Performs a CSG add operation between this shape and the given shape. First ensures that sizes
     * and origins are matched between the two shapes.
     * 
     * @param s The shape to add
     */
    public void add(ComplexShape s)
    {
        checkNotNull(s, "Cannot operate with a null shape.");
        combineSizes(s);
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
     * Performs a CSG subtract operation between this shape and the given shape. First ensures that
     * sizes and origins are matched between the two shapes.
     * 
     * @param s The shape to subtract from this shape
     */
    public void subtract(ComplexShape s)
    {
        checkNotNull(s, "Cannot operate with a null shape.");
        combineSizes(s);
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
     * Performs a CSG intersection operation between this shape and the given shape. First ensures
     * that sizes and origins are matched between the two shapes.
     * 
     * @param s The shape to intersect with
     */
    public void intersect(ComplexShape s)
    {
        checkNotNull(s, "Cannot operate with a null shape.");
        combineSizes(s);
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
     * Performs a CSG xor operation between this shape and the given shape. First ensures that sizes
     * and origins are matched between the two shapes.
     * 
     * @param s The shape to xor against
     */
    public void xor(ComplexShape s)
    {
        checkNotNull(s, "Cannot operate with a null shape.");
        combineSizes(s);
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
     * Returns an array of {@link Vector3i} these vectors represent all set positions of this shape.
     * 
     * @return The set positions
     */
    public Vector3i[] getShape()
    {
        List<Vector3i> points = new ArrayList<Vector3i>();
        for (int x = 0; x < this.width; x++)
        {
            for (int y = 0; y < this.height; y++)
            {
                for (int z = 0; z < this.length; z++)
                {
                    if (get(x, y, z, false))
                    {
                        points.add(new Vector3i(x - this.origin.getX(), y - this.origin.getY(), z - this.origin.getZ()));
                    }
                }
            }
        }
        return points.toArray(new Vector3i[points.size()]);
    }

    /**
     * Flattens this shape. The resultant shape has a height of 1 with the position at each x,z
     * position set if any block in a column above that point was set. As a side effect the origin
     * is set to have a y value of 0.
     */
    public void flatten()
    {
        if (this.height < 2)
        {
            return;
        }
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
        resize(this.width, 1, this.length, this.origin.getX(), this.origin.getY(), this.origin.getZ());
        setOrigin(new Vector3i(this.origin.getX(), 0, this.origin.getZ()));
    }

    /**
     * Creates a new Shape identical to this one.
     * 
     * @return The duplicate shape
     */
    @Override
    public ComplexShape clone()
    {
        ComplexShape newShape = new ComplexShape(this.width, this.height, this.length, this.origin);
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

    /**
     * Gets a String representation of this shape's size and origin.
     * 
     * @return The string
     */
    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder("");
        string.append("Shape (").append(getWidth()).append("x").append(getHeight()).append("x").append(getLength()).append(") origin: ")
                .append(this.origin.toString()).append("\n");
        for (int y = 0; y < getHeight(); y++)
        {
            for (int z = 0; z < getLength(); z++)
            {
                for (int x = 0; x < getWidth(); x++)
                {
                    string.append(get(x, y, z, false) ? "1" : "0");
                }
                if (z < getLength() - 1)
                {
                    string.append("\n");
                }
            }
            if (y < getHeight() - 1)
            {
                string.append("\n\n");
            }
        }
        return string.toString();
    }

    @Override
    public void fillFrom(Shape shape)
    {
        checkArgument(shape.getWidth() == getWidth());
        checkArgument(shape.getHeight() == getHeight());
        checkArgument(shape.getLength() == getLength());
        if (shape instanceof ComplexShape)
        {
            byte[][][] other = ((ComplexShape) shape).shape;
            for (int x = 0; x < getWidth(); x++)
            {
                for (int z = 0; z < getLength(); z++)
                {
                    for (int y = 0; y < shape.getHeight() / 8 + 1; y++)
                    {
                        this.shape[x][z][y] = other[x][z][y];
                    }
                }
            }
        } else
        {
            for (int x = 0; x < shape.getWidth(); x++)
            {
                for (int y = 0; y < shape.getHeight(); y++)
                {
                    for (int z = 0; z < shape.getLength(); z++)
                    {
                        if (shape.get(x, y, z, false))
                        {
                            set(x, y, z, false);
                        } else
                        {
                            unset(x, y, z, false);
                        }
                    }
                }
            }
        }
    }
}
