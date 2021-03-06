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
package com.voxelplugineering.voxelsniper.brush;

/**
 * An enumeration of standard keys.
 */
@SuppressWarnings("javadoc")
public final class BrushKeys
{

    // Globals
    public static final String AIR_MATERIAL = "$airMaterial";
    public static final String BRUSH_SIZE = "brushSize";
    public static final String MASK_MATERIAL = "$maskmaterial";
    public static final String MASK_MATERIAL_WILDCARD = "maskmaterialwildcard";
    public static final String MATERIAL = "$setMaterial";
    public static final String RANGE = "range";
    public static final String PHYSICS = "physics";

    // Runtime
    public static final String ACTION = "$action";
    public static final String LAST_BLOCK = "$lastBlock";
    public static final String LAST_FACE = "$lastFace";
    public static final String RAY_LENGTH = "$length";
    public static final String ORIGIN = "$origin";
    public static final String PLAYER_PITCH = "$pitch";
    public static final String PLAYER = "$player";
    public static final String SHAPE = "$shape";
    public static final String STRUCTURING_ELEMENT = "$structuringElement";
    public static final String TARGET_BLOCK = "$targetBlock";
    public static final String TARGET_FACE = "$targetFace";
    public static final String PLAYER_YAW = "$yaw";

    // Parameters
    public static final String BIOME = "biome";
    public static final String EXCLUDE_FLUID = "excludeFluid";
    public static final String GROW_PERCENT = "growth";
    public static final String HEIGHT = "height";
    public static final String INNER_RADIUS = "ir";
    public static final String KERNEL = "kernel";
    public static final String KERNEL_SIZE = "kernelSize";
    public static final String LENGTH = "length";
    public static final String OFFSET = "offset";
    public static final String OFFSET_X = "xoffset";
    public static final String OFFSET_Y = "yoffset";
    public static final String OFFSET_Z = "zoffset";
    public static final String PITCH = "pitch";
    public static final String RADIUS_X = "rx";
    public static final String RADIUS_Y = "ry";
    public static final String RADIUS_Z = "rz";
    public static final String RANDOM_CHANCE = "rand";
    public static final String RECURSIONS = "recursions";
    public static final String ROLL = "roll";
    public static final String SEED_PERCENT = "seed";
    public static final String USE_FACE = "face";
    public static final String WIDTH = "width";
    public static final String YAW = "yaw";
    public static final String ENTITY_TYPE = "type";
    public static final String ENTITY_COUNT = "count";

    // Flags
    public static final String POINT_A = "$pointA";
    public static final String POINT_B = "$pointB";

    private BrushKeys()
    {
    }
}
