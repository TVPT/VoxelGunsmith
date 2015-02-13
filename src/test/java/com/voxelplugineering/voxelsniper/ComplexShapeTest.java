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
package com.voxelplugineering.voxelsniper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.shape.ComplexShape;
import com.voxelplugineering.voxelsniper.shape.csg.CuboidShape;
import com.voxelplugineering.voxelsniper.shape.csg.EllipsoidShape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * A set of tests for the {@link ComplexShape}.
 */
public class ComplexShapeTest
{

    /**
     * 
     */
    @Test
    public void testBasic()
    {
        ComplexShape shape = new ComplexShape(1, 1, 1);
        assertEquals(false, shape.get(0, 0, 0, false));

        shape.set(0, 0, 0, false);
        assertEquals(true, shape.get(0, 0, 0, false));

        shape.unset(0, 0, 0, false);
        assertEquals(false, shape.get(0, 0, 0, false));
    }

    /**
     * 
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testBasicOOB2()
    {
        ComplexShape shape = new ComplexShape(0, 0, 0);
        shape.set(1, 1, 1, false);
    }

    /**
     * 
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testBasicOOB3()
    {
        ComplexShape shape = new ComplexShape(0, 0, 0);
        shape.get(1, 1, 1, false);
    }

    /**
     * 
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testBasicOOB()
    {
        ComplexShape shape = new ComplexShape(0, 0, 0);
        shape.unset(1, 1, 1, false);
    }

    /**
     * 
     */
    @Test
    public void testBasic2()
    {
        ComplexShape shape = new ComplexShape(1, 1, 1, new Vector3i(2, 2, 2));
        assertEquals(false, shape.get(-2, -2, -2, true));

        shape.set(-2, -2, -2, true);
        assertEquals(true, shape.get(-2, -2, -2, true));

        shape.unset(-2, -2, -2, true);
        assertEquals(false, shape.get(-2, -2, -2, true));
    }

    /**
     * 
     */
    @Test
    public void testMoveOrigin()
    {
        ComplexShape shape = new ComplexShape(1, 1, 1, new Vector3i(0, 0, 0));
        assertEquals(false, shape.get(0, 0, 0, true));

        shape.setOrigin(new Vector3i(2, 2, 2));
        assertEquals(false, shape.get(-2, -2, -2, true));
    }

    /**
     * 
     */
    @Test
    public void testFromShape()
    {
        Shape s = new EllipsoidShape(11, 11, 11, new Vector3i(5, 5, 5));
        ComplexShape c = new ComplexShape(s);
        assertShapesEqual(s, c);
    }

    /**
     * 
     */
    @Test
    public void testClone()
    {
        ComplexShape shape = new ComplexShape(new EllipsoidShape(11, 11, 11, new Vector3i(5, 5, 5)));
        ComplexShape clone = shape.clone();
        assertShapesEqual(shape, clone);
    }

    /**
     * 
     */
    @Test
    public void testGrow()
    {
        ComplexShape shape = new ComplexShape(1, 1, 1);
        shape.grow(1, 1, 1);

        assertEquals(2, shape.getWidth());
        assertEquals(2, shape.getHeight());
        assertEquals(2, shape.getHeight());

        assertEquals(0, shape.getOrigin().getX());
        assertEquals(0, shape.getOrigin().getY());
        assertEquals(0, shape.getOrigin().getZ());
    }

    /**
     * 
     */
    @Test
    public void testGrow2()
    {
        CuboidShape s = new CuboidShape(1, 1, 1, new Vector3i(1, 0, 0));
        ComplexShape shape = new ComplexShape(s);
        shape.grow(-1, -1, -1);

        assertEquals(2, shape.getWidth());
        assertEquals(2, shape.getHeight());
        assertEquals(2, shape.getHeight());

        assertEquals(2, shape.getOrigin().getX());
        assertEquals(1, shape.getOrigin().getY());
        assertEquals(1, shape.getOrigin().getZ());
    }

    /**
     * 
     */
    @Test
    public void testGrow3()
    {
        ComplexShape shape = new ComplexShape(1, 1, 1);
        shape.grow(-1, -1, -1);

        assertEquals(2, shape.getWidth());
        assertEquals(2, shape.getHeight());
        assertEquals(2, shape.getHeight());

        assertEquals(1, shape.getOrigin().getX());
        assertEquals(1, shape.getOrigin().getY());
        assertEquals(1, shape.getOrigin().getZ());
    }

    /**
     * 
     */
    @Test
    public void testShrink()
    {
        ComplexShape shape = new ComplexShape(2, 2, 2);
        shape.shrink(1, 1, 1);

        assertEquals(1, shape.getWidth());
        assertEquals(1, shape.getHeight());
        assertEquals(1, shape.getHeight());

        assertEquals(0, shape.getOrigin().getX());
        assertEquals(0, shape.getOrigin().getY());
        assertEquals(0, shape.getOrigin().getZ());
    }

    /**
     * 
     */
    @Test
    public void testShrink2()
    {
        CuboidShape s = new CuboidShape(2, 2, 2, new Vector3i(1, 1, 1));
        ComplexShape shape = new ComplexShape(s);
        shape.shrink(-1, -1, -1);

        assertEquals(1, shape.getWidth());
        assertEquals(1, shape.getHeight());
        assertEquals(1, shape.getHeight());

        assertEquals(0, shape.getOrigin().getX());
        assertEquals(0, shape.getOrigin().getY());
        assertEquals(0, shape.getOrigin().getZ());
    }

    /**
     * 
     */
    @Test
    public void testShrink3()
    {
        ComplexShape shape = new ComplexShape(2, 2, 2);
        shape.shrink(-1, -1, -1);

        assertEquals(1, shape.getWidth());
        assertEquals(1, shape.getHeight());
        assertEquals(1, shape.getHeight());

        assertEquals(-1, shape.getOrigin().getX());
        assertEquals(-1, shape.getOrigin().getY());
        assertEquals(-1, shape.getOrigin().getZ());
    }

    /**
     * 
     */
    @Test
    public void testMatchsize()
    {
        ComplexShape shape1 = new ComplexShape(2, 1, 1);
        ComplexShape shape2 = new ComplexShape(1, 1, 1);
        shape1.matchSize(shape2);

        assertShapesEqual(shape2, shape1);
    }

    /**
     * 
     */
    @Test
    public void testMatchsize2()
    {
        ComplexShape shape1 = new ComplexShape(1, 1, 1);
        ComplexShape shape2 = new ComplexShape(2, 1, 1);
        shape1.matchSize(shape2);

        assertShapesEqual(shape2, shape1);
    }

    /**
     * 
     */
    @Test
    public void testMatchsize3()
    {
        ComplexShape shape1 = new ComplexShape(2, 1, 1, 1, 0, 0);
        ComplexShape shape2 = new ComplexShape(1, 1, 1);
        shape1.matchSize(shape2);

        assertShapesEqual(shape2, shape1);
    }

    /**
     * 
     */
    @Test
    public void testMatchsize4()
    {
        ComplexShape shape1 = new ComplexShape(1, 1, 1);
        ComplexShape shape2 = new ComplexShape(2, 1, 1, 1, 0, 0);
        shape1.matchSize(shape2);

        assertShapesEqual(shape2, shape1);
    }

    /**
     * 
     */
    @Test
    public void testMatchsize5()
    {
        ComplexShape shape1 = new ComplexShape(3, 1, 1, 1, 0, 0);
        ComplexShape shape2 = new ComplexShape(1, 1, 1);
        shape1.matchSize(shape2);

        assertShapesEqual(shape2, shape1);
    }

    /**
     * 
     */
    @Test
    public void testMatchsize6()
    {
        ComplexShape shape1 = new ComplexShape(1, 1, 1);
        ComplexShape shape2 = new ComplexShape(3, 1, 1, 1, 0, 0);
        shape1.matchSize(shape2);

        assertShapesEqual(shape2, shape1);
    }

    /**
     * 
     */
    @Test
    public void testFlatten()
    {
        ComplexShape shape1 = new ComplexShape(1, 2, 2);
        shape1.set(0, 1, 1, false);
        ComplexShape shape2 = new ComplexShape(1, 1, 2);
        shape2.set(0, 0, 1, false);
        shape1.flatten();

        assertShapesDeepEqual(shape1, shape2);
    }

    /**
     * 
     */
    @Test
    public void testFlatten4()
    {
        ComplexShape shape1 = new ComplexShape(1, 2, 2, 0, 1, 0);
        shape1.set(0, 1, 1, false);
        ComplexShape shape2 = new ComplexShape(1, 1, 2);
        shape2.set(0, 0, 1, false);
        shape1.flatten();
        assertShapesDeepEqual(shape1, shape2);
    }

    /**
     * 
     */
    @Test
    public void testGetShape()
    {
        ComplexShape shape1 = new ComplexShape(4, 1, 1);
        shape1.set(1, 0, 0, false);
        shape1.set(3, 0, 0, false);

        Vector3i[] points = shape1.getShape();
        assertEquals(2, points.length);
        assertVectorEquals(new Vector3i(1, 0, 0), points[0]);
        assertVectorEquals(new Vector3i(3, 0, 0), points[1]);
    }

    /**
     * 
     */
    @Test
    public void testResizePositive()
    {
        ComplexShape shape = new ComplexShape(1, 1, 1);
        shape.resizePositive(1, 0, 0);

        assertEquals(2, shape.getWidth());
        assertEquals(1, shape.getHeight());
        assertEquals(1, shape.getHeight());

        assertEquals(0, shape.getOrigin().getX());
        assertEquals(0, shape.getOrigin().getY());
        assertEquals(0, shape.getOrigin().getZ());
    }

    /**
     * 
     */
    @Test
    public void testResizePositive2()
    {
        ComplexShape shape = new ComplexShape(2, 1, 1);
        shape.resizePositive(-1, 0, 0);

        assertEquals(1, shape.getWidth());
        assertEquals(1, shape.getHeight());
        assertEquals(1, shape.getHeight());

        assertEquals(0, shape.getOrigin().getX());
        assertEquals(0, shape.getOrigin().getY());
        assertEquals(0, shape.getOrigin().getZ());
    }

    /**
     * 
     */
    @Test
    public void testResizeNegative()
    {
        ComplexShape shape = new ComplexShape(1, 1, 1);
        shape.resizeNegative(1, 0, 0);

        assertEquals(2, shape.getWidth());
        assertEquals(1, shape.getHeight());
        assertEquals(1, shape.getHeight());

        assertEquals(1, shape.getOrigin().getX());
        assertEquals(0, shape.getOrigin().getY());
        assertEquals(0, shape.getOrigin().getZ());
    }

    /**
     * 
     */
    @Test
    public void testResizeNegative2()
    {
        ComplexShape shape = new ComplexShape(2, 1, 1);
        shape.resizeNegative(-1, 0, 0);

        assertEquals(1, shape.getWidth());
        assertEquals(1, shape.getHeight());
        assertEquals(1, shape.getHeight());

        assertEquals(-1, shape.getOrigin().getX());
        assertEquals(0, shape.getOrigin().getY());
        assertEquals(0, shape.getOrigin().getZ());
    }

    /**
     * 
     */
    @Test
    public void testInvert()
    {
        ComplexShape shape1 = new ComplexShape(1, 1, 1);
        ComplexShape shape2 = new ComplexShape(1, 1, 1);
        shape2.set(0, 0, 0, false);
        shape1.invert();
        assertShapesDeepEqual(shape2, shape1);
    }

    /**
     * 
     */
    // @Test
    public void testCombine()
    {
        ComplexShape shape1 = new ComplexShape(1, 1, 1);
        shape1.set(0, 0, 0, false);
        ComplexShape shape2 = new ComplexShape(1, 1, 1, 2, 0, 0);
        shape2.set(2, 0, 0, false);
        ComplexShape shape3 = new ComplexShape(3, 1, 1);
        shape3.set(0, 0, 0, false);
        shape1.combineSizes(shape2);
        assertShapesDeepEqual(shape3, shape1);
    }

    /**
     * 
     */
    // @Test
    public void testAdd()
    {
        ComplexShape shape1 = new ComplexShape(1, 1, 1);
        shape1.set(0, 0, 0, false);
        ComplexShape shape2 = new ComplexShape(3, 1, 1);
        shape2.set(2, 0, 0, false);
        ComplexShape shape3 = new ComplexShape(1, 1, 1);
        shape3.set(0, 0, 0, false);
        shape3.set(2, 0, 0, false);
        shape1.add(shape2);
        assertShapesDeepEqual(shape3, shape1);
    }

    /**
     * 
     */
    // @Test
    public void testAdd2()
    {
        ComplexShape shape1 = new ComplexShape(1, 1, 1);
        shape1.set(0, 0, 0, false);
        ComplexShape shape2 = new ComplexShape(1, 1, 1, new Vector3i(-2, 0, 0));
        shape2.set(0, 0, 0, false);
        ComplexShape shape3 = new ComplexShape(1, 1, 1);
        shape3.set(0, 0, 0, false);
        shape3.set(2, 0, 0, false);
        shape1.add(shape2);
        assertShapesDeepEqual(shape3, shape1);
    }

    private static void assertVectorEquals(Vector3i a, Vector3i b)
    {
        assertEquals(a.getX(), b.getX());
        assertEquals(a.getY(), b.getY());
        assertEquals(a.getZ(), b.getZ());
    }

    private static void assertShapesEqual(Shape a, Shape b)
    {
        assertEquals(a.getWidth(), b.getWidth());
        assertEquals(a.getHeight(), b.getHeight());
        assertEquals(a.getHeight(), b.getHeight());

        assertVectorEquals(a.getOrigin(), b.getOrigin());
    }

    private static void assertShapesDeepEqual(Shape a, Shape b)
    {
        assertEquals(a.getWidth(), b.getWidth());
        assertEquals(a.getHeight(), b.getHeight());
        assertEquals(a.getHeight(), b.getHeight());

        assertVectorEquals(a.getOrigin(), b.getOrigin());

        for (int x = 0; x < a.getWidth(); x++)
        {
            for (int y = 0; y < a.getHeight(); y++)
            {
                for (int z = 0; z < a.getLength(); z++)
                {
                    assertEquals(a.get(x, y, z, false), b.get(x, y, z, false));
                }
            }
        }
    }

}
