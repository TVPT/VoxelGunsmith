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

import com.voxelplugineering.voxelsniper.brush.BrushChain;
import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushWrapper;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.StringUtilities;

import com.google.common.base.Optional;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class BrushCommand extends Command
{

    /**
     * Constructs a new BrushCommand.
     * 
     * @param context The context
     */
    public BrushCommand(Context context)
    {
        super("brush", "Sets your current brush", context);
        setAliases("b");
        setPermissions("voxelsniper.command.brush");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        // TODO reduce NPath complexity
        checkNotNull(sender, "Cannot have a null sniper!");

        if (!sender.isPlayer())
        {
            sender.sendMessage(VoxelSniperConfiguration.commandPlayerOnly);
            return true;
        }
        Player sniper = (Player) sender;
        if (!getPerms().hasPermission(sniper, "voxelsniper.command.brush"))
        {
            sender.sendMessage(VoxelSniperConfiguration.permissionsRequiredMessage);
            return true;
        }
        if (args.length == 0) {
            String full = "";
            for (BrushWrapper b : sniper.getCurrentBrush().getBrushes())
            {
                full += b.getName() + " ";
            }
            sniper.sendMessage(VoxelSniperConfiguration.brushSetMessage, full.trim());
            return true;
        }
        if (args.length == 1)
        {
            try
            {
                double brushSize = Double.parseDouble(args[0]);
                sniper.getBrushVars().setContext(BrushContext.GLOBAL);
                sniper.getBrushVars().set(BrushContext.GLOBAL, BrushKeys.BRUSH_SIZE, brushSize);
                sniper.sendMessage(VoxelSniperConfiguration.brushSizeChangedMessage, brushSize);
                return true;
            } catch (NumberFormatException ignored)
            {
                assert true;
            }
        }
        if (args.length >= 1)
        {
            String fullBrush = StringUtilities.getSection(args, 0, args.length - 1);
            fullBrush = sniper.getAliasHandler().getRegistry("brush").get().expand(fullBrush);
            BrushChain brush = new BrushChain(fullBrush);
            for (String b : fullBrush.split(" "))
            {
                Optional<BrushWrapper> br = sniper.getBrushManager().getBrush(b);
                if (br.isPresent())
                {
                    brush.chain(br.get());
                } else
                {
                    sniper.sendMessage(VoxelSniperConfiguration.brushNotFoundMessage, b);
                    return true;
                }
            }
            sniper.setCurrentBrush(brush);
            sniper.getBrushVars().clearFlags();
            sniper.sendMessage(VoxelSniperConfiguration.brushSetMessage, brush.getName());

            boolean hasShape = false;
            boolean hasEffect = false;

            for (BrushWrapper b : brush.getBrushes())
            {
                if (b.getType() == BrushPartType.SHAPE)
                {
                    hasShape = true;
                } else if (b.getType() == BrushPartType.EFFECT)
                {
                    hasEffect = true;
                    if (!hasShape)
                    {
                        sniper.sendMessage(VoxelSniperConfiguration.effectBeforeShape);
                        return true;
                    }
                }
            }
            if (hasShape && !hasEffect)
            {
                sniper.sendMessage(VoxelSniperConfiguration.shapeWithoutEffect1);
                sniper.sendMessage(VoxelSniperConfiguration.shapeWithoutEffect2, fullBrush);
                brush.chain(sniper.getBrushManager().getBrush("material").get());
            }

            return true;
        }
        sender.sendMessage(this.getHelpMsg());
        return false;
    }
}
