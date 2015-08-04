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
import com.voxelplugineering.voxelsniper.service.text.TextFormat;

/**
 * A configuration container which is exposed for user settings.
 */
@SuppressWarnings("javadoc")
@ConfigurationContainer(name = "voxelsniper")
public class VoxelSniperConfiguration
{

    // Default constants
    public static double rayTraceRange = 250;
    public static int blockChangesPerSecond = 80000;
    public static int undoHistorySize = 20;
    public static int statusMessageThreshold = 80000;
    public static String playerDataDirectory = "players/";

    // Default brush settings
    @ConfigValue(section = "defaults")
    public static String defaultBrush = "voxel material";
    @ConfigValue(section = "defaults")
    public static double defaultBrushSize = 3;
    @ConfigValue(section = "defaults")
    public static String defaultBrushMaterial = "AIR";

    // Default messages
    @ConfigValue(section = "messages")
    public static String permissionsRequiredMessage = TextFormat.DARK_RED + "You require more permissions in order to perform this action.";
    @ConfigValue(section = "messages")
    public static String defaultHelpMessage = TextFormat.RED + "No help is provided for this command.";
    @ConfigValue(section = "messages")
    public static String defaultBrushHelpMessage = TextFormat.RED + "No help is provided for this brush part.";
    @ConfigValue(section = "messages")
    public static String brushSizeChangedMessage = TextFormat.GREEN + "Your brush size was changed to " + TextFormat.GOLD + "%.1f";
    @ConfigValue(section = "messages")
    public static String brushNotFoundMessage = TextFormat.RED + "Could not find a brush part named " + TextFormat.GOLD + "%s";
    @ConfigValue(section = "messages")
    public static String brushSetMessage = TextFormat.GREEN + "Your brush has been set to " + TextFormat.GOLD + "%s";
    @ConfigValue(section = "messages")
    public static String materialNotFoundMessage = TextFormat.RED + "Could not find that material.";
    @ConfigValue(section = "messages")
    public static String materialSetMessage = TextFormat.GREEN + "Set material to " + TextFormat.GOLD + "%s";
    @ConfigValue(section = "messages")
    public static String materialMaskSetMessage = TextFormat.GREEN + "Set secondary material to " + TextFormat.GOLD + "%s";
    @ConfigValue(section = "messages")
    public static String defaultCommandHelpMessage = TextFormat.RED + "No help is provided for this command.";

    // Alias Settings
    public static boolean caseSensitiveAliases = true;

}
