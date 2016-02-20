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

import com.voxelplugineering.voxelsniper.brush.BrushWrapper;
import com.voxelplugineering.voxelsniper.brush.GlobalBrushManager;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.util.Context;

/**
 * A command get fetching the help information for a brush.
 */
public class HelpCommand extends Command
{

    private final GlobalBrushManager brushes;

    /**
     * Creates a new Command instance.
     * 
     * @param context The context
     */
    public HelpCommand(Context context)
    {
        super("voxelhelp", "Provides help information for brush parts: /vhelp <brushName>", context);
        setAliases("vhelp", "man");
        setPermissions("voxelsniper.command.help");
        this.brushes = context.getRequired(GlobalBrushManager.class);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            BrushWrapper brush;
            if (sender instanceof Player)
            {
                brush = ((Player) sender).getBrushManager().getBrush(args[0]).orElse(null);
            } else
            {
                brush = this.brushes.getBrush(args[0]).orElse(null);
            }
            if (brush == null)
            {
                sender.sendMessage(VoxelSniperConfiguration.brushNotFoundMessage, args[0]);
                return true;
            }
            sender.sendMessage(VoxelSniperConfiguration.brushHelpFormat, brush.getName(), brush.getHelp());
            return true;
        }
        return false;
    }

}
