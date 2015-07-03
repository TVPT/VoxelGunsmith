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


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * A Factory for creating standard shapes.
 */
public class PrimativeShapeFactory
{
    private static Set<String> cuboidNames;
    private static Set<String> cylinderNames;
    private static Set<String> ellipsoidNames;
    private static Set<String> flatCuboidNames;
    private static Set<String> flatCylinderNames;
    private static Set<String> flatEllipsoidNames;
    
    /*
     * Creates a shape from it's name and a size.
     * Shapes with direction are assumed to face in the positive y direction.
     */
    public static Optional<Shape> createShape(String name, double size)
    {
        String type = name.toLowerCase();
        Shape s = null;
        int origin = (int) size;
        int diameter = (int) ((2 * size) + 1);
        if(cuboidNames.contains(type))
        {
            s = new CuboidShape(diameter, diameter, diameter, new Vector3i(origin, origin, origin));
        }
        if(flatCuboidNames.contains(type))
        {
            s = new CuboidShape(diameter, 1, diameter, new Vector3i(origin, 0, origin));
        }
        if(ellipsoidNames.contains(type))
        {
            s = new EllipsoidShape(size, size, size, new Vector3i(origin, origin, origin));
        }
        if(flatEllipsoidNames.contains(type))
        {
            s = new EllipsoidShape(size, 1, size, new Vector3i(origin, 0, origin));
        }
        if(cylinderNames.contains(type))
        {
            s = new CylinderShape(size, origin, size, new Vector3i(origin, origin, origin));
        }
        if(flatCylinderNames.contains(type))
        {
            s = new CylinderShape(size, 1, size, new Vector3i(origin, 0, origin));
        }
        return Optional.of(s);
    }
    
    static {
        cuboidNames = new HashSet<String>(Arrays.asList(new String[]{"cube", "cuboid", "voxel", "box", "brick", "3Dsquare", "3Dvoxeldisc", "3Dvoxeldisk", "3Drectangle"}));
        cylinderNames = new HashSet<String>(Arrays.asList(new String[]{"cylinder", "barrel", "tube", "pipe", "3Ddisc", "3Ddisk","3Dcircle"}));
        ellipsoidNames = new HashSet<String>(Arrays.asList(new String[]{"ellipsoid", "sphere", "ball", "3Dcircle", "3Dellipse"}));
        flatCuboidNames = new HashSet<String>(Arrays.asList(new String[]{"square", "rectangle", "voxeldisc", "voxeldisk", "2Dcube", "2Dbrick", "2Dbox", "2Dcube", "2Dbrick"}));
        flatCylinderNames = new HashSet<String>(Arrays.asList(new String[]{"circle", "disk", "disc", "2Dpipe", "2Dcylinder", "2Dbarrel","2Dtube"}));
        flatEllipsoidNames = new HashSet<String>(Arrays.asList(new String[]{"oval", "ellipse", "2Dellipsoid", "2Dsphere", "2Dball"}));
    }
}
