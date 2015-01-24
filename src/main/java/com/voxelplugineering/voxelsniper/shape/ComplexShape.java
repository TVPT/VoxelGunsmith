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

import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Represent a 3-d voxel shape.
 */
public class ComplexShape implements Shape
{

    /**
     * The shape. Dimensions are [x][z][y/8].
     */
    private byte[][][] shape;
    /**
     * The origin location of the shape.
     */
    private Vector3i origin;
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
     * Creates a new shape. The shape is initially all unset. The origin is set
     * to (0, 0, 0).
     * 
     * @param width the width
     * @param height the height
     * @param length the length
     */
    public ComplexShape(int width, int height, int length)
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
     * @param width the width
     * @param height the height
     * @param length the length
     * @param origin the origin
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

    /**
     * Returns the width of this shape (x-axis size). Note that this is not the
     * width of the region set, but rather the width of the total possible
     * volume.
     * 
     * @return the width
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Returns the height of this shape (y-axis size). Note that this is not the
     * height of the region set, but rather the height of the total possible
     * volume.
     * 
     * @return the height
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Returns the length of this shape (z-axis size). Note that this is not the
     * length of the region set, but rather the length of the total possible
     * volume.
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
    public void setOrigin(Vector3i origin)
    {
        checkNotNull(origin, "Origin cannot be null");
        this.origin = origin;
    }

    /**
     * Returns the origin of this shape.
     * 
     * @return the origin
     */
    public Vector3i getOrigin()
    {
        return this.origin;
    }

    /**
     * Sets the given position in the shape. This position is relative to the
     * lowest corner of the shape.
     * 
     * @param x the x position to set
     * @param y the y position to set
     * @param z the z position to set
     * @param relative if the position being retrieved should be offset of the
     *            origin of this shape
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
     * Unsets the given position in the shape. This position is relative to the
     * lowest corner of the shape.
     * 
     * @param x the x position to unset
     * @param y the y position to unset
     * @param z the z position to unset
     * @param relative if the position being retrieved should be offset of the
     *            origin of this shape
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
     * @param relative if the position being retrieved should be offset of the
     *            origin of this shape
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
            throw new ArrayIndexOutOfBoundsException("Tried to get point outside of the shape. (" + x + ", " + y + ", " + z + ")");
        }
        return ((this.shape[x][z][y / 8] >> y % 8) & 1) == 1;
    }

    /**
     * Resizes the shape relative to the current size.
     * 
     * @param dx the x difference to apply
     * @param dy the y difference to apply
     * @param dz the z difference to apply
     */
    public void grow(int dx, int dy, int dz)
    {
        resize(this.width + Math.abs(dx), this.height + Math.abs(dy), this.length + Math.abs(dz), this.origin.getX() + (dx < 0 ? -dx : 0),
                this.origin.getY() + (dy < 0 ? -dy : 0), this.origin.getZ() + (dz < 0 ? -dz : 0));
    }

    /**
     * Resizes the shape relative to the current size.
     * 
     * @param dx the x difference to apply
     * @param dy the y difference to apply
     * @param dz the z difference to apply
     */
    public void shrink(int dx, int dy, int dz)
    {
        resize(this.width - Math.abs(dx), this.height - Math.abs(dy), this.length - Math.abs(dz), this.origin.getX() - (dx < 0 ? -dx : 0),
                this.origin.getY() - (dy < 0 ? -dy : 0), this.origin.getZ() - (dz < 0 ? -dz : 0));
    }

    /**
     * Resizes the lower edge of the shape.
     * 
     * @param fndx the x difference to apply
     * @param fndy the y difference to apply
     * @param fndz the z difference to apply
     */
    public void resizeNegative(int fndx, int fndy, int fndz)
    {
        grow(fndx > 0 ? -fndx : 0, fndy > 0 ? -fndy : 0, fndz > 0 ? -fndz : 0);
        shrink(fndx < 0 ? fndx : 0, fndy < 0 ? fndy : 0, fndz < 0 ? fndz : 0);
    }

    /**
     * Resizes the upper edge of the shape.
     * 
     * @param fdx the x difference to apply
     * @param fdy the y difference to apply
     * @param fdz the z difference to apply
     */
    public void resizePositive(int fdx, int fdy, int fdz)
    {
        grow(fdx > 0 ? fdx : 0, fdy > 0 ? fdy : 0, fdz > 0 ? fdz : 0);
        shrink(fdx < 0 ? -fdx : 0, fdy < 0 ? -fdy : 0, fdz < 0 ? -fdz : 0);
    }

    /**
     * Changes the size to the given values. Attempts to retain as much of the
     * shape as possible.
     * 
     * @param w the new width
     * @param h the new height
     * @param l the new length
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
                        newShape[x][z][y / 8] =
                                (byte) (newShape[x][z][y / 8] | ((this.shape[xx][zz][yy / 8] & (1 << (yy % 8))) >> (yy % 8) << (y % 8)));
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
     * @param other the shape to match with
     */
    public void matchSize(Shape other)
    {
        checkNotNull(other, "Cannot match size with a null shape.");
        int dx = other.getWidth() - this.getWidth();
        int dy = other.getHeight() - this.getHeight();
        int dz = other.getLength() - this.getLength();
        int odx = (other.getOrigin().getX() - this.origin.getX());
        int ody = (other.getOrigin().getY() - this.origin.getY());
        int odz = (other.getOrigin().getZ() - this.origin.getZ());
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
     * Combines the bounding boxes of this shape with the given shape. the
     * result will be a shape whose bounds includes the bounds of both shapes.
     * 
     * @param other The shape to combine with
     */
    public void combineSizes(Shape other)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Performs a CSG add operation between this shape and the given shape.
     * First ensures that sizes and origins are matched between the two shapes.
     * 
     * @param s the shape to add
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
     * Performs a CSG subtract operation between this shape and the given shape.
     * First ensures that sizes and origins are matched between the two shapes.
     * 
     * @param s the shape to subtract from this shape
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
     * Performs a CSG intersection operation between this shape and the given
     * shape. First ensures that sizes and origins are matched between the two
     * shapes.
     * 
     * @param s the shape to intersect with
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
     * Performs a CSG xor operation between this shape and the given shape.
     * First ensures that sizes and origins are matched between the two shapes.
     * 
     * @param s the shape to xor against
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
     * Returns an array of {@link Vector3i} these vectors represent all set
     * positions of this shape.
     * 
     * @return the set positions
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
     * Flattens this shape. The resultant shape has a height of 1 with the
     * position at each x,z position set if any block in a column above that
     * point was set. As a side effect the origin is set to have a y value of 0.
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
        resize(this.width, 1, this.length, this.origin.getX(), this.origin.getY(), this.origin.getZ());
        setOrigin(new Vector3i(this.origin.getX(), 0, this.origin.getZ()));
    }

    /**
     * Creates a new Shape identical to this one.
     * 
     * @return the duplicate shape
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
}
