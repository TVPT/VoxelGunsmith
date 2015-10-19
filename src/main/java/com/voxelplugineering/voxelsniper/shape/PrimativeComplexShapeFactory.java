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

import com.voxelplugineering.voxelsniper.util.Direction;

/**
 * A Factory for creating standard shapes.
 */
public class PrimativeComplexShapeFactory
{

    /**
     * Creates a cube with the given radius, the distance along a side of the cube will be
     * radius*2+1
     * 
     * @param radius the radius
     * @return the new shape
     */
    public static ComplexShape createVoxel(double radius)
    {
        return createCuboid(radius, radius, radius);
    }

    /**
     * Creates a square disc with a height of 1, aligned orthogonally to the given direction.
     * 
     * @param radius the radius of the disc
     * @param direction the direction
     * @return the new shape
     */
    public static ComplexShape createVoxelDisc(double radius, Direction direction)
    {
        if (direction == Direction.EAST || direction == Direction.WEST) // x-axis
        {
            return createCuboid(0, radius, radius);
        } else if (direction == Direction.NORTH || direction == Direction.SOUTH) // z-axis
        {
            return createCuboid(radius, radius, 0);
        } else
        // y-axis default
        {
            return createCuboid(radius, 0, radius);
        }
    }

    /**
     * Creates a rectangular volume. the 3 radii are for each of the axes with each side equal to
     * the radius*2+1 where radius is the radius for that specific axis.
     * 
     * @param rx the x-axis radius
     * @param ry the y-axis radius
     * @param rz the z-axis radius
     * @return the new shape
     */
    public static ComplexShape createCuboid(double rx, double ry, double rz)
    {
        ComplexShape s = new ComplexShape((int) Math.floor(rx) * 2 + 1, (int) Math.floor(ry) * 2 + 1, (int) Math.floor(rz) * 2 + 1,
                (int) Math.floor(rx), (int) Math.floor(ry), (int) Math.floor(rz));
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
    public static ComplexShape createEllipsoid(double rx, double ry, double rz)
    {
        ComplexShape s = new ComplexShape((int) Math.ceil(rx) * 2 + 1, (int) Math.ceil(ry) * 2 + 1, (int) Math.ceil(rz) * 2 + 1, (int) Math.ceil(rx),
                (int) Math.ceil(ry), (int) Math.ceil(rz));
        s.set((int) Math.ceil(rx), (int) Math.ceil(ry), (int) Math.ceil(rz), false);
        for (double x = 0; x <= rx; x++)
        {

            final double xSquared = (x / (rx)) * (x / (rx));

            for (double z = 0; z <= rz; z++)
            {

                final double zSquared = (z / (rz)) * (z / (rz));

                for (double y = 0; y <= ry; y++)
                {

                    final double ySquared = (y / (ry)) * (y / (ry));

                    if (xSquared + ySquared + zSquared <= 1)
                    {
                        s.set((int) (Math.ceil(rx) + x), (int) (Math.ceil(ry) + y), (int) (Math.ceil(rz) + z), false);
                        s.set((int) (Math.ceil(rx) + x), (int) (Math.ceil(ry) + y), (int) (Math.ceil(rz) - z), false);
                        s.set((int) (Math.ceil(rx) + x), (int) (Math.ceil(ry) - y), (int) (Math.ceil(rz) + z), false);
                        s.set((int) (Math.ceil(rx) + x), (int) (Math.ceil(ry) - y), (int) (Math.ceil(rz) - z), false);
                        s.set((int) (Math.ceil(rx) - x), (int) (Math.ceil(ry) + y), (int) (Math.ceil(rz) + z), false);
                        s.set((int) (Math.ceil(rx) - x), (int) (Math.ceil(ry) + y), (int) (Math.ceil(rz) - z), false);
                        s.set((int) (Math.ceil(rx) - x), (int) (Math.ceil(ry) - y), (int) (Math.ceil(rz) + z), false);
                        s.set((int) (Math.ceil(rx) - x), (int) (Math.ceil(ry) - y), (int) (Math.ceil(rz) - z), false);
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
    public static ComplexShape createSphere(double r)
    {
        return createEllipsoid(r, r, r);
    }

    /**
     * Creates an elliptical cylinder in the direction of the given axis.
     * 
     * <p> The axes are ordered according to the right had rule, where the directional axis takes
     * the place of the z-axis.
     * 
     * @param rx the first radius perpendicular to the axis
     * @param ry the second radius perpendicular to the axis
     * @param height the height of the cylinder
     * @param direction the direction axis
     * @return the new shape
     */
    public static ComplexShape createEllipticalCylinder(double rx, double ry, int height, Direction direction)
    {
        ComplexShape s;
        if (direction == Direction.EAST || direction == Direction.WEST) // x-axis
        {
            s = new ComplexShape(height, (int) Math.ceil(rx) * 2 + 1, (int) Math.ceil(ry) * 2 + 1, 0, (int) Math.ceil(rx), (int) Math.ceil(ry));
            s.set((int) Math.ceil(rx), (int) Math.ceil(ry), 0, false);
            for (double x = 0; x <= rx; x++)
            {

                final double xSquared = (x / rx) * (x / rx);

                for (double y = 0; y <= ry; y++)
                {

                    final double zSquared = (y / ry) * (y / ry);

                    for (int z = 0; z < height; z++)
                    {
                        if (xSquared + zSquared <= 1)
                        {
                            s.set(z, (int) (Math.ceil(rx) + x), (int) (Math.ceil(ry) + y), false);
                            s.set(z, (int) (Math.ceil(rx) + x), (int) (Math.ceil(ry) - y), false);
                            s.set(z, (int) (Math.ceil(rx) - x), (int) (Math.ceil(ry) + y), false);
                            s.set(z, (int) (Math.ceil(rx) - x), (int) (Math.ceil(ry) - y), false);
                        }

                    }
                }
            }
        } else if (direction == Direction.NORTH || direction == Direction.SOUTH) // z-axis
        {
            s = new ComplexShape((int) Math.ceil(rx) * 2 + 1, (int) Math.ceil(ry) * 2 + 1, height, (int) Math.ceil(rx), (int) Math.ceil(ry), 0);
            s.set((int) Math.ceil(rx), 0, (int) Math.ceil(ry), false);
            for (double x = 0; x <= rx; x++)
            {

                final double xSquared = (x / rx) * (x / rx);

                for (double y = 0; y <= ry; y++)
                {

                    final double zSquared = (y / ry) * (y / ry);

                    for (int z = 0; z < height; z++)
                    {
                        if (xSquared + zSquared <= 1)
                        {
                            s.set((int) (Math.ceil(rx) + x), (int) (Math.ceil(ry) + y), z, false);
                            s.set((int) (Math.ceil(rx) + x), (int) (Math.ceil(ry) - y), z, false);
                            s.set((int) (Math.ceil(rx) - x), (int) (Math.ceil(ry) + y), z, false);
                            s.set((int) (Math.ceil(rx) - x), (int) (Math.ceil(ry) - y), z, false);
                        }

                    }
                }
            }
        } else
        // y-axis default
        {
            s = new ComplexShape((int) Math.ceil(rx) * 2 + 1, height, (int) Math.ceil(ry) * 2 + 1, (int) Math.ceil(rx), 0, (int) Math.ceil(ry));
            s.set((int) Math.ceil(rx), 0, (int) Math.ceil(ry), false);
            for (double x = 0; x <= rx; x++)
            {

                final double xSquared = (x / rx) * (x / rx);

                for (double y = 0; y <= ry; y++)
                {

                    final double zSquared = (y / ry) * (y / ry);

                    for (int z = 0; z < height; z++)
                    {
                        if (xSquared + zSquared <= 1)
                        {
                            s.set((int) (Math.ceil(rx) + x), z, (int) (Math.ceil(ry) + y), false);
                            s.set((int) (Math.ceil(rx) + x), z, (int) (Math.ceil(ry) - y), false);
                            s.set((int) (Math.ceil(rx) - x), z, (int) (Math.ceil(ry) + y), false);
                            s.set((int) (Math.ceil(rx) - x), z, (int) (Math.ceil(ry) - y), false);
                        }

                    }
                }
            }
        }
        return s;
    }

    /**
     * Creates a cylinder with the given radius and height, the flat plane of the cylinder is
     * aligned to be orthogonal to the given direction.
     * 
     * @param radius the radius
     * @param height the height
     * @param direction the direction
     * @return the new shape
     */
    public static ComplexShape createCylinder(double radius, int height, Direction direction)
    {
        return createEllipticalCylinder(radius, radius, height, direction);
    }

    /**
     * Creates a disc with the given radius and a height of 1, the flat plane of the disc is aligned
     * to be orthogonal to the given direction.
     * 
     * @param radius the radius
     * @param direction the direction
     * @return the new shape
     */
    public static ComplexShape createDisc(double radius, Direction direction)
    {
        return createEllipticalCylinder(radius, radius, 1, direction);
    }

    /**
     * Creates a ellipse with the given pair of radii, the flat plane of the ellipse is aligned to
     * be orthogonal to the given direction.
     * 
     * <p> The axes are ordered according to the right had rule, where the direction takes the place
     * of the z-axis.
     * 
     * @param rx the radius in the x-direction
     * @param ry the radius in the y direction
     * @param direction the direction
     * @return the new shape
     */
    public static ComplexShape createEllipse(double rx, double ry, Direction direction)
    {
        return createEllipticalCylinder(rx, ry, 1, direction);
    }

}
