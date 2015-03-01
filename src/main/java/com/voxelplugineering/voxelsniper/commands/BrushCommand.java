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

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.commands.CommandSender;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormat;
import com.voxelplugineering.voxelsniper.brushes.BrushChain;
import com.voxelplugineering.voxelsniper.command.Command;
import com.voxelplugineering.voxelsniper.util.BrushParsing;
import com.voxelplugineering.voxelsniper.util.StringUtilities;

/**
 * Standard brush command to select a brush and provide the necessary arguments
 * to said brush.
 */
public class BrushCommand extends Command
{

    /**
     * The message sent to players when their brush size is changed.
     */
    private String brushSizeChangeMessage = Gunsmith.getConfiguration().get("brushSizeChangedMessage", String.class)
            .or("Your brush size was changed to %.1f");
    /**
     * The message sent to players when their brush is set.
     */
    private String brushSetMessage = Gunsmith.getConfiguration().get("brushSetMessage", String.class).or("Your brush has been set to %s");

    /**
     * Constructs a new BrushCommand
     */
    public BrushCommand()
    {
        super("brush", "Sets your current brush");
        setAliases("b");
        setPermissions("voxelsniper.command.brush");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        checkNotNull(sender, "Cannot have a null sniper!");

        if (!sender.isPlayer())
        {
            sender.sendMessage("Sorry this is a player-only command.");
            return true;
        }
        Player sniper = (Player) sender;
        if(!Gunsmith.getPermissionsProxy().hasPermission(sniper, "voxelsniper.command.brush"))
        {
            sender.sendMessage("Sorry you lack the required permissions for this command");
            return true;
        }
        if (args.length == 1)
        {
            try
            {
                double brushSize = Double.parseDouble(args[0]);
                sniper.getBrushSettings().set("brushSize", brushSize);
                sniper.sendMessage(this.brushSizeChangeMessage, brushSize);
                return true;
            } catch (NumberFormatException ignored)
            {
                assert true;
            }
        }
        if (args.length >= 1)
        {

            String fullBrush = StringUtilities.getSection(args, 0, args.length - 1);
            Optional<BrushChain> brush = BrushParsing.parse(fullBrush, sniper.getPersonalBrushManager(), sniper.getPersonalAliasHandler().getRegistry("brush").get());
            if (brush.isPresent())
            {
                sniper.setCurrentBrush(brush.get());
                sniper.sendMessage(this.brushSetMessage, fullBrush);

                //TODO non-action check
            } else
            {
                sniper.sendMessage(TextFormat.RED + "Failed to parse brush, check your brush names!");
            }

            return true;
        }
        sender.sendMessage(this.getHelpMsg());
        return false;
    }
}
