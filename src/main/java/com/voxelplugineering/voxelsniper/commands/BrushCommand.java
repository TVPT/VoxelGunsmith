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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.api.commands.CommandSender;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormat;
import com.voxelplugineering.voxelsniper.brushes.BrushNodeGraph;
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
            if (!validate(fullBrush))
            {
                sender.sendMessage(TextFormat.DARK_RED + "Sorry, your brush contained unbalanced or embedded braces.");
            }
            if (alias.isPresent())
            {
                fullBrush = alias.get().expand(fullBrush);
            }
            BrushNodeGraph start = null;
            BrushNodeGraph last = null;
            Pattern pattern = Pattern.compile("([\\S&&[^\\{]]+)[\\s]*(?:((?:\\{[^\\}]*\\}[\\s]*)+))?");
            Matcher match = pattern.matcher(prep(fullBrush));
            while (match.find())
            {
                String brushName = match.group(1);
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
                sniper.setBrushArgument(brushName, normalize(match.group(2)));
            }
            sniper.setCurrentBrush(start);
            sniper.sendMessage(this.brushSetMessage, fullBrush);
            return true;
        }
        return false;
    }

    private boolean validate(String fullBrush)
    {
        int co = 0;
        for (char c : fullBrush.toCharArray())
        {
            if (c == '{')
            {
                if (co > 0)
                {
                    return false;
                }
                co++;
            }
            if (c == '}')
            {
                if (co < 0)
                {
                    return false;
                }
                co--;
            }
        }
        return co == 0;
    }

    private String normalize(String s)
    {
        if (s == null)
        {
            return null;
        }
        Pattern p = Pattern.compile("(\\{[^\\}]*\\})[\\s]*");
        Matcher match = p.matcher(s);
        String f = "";
        while (match.find())
        {
            String m = match.group(1);
            System.out.println("match: " + m);
            m = m.trim().replace(" ", ",");
            m = m.replace("{,", "{");
            m = m.replace(",}", "}");
            while (m.contains(",,"))
            {
                m = m.replace(",,", ",");
            }
            f += m + " ";
        }
        f = f.replaceAll("\\}[\\s]*\\{", ",");
        f = f.trim();
        return f;
    }

    private String prep(String s)
    {
        s = s.trim();
        while (s.startsWith("{"))
        {
            int index = s.indexOf("}");
            if (index == -1)
            {
                s = s.substring(1);
            }
            s = s.substring(index + 1).trim();
        }
        return s;
    }
}
