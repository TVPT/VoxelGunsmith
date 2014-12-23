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
package com.voxelplugineering.voxelsniper.util;

import com.thevoxelbox.vsl.type.Type;
import com.thevoxelbox.vsl.type.TypeDepth;

/**
 * A static registry of common Gunsmith {@link Type}s.
 */
public class GunsmithTypes
{
    
    public static Type COMMONBLOCK;
    public static Type COMMONLOCATION;
    public static Type COMMONMATERIAL;
    public static Type COMMONVECTOR;
    public static Type COMMONWORLD;
    public static Type SHAPE;
    public static Type MATERIALSHAPE;
    
    /**
     * Initializes this registry loading the type information into the global {@link Type} registry and setting the static values.
     */
    public static void init()
    {
        Type.registerType("COMMONBLOCK", "com/voxelplugineering/voxelsniper/common/CommonBlock");
        COMMONBLOCK = Type.getType("COMMONBLOCK", TypeDepth.SINGLE).get();
        Type.registerType("COMMONLOCATION", "com/voxelplugineering/voxelsniper/common/CommonLocation");
        COMMONLOCATION = Type.getType("COMMONLOCATION", TypeDepth.SINGLE).get();
        Type.registerType("COMMONMATERIAL", "com/voxelplugineering/voxelsniper/common/CommonMaterial");
        COMMONMATERIAL = Type.getType("COMMONMATERIAL", TypeDepth.SINGLE).get();
        Type.registerType("COMMONVECTOR", "com/voxelplugineering/voxelsniper/common/CommonVector");
        COMMONVECTOR = Type.getType("COMMONVECTOR", TypeDepth.SINGLE).get();
        Type.registerType("COMMONWORLD", "com/voxelplugineering/voxelsniper/common/CommonWorld");
        COMMONWORLD = Type.getType("COMMONWORLD", TypeDepth.SINGLE).get();
        Type.registerType("SHAPE", "com/voxelplugineering/voxelsniper/shape/Shape");
        SHAPE = Type.getType("SHAPE", TypeDepth.SINGLE).get();
        Type.registerType("MATERIALSHAPE", "com/voxelplugineering/voxelsniper/shape/MaterialShape");
        MATERIALSHAPE = Type.getType("MATERIALSHAPE", TypeDepth.SINGLE).get();
    }
}
