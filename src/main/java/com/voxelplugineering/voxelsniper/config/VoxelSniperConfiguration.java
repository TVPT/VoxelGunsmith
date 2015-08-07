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
    public static boolean useUUIDsForDataDirectories = true;
    public static boolean generateDefaultAliases = true;

    // Default brush settings
    @ConfigValue(section = "defaults")
    public static String defaultBrush = "voxel material";
    @ConfigValue(section = "defaults")
    public static double defaultBrushSize = 3;
    @ConfigValue(section = "defaults")
    public static String defaultBrushMaterial = "AIR";
    @ConfigValue(section = "defaults", hidden = true)
    public static boolean defaultMaterialMaskWildcard = true;

    // Default messages
    @ConfigValue(section = "messages.command")
    public static String permissionsRequiredMessage = TextFormat.DARK_RED + "You require more permissions in order to perform this action.";
    @ConfigValue(section = "messages.command")
    public static String defaultHelpMessage = TextFormat.RED + "No help is provided for this command.";
    @ConfigValue(section = "messages.command")
    public static String commandPlayerOnly = "Sorry this is a player-only command.";
    @ConfigValue(section = "messages.command", hidden = true)
    public static String keyValueDeliminator = "=";
    @ConfigValue(section = "messages.command.alias")
    public static String aliasSet = TextFormat.GREEN + "Added alias mapping " + TextFormat.BLUE + "'%s'" + TextFormat.GREEN + " to " + TextFormat.BLUE
            + "'%s'";
    @ConfigValue(section = "messages.command.alias")
    public static String aliasNotFound = TextFormat.RED + "Alias target " + TextFormat.BLUE + "%s" + TextFormat.RED
            + " was not found in %s registry.";
    @ConfigValue(section = "messages.command.brush")
    public static String defaultBrushHelpMessage = TextFormat.RED + "No help is provided for this brush part.";
    @ConfigValue(section = "messages.command.brush")
    public static String brushSizeChangedMessage = TextFormat.GREEN + "Your brush size was changed to " + TextFormat.GOLD + "%.1f";
    @ConfigValue(section = "messages.command.brush")
    public static String brushNotFoundMessage = TextFormat.RED + "Could not find a brush part named " + TextFormat.GOLD + "%s";
    @ConfigValue(section = "messages.command.brush")
    public static String brushSetMessage = TextFormat.GREEN + "Your brush has been set to " + TextFormat.GOLD + "%s";
    @ConfigValue(section = "messages.command.brush")
    public static String effectBeforeShape = TextFormat.YELLOW + "You specified an effect without a shape preceeding it.";
    @ConfigValue(section = "messages.command.brush")
    public static String shapeWithoutEffect1 = TextFormat.YELLOW + "You specified a shape without an effect. Defaulting to a material effect.";
    @ConfigValue(section = "messages.command.brush")
    public static String shapeWithoutEffect2 = TextFormat.YELLOW + "To avoid this message use \'/b %s material\' in the future.";
    @ConfigValue(section = "messages.command.material")
    public static String materialNotFoundMessage = TextFormat.RED + "Could not find that material.";
    @ConfigValue(section = "messages.command.material")
    public static String materialSetMessage = TextFormat.GREEN + "Set material to " + TextFormat.GOLD + "%s";
    @ConfigValue(section = "messages.command.material")
    public static String noTargetBlock = TextFormat.RED + "Could not fetch your target block";
    @ConfigValue(section = "messages.command.materialmask")
    public static String materialMaskSetMessage = TextFormat.GREEN + "Set secondary material to " + TextFormat.GOLD + "%s";
    @ConfigValue(section = "messages.command.parameter")
    public static String paramSet = TextFormat.GREEN + "Set brush parameter " + TextFormat.BLUE + "'%s'" + TextFormat.GREEN + " to " + TextFormat.BLUE
            + "'%s'";
    @ConfigValue(section = "messages.command.redoother")
    public static String redootherMessage = TextFormat.GREEN + "Redoing %s's last %d undone changes";
    @ConfigValue(section = "messages.command.undoother")
    public static String playerNotFound = TextFormat.RED + "Sorry, we could not find that player.";
    @ConfigValue(section = "messages.command.undoother")
    public static String undootherMessage = TextFormat.GREEN + "Undoing %s's last %d changes";
    @ConfigValue(section = "messages.command.undo")
    public static String undoMessage = TextFormat.GREEN + "%d changes undone.";
    @ConfigValue(section = "messages.command.redo")
    public static String redoMessage = TextFormat.GREEN + "%d changes re-applied.";
    @ConfigValue(section = "messages.command.vs")
    public static String vsInternal = TextFormat.RED + "Cannot set internal value manually.";
    @ConfigValue(section = "messages.command.vs")
    public static String globalSet = TextFormat.GREEN + "Set global parameter " + TextFormat.BLUE + "'%s'" + TextFormat.GREEN + " to "
            + TextFormat.BLUE + "'%s'";
    @ConfigValue(section = "messages.command.vs")
    public static String vsRangeSet = TextFormat.GREEN + "Set your maximum range to %d";
    @ConfigValue(section = "messages.command.vs")
    public static String vsRangeReset = TextFormat.GREEN + "Reset your maximum range to %d";
    @ConfigValue(section = "messages.command.vs")
    public static String vsConfigReloadSuccess = TextFormat.GREEN + "Successfully reloaded configuration from file.";
    @ConfigValue(section = "messages.command.vs")
    public static String vsConfigReloadFailed = TextFormat.RED + "Failed to reload configuration from file. Check console for errors.";
    @ConfigValue(section = "messages.command.help")
    public static String brushHelpFormat = TextFormat.GREEN + "%s" + TextFormat.GOLD + ": " + TextFormat.AQUA + "%s";

    @ConfigValue(section = "messages.brush")
    public static String missingShape = TextFormat.RED + "You must have at least one shape before your %s brush.";
    @ConfigValue(section = "messages.brush")
    public static String missingParam = TextFormat.RED + "Missing required parameter %s for the %s brush.";
    @ConfigValue(section = "messages.brush")
    public static String missingMaterial = TextFormat.RED + "You must select a material.";
    @ConfigValue(section = "messages.brush")
    public static String missingAltMaterial = TextFormat.RED + "You must select a secondary material.";
    @ConfigValue(section = "messages.brush")
    public static String brushExecError = TextFormat.DARK_RED + "Error executing brush, see console for more details.";
    @ConfigValue(section = "messages.brush")
    public static String paramHeader = TextFormat.GOLD + "Parameters for " + TextFormat.GREEN + "%s" + TextFormat.GOLD + ":";
    @ConfigValue(section = "messages.brush")
    public static String paramInfo = TextFormat.GREEN + "    %s" + TextFormat.GOLD + " - " + TextFormat.YELLOW + "%s";

    @ConfigValue(section = "messages.brush.set")
    public static String setFirstPoint = TextFormat.GREEN + "Set first point to (%d, %d, %d).";
    @ConfigValue(section = "messages.brush.set")
    public static String setSecondPoint = TextFormat.GREEN + "Set second point to (%d, %d, %d).";

    // Brush settings
    @ConfigValue(section = "brush.blend")
    public static String blendDefaultKernalShape = "voxel";
    @ConfigValue(section = "brush.blend")
    public static double blendDefaultKernalSize = 1.0;
    @ConfigValue(section = "brush.linearblend")
    public static String linearblendDefaultKernalShape = "voxel";
    @ConfigValue(section = "brush.linearblend")
    public static double linearblendDefaultKernalSize = 2.0;

    @ConfigValue(section = "brush.random")
    public static double randomDefaultChance = 0.5;
    @ConfigValue(section = "brush.splatter")
    public static double splatterDefaultSeed = 0.1;
    @ConfigValue(section = "brush.splatter")
    public static double splatterDefaultGrowth = 0.1;
    @ConfigValue(section = "brush.splatter")
    public static int splatterDefaultRecursions = 3;
    @ConfigValue(section = "brush.splatter")
    public static int splatterMaxRecursions = 10;

    @ConfigValue(section = "brush.cylinder")
    public static boolean cylDefaultFace = false;
    @ConfigValue(section = "brush.disc")
    public static boolean discDefaultFace = false;
    @ConfigValue(section = "brush.voxeldisc")
    public static boolean voxeldiscDefaultFace = false;
    @ConfigValue(section = "brush.ellipse")
    public static boolean ellipseDefaultFace = false;

    // Alias Settings
    public static boolean caseSensitiveAliases = true;

}
