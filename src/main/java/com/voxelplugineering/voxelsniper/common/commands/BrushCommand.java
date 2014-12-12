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
package com.voxelplugineering.voxelsniper.common.commands;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.common.command.Command;
import com.voxelplugineering.voxelsniper.common.command.CommandArgument;
import com.voxelplugineering.voxelsniper.common.command.args.RawArgument;
import com.voxelplugineering.voxelsniper.util.Utilities;

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
        setAliases("b", "brush");
        addArgument(new RawArgument("raw"));
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
    public boolean execute(ISniper sniper, Map<String, CommandArgument<?>> args)
    {
        checkNotNull(sniper, "Cannot have a null sniper!");
        String[] s = ((RawArgument) args.get("raw")).getChoice();
        if (s.length == 1)
        {
            try
            {
                double brushSize = Double.parseDouble(s[0]);
                sniper.getBrushSettings().set("brushSize", brushSize);
                sniper.sendMessage(this.brushSizeChangeMessage, brushSize);
                return true;
            } catch (NumberFormatException ignored)
            {
                assert true; //lol checkstyle warnings ;)
            }
        }
        if (s.length >= 1)
        {

            Optional<AliasRegistry> alias = sniper.getPersonalAliasHandler().getRegistry("brush");
            String fullBrush = Utilities.getSection(s, 0, s.length - 1);
            if (alias.isPresent())
            {
                fullBrush = alias.get().expand(fullBrush);
            }
            IBrush start = null;
            IBrush last = null;
            for (String brushName : fullBrush.split(" "))
            {

                IBrush brush = sniper.getPersonalBrushManager().getNewBrushInstance(brushName).orNull();
                if (brush == null)
                {
                    sniper.getPersonalBrushManager().loadBrush(brushName);
                    brush = sniper.getPersonalBrushManager().getNewBrushInstance(brushName).orNull();
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

            }
            sniper.setCurrentBrush(start);
            sniper.sendMessage(this.brushSetMessage, fullBrush);
            return true;
        }
        return false;
    }

}
