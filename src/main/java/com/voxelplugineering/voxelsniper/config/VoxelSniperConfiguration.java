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
 * A configuration container which is exposed for user settings.
 */
public class VoxelSniperConfiguration
{
    //Default constants
    double RAY_TRACE_MAX_RANGE = 250;
    int BLOCK_CHANGES_PER_SECOND = 160000;
    int UNDO_HISTORY_SIZE = 20;

    //Default brush settings
    String DEFAULT_BRUSH = "snipe material";
    double DEFAULT_BRUSH_SIZE = 3;
    String DEFAULT_BRUSH_MATERIAL = "AIR";
    
    //Default messages
    String PERMISSIONS_REQUIRED_MESSAGE = "You require more permissions in order to perform this action.";
    String DEFAULT_HELP_MESSAGE = "No help is provided for this command.";
    String BRUSH_SIZE_CHANGED_MESSAGE = "Your brush size was changed to %.1f";
    String BRUSH_NOT_FOUND_MESSAGE = "Could not find a brush part named %s";
    String BRUSH_SET_MESSAGE = "Your brush has been set to %s";
    String MATERIAL_NOT_FOUND_MESSAGE = "Could not find that material.";
    String MATERIAL_SET_MESSAGE = "Set material to %s";
    String MATERIAL_MASK_SET_MESSAGE = "Set secondary material to %s";
}
