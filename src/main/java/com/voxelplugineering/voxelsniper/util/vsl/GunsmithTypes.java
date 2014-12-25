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
package com.voxelplugineering.voxelsniper.util.vsl;

import com.thevoxelbox.vsl.type.Type;
import com.thevoxelbox.vsl.type.TypeDepth;
import com.voxelplugineering.voxelsniper.world.CommonBlock;

/**
 * A static registry of common Gunsmith {@link Type}s.
 */
@SuppressWarnings("javadoc")
public class GunsmithTypes
{

    public static Type BLOCK;
    public static Type LOCATION;
    public static Type MATERIAL;
    public static Type VECTOR3I;
    public static Type WORLD;
    public static Type SHAPE;
    public static Type MATERIALSHAPE;
    public static Type DIRECTION;
    public static Type SHAPEFACTORY;

    /**
     * Initializes this registry loading the type information into the global {@link Type} registry and setting the static values.
     */
    public static void init()
    {
        Type.registerType("BLOCK", org.objectweb.asm.Type.getInternalName(CommonBlock.class));
        BLOCK = Type.getType("BLOCK", TypeDepth.SINGLE).get();
        Type.registerType("LOCATION", "com/voxelplugineering/voxelsniper/api/world/Location");
        LOCATION = Type.getType("LOCATION", TypeDepth.SINGLE).get();
        Type.registerType("MATERIAL", "com/voxelplugineering/voxelsniper/api/world/material/Material");
        MATERIAL = Type.getType("MATERIAL", TypeDepth.SINGLE).get();
        Type.registerType("VECTOR3I", "com/voxelplugineering/voxelsniper/util/math/Vector3i");
        VECTOR3I = Type.getType("VECTOR3I", TypeDepth.SINGLE).get();
        Type.registerType("WORLD", "com/voxelplugineering/voxelsniper/api/world/World");
        WORLD = Type.getType("WORLD", TypeDepth.SINGLE).get();
        Type.registerType("SHAPE", "com/voxelplugineering/voxelsniper/shape/Shape");
        SHAPE = Type.getType("SHAPE", TypeDepth.SINGLE).get();
        Type.registerType("MATERIALSHAPE", "com/voxelplugineering/voxelsniper/shape/MaterialShape");
        MATERIALSHAPE = Type.getType("MATERIALSHAPE", TypeDepth.SINGLE).get();
        Type.registerType("DIRECTION", "com/voxelplugineering/voxelsniper/util/Direction");
        DIRECTION = Type.getType("DIRECTION", TypeDepth.SINGLE).get();
        Type.registerType("SHAPEFACTORY", "com/voxelplugineering/voxelsniper/shape/ShapeFactory");
        SHAPEFACTORY = Type.getType("SHAPEFACTORY", TypeDepth.SINGLE).get();

        /*Type.registerType("COMMONBLOCK", "com/voxelplugineering/voxelsniper/common/CommonBlock");
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
        MATERIALSHAPE = Type.getType("MATERIALSHAPE", TypeDepth.SINGLE).get();*/
    }
}
