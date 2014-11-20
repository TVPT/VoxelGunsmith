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

/**
 * A Factory for creating standard shapes.
 */
public class ShapeFactory
{

    /**
     * Creates a cube with the given radius, the distance along a side of the cube will be radius*2+1
     * 
     * @param radius the radius
     * @return the new shape
     */
    public static Shape createVoxel(int radius)
    {
        return createCuboid(radius, radius, radius);
    }

    /**
     * Creates a rectangular area shape. the 3 radii are for each of the axes with each side equal to the radius*2+1 where radius is the radius for
     * that specific axis.
     * 
     * @param rx the x-axis radius
     * @param ry the y-axis radius
     * @param rz the z-axis radius
     * @return the new shape
     */
    public static Shape createCuboid(int rx, int ry, int rz)
    {
        Shape s = new Shape(rx * 2 + 1, ry * 2 + 1, rz * 2 + 1, rx, ry, rz);
        s.invert();
        return s;
    }

    /**
     * Creates an ellipsoidal shape.
     * 
     * @param rx the x-axis radius
     * @param ry the y-axis radius
     * @param rz the z-axis radius
     * @return the new shape
     */
    public static Shape createEllipsoid(double rx, double ry, double rz)
    {
        Shape s = new Shape((int) rx * 2 + 1, (int) ry * 2 + 1, (int) rz * 2 + 1, (int) rx, (int) ry, (int) rz);
        for (int x = (int) -rx; x <= rx; x++)
        {
            int nx = x + (int) rx;
            for (int y = (int) -ry; y <= ry; y++)
            {
                int ny = y + (int) ry;
                for (int z = (int) -rz; z <= rz; z++)
                {
                    int nz = z + (int) rz;
                    if ((x / rx) * (x / rx) + (y / ry) * (y / ry) + (z / rz) * (z / rz) < 1)
                    {
                        s.set(nx, ny, nz);
                    }
                }
            }
        }
        return s;
    }

    /**
     * Creates a sphere.
     * 
     * @param r the radius
     * @return the new shape
     */
    public static Shape createSphere(double r)
    {
        return createEllipsoid(r, r, r);
    }

    /**
     * Creates an elliptical cylinder in the direction of the given axis. The axes are numbered as follows:
     * <p>
     * 1: x-axis<br>
     * 2: y-axis<br>
     * 3: z-axis
     * <p>
     * The axes are ordered according to the right had rule, where the directional access takes the place of the z-axis.
     * 
     * @param rx the first radius perpendicular to the axis
     * @param ry the second radius perpendicular to the axis
     * @param height the height of the cylinder
     * @param axis the direction axis
     * @return the new shape
     */
    public static Shape createEllipticalCylinder(double rx, double ry, double height, int axis)
    {
        Shape s;
        if (axis == 1) // x-axis
        {
            s = new Shape((int) height, (int) rx * 2 + 1, (int) ry * 2 + 1);
        } else if (axis == 3) // z-axis
        {
            s = new Shape((int) rx * 2 + 1, (int) ry * 2 + 1, (int) height);
        } else
        // y-axis default
        {
            s = new Shape((int) rx * 2 + 1, (int) height, (int) ry * 2 + 1);
        }
        return s;
    }

}
