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
package com.voxelplugineering.voxelsniper.commands;

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushManager;
import com.voxelplugineering.voxelsniper.brush.BrushWrapper;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.StringUtilities;

import java.util.Optional;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class ParameterCommand extends Command
{

    /**
     * Constructs a new {@link ParameterCommand}.
     * 
     * @param context The context
     */
    public ParameterCommand(Context context)
    {
        super("param", "Sets a brushes parameter: /param [target] key=value", context);
        setAliases("parameter", "p");
        setPermissions("voxelsniper.command.param");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        checkNotNull(sender, "Cannot have a null sniper!");

        if (!sender.isPlayer())
        {
            sender.sendMessage(VoxelSniperConfiguration.commandPlayerOnly);
            return true;
        }
        Player sniper = (Player) sender;
        if (args.length < 2)
        {
            sniper.getCurrentBrush().printParams(sender);
            return true;
        }
        String target = args[0];
        BrushManager brushes = sniper.getBrushManager();
        Optional<BrushWrapper> brush = brushes.getBrush(target);
        if (brush.isPresent())
        {
            String arg = StringUtilities.getSection(args, 1, args.length - 1);
            if (!arg.contains(VoxelSniperConfiguration.keyValueDeliminator))
            {
                sniper.sendMessage(this.getHelpMsg());
                return true;
            }
            int k = arg.indexOf(VoxelSniperConfiguration.keyValueDeliminator);
            String key = arg.substring(0, k).trim();
            String value = arg.substring(k + 1, arg.length()).trim();
            sniper.getBrushVars().set(BrushContext.of(brush.get()), key, value);
            sniper.sendMessage(VoxelSniperConfiguration.paramSet, key, value);
        } else
        {
            sniper.sendMessage(VoxelSniperConfiguration.brushNotFoundMessage, target);
        }
        return true;
    }

}
