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
package com.voxelplugineering.voxelsniper.core.commands;

import com.voxelplugineering.voxelsniper.api.brushes.Brush;
import com.voxelplugineering.voxelsniper.api.commands.CommandSender;
import com.voxelplugineering.voxelsniper.api.entity.Player;
import com.voxelplugineering.voxelsniper.core.Gunsmith;

/**
 * A command get fetching the help information for a brush.
 */
public class HelpCommand extends Command
{

    /**
     * Creates a new Command instance.
     */
    public HelpCommand()
    {
        super("voxelhelp", "Provides help information for brush parts: /help <brushName>");
        setAliases("vhelp", "man");
        setPermissions("voxelsniper.command.help");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            Brush brush;
            if (sender instanceof Player)
            {
                brush = ((Player) sender).getBrushManager().getBrush(args[0]).orNull();
            } else
            {
                brush = Gunsmith.getGlobalBrushManager().getBrush(args[0]).orNull();
            }
            if (brush == null)
            {
                sender.sendMessage("Sorry the brush " + args[0] + " could not be found.");
                return true;
            }
            sender.sendMessage(brush.getHelp());
            return true;
        }
        return false;
    }

}
