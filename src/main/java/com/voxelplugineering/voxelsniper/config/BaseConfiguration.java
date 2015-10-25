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

import com.voxelplugineering.voxelsniper.service.config.ConfigValue;
import com.voxelplugineering.voxelsniper.service.config.ConfigurationContainer;

/**
 * Default base configuration values.
 */
@ConfigurationContainer(name = "additional",
        createByDefault = false)
@SuppressWarnings("javadoc")
public class BaseConfiguration
{

    @ConfigValue(hidden = true)
    public static double playerEyeHeight = 1.62;
    @ConfigValue(hidden = true)
    public static int minimumWorldDepth = 0;
    @ConfigValue(hidden = true)
    public static int maximumWorldHeight = 255;
    @ConfigValue(hidden = true)
    public static double rayTraceStep = 0.2;

    public static String defaultBiomeName = "plains";
    public static String defaultMaterialName = "air";

    @ConfigValue(hidden = true)
    public static String eventBusThreadPrefix = "VoxelSniperEventBus-";

    @ConfigValue(hidden = true)
    public static int aliasInterval = 30000;
    @ConfigValue(hidden = true)
    public static int changeInterval = 100;

}
