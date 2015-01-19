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
import com.voxelplugineering.voxelsniper.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.api.brushes.BrushParser;
import com.voxelplugineering.voxelsniper.api.brushes.BrushParser.BrushPart;
import com.voxelplugineering.voxelsniper.api.commands.CommandSender;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormat;
import com.voxelplugineering.voxelsniper.brushes.BrushNodeGraph;
import com.voxelplugineering.voxelsniper.brushes.StandardBrushParser;
import com.voxelplugineering.voxelsniper.command.Command;
import com.voxelplugineering.voxelsniper.util.StringUtilities;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class BrushCommand extends Command
{
    /**
     * The message sent to players when their brush size is changed.
     */
    private String brushSizeChangeMessage = "Your brush size was changed to %.1f";
    /**
     * The message sent to players when a brush is not found.
     */
    private String brushNotFoundMessage = "Could not find a brush part named %s";
    /**
     * The message sent to players when their brush is set.
     */
    private String brushSetMessage = "Your brush has been set to %s";

    /**
     * Constructs a new BrushCommand
     */
    public BrushCommand()
    {
        super("brush", "Sets your current brush");
        setAliases("b");
        setPermissions("voxelsniper.command.brush");
        if (Gunsmith.getConfiguration().has("brushSizeChangedMessage"))
        {
            this.brushSizeChangeMessage = Gunsmith.getConfiguration().get("brushSizeChangedMessage").get().toString();
        }
        if (Gunsmith.getConfiguration().has("brushNotFoundMessage"))
        {
            this.brushNotFoundMessage = Gunsmith.getConfiguration().get("brushNotFoundMessage").get().toString();
        }
        if (Gunsmith.getConfiguration().has("brushSetMessage"))
        {
            this.brushSetMessage = Gunsmith.getConfiguration().get("brushSetMessage").get().toString();
        }
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

            Optional<AliasRegistry> alias = sniper.getPersonalAliasHandler().getRegistry("brush");
            String fullBrush = StringUtilities.getSection(args, 0, args.length - 1);
            BrushNodeGraph start = null;
            BrushNodeGraph last = null;
            BrushParser parser = new StandardBrushParser();
            Optional<BrushPart[]> partss = parser.parse(fullBrush);
            if (!partss.isPresent())
            {
                sender.sendMessage(TextFormat.DARK_RED + "Sorry, That brush was invalid.");
                return false;
            }
            BrushPart[] parts = partss.get();
            if (alias.isPresent())
            {
                parts = alias.get().expand(parts, parser);
            }
            for (BrushPart part : parts)
            {
                String brushName = part.getBrushName();
                BrushNodeGraph brush = sniper.getPersonalBrushManager().getBrush(brushName).orNull();
                if (brush == null)
                {
                    sniper.getPersonalBrushManager().loadBrush(brushName);
                    brush = sniper.getPersonalBrushManager().getBrush(brushName).orNull();
                    if (brush == null)
                    {
                        sniper.sendMessage(this.brushNotFoundMessage, brushName);
                        return false;
                    }
                }
                if (start == null)
                {
                    start = brush;
                    last = brush;
                } else
                {
                    last.chain(brush);
                    last = brush;
                }
                sniper.setBrushArgument(brushName, part.getBrushArgument());
            }
            sniper.setCurrentBrush(start);
            sniper.sendMessage(this.brushSetMessage, fullBrush);
            return true;
        }
        sender.sendMessage(this.getHelpMsg());
        return false;
    }
}
