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
package com.voxelplugineering.voxelsniper.config;

/**
 * Default base configuration values. All fields should be declared as static and final.
 */
public class BaseConfiguration
{

    static final double PLAYER_EYE_HEIGHT = 1.62;
    static final int MINIMUM_WORLD_DEPTH = 0;
    static final int MAXIMUM_WORLD_HEIGHT = 255;
    static final double RAY_TRACE_STEP = 0.2;
    static final double RAY_TRACE_RANGE = 250;
    static final String DEFAULT_BRUSH = "snipe";
    static final double DEFAULT_BRUSH_SIZE = 3;
    static final String PERMISSIONS_REQUIRED_MESSAGE = "You require more permissions in order to perform this action.";
    static final int BLOCK_CHANGES_PER_TICK = 5000;

}
