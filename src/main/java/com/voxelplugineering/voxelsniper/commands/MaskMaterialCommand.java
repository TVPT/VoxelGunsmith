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
import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.material.Material;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class MaskMaterialCommand extends Command
{

    /**
     * The message sent to players if their chosen material is not found within the registry.
     */
    private String materialNotFoundMessage;
    /**
     * The message sent to the player when their material is set.
     */
    private String materialSetMessage;

    /**
     * Constructs a new {@link MaskMaterialCommand}.
     * 
     * @param context The context
     */
    public MaskMaterialCommand(Context context)
    {
        super("maskmaterial", "Sets your current secondary brush material", context);
        setAliases("vr");
        setPermissions("voxelsniper.command.materialmask");
        this.materialNotFoundMessage = getConfig().get("materialNotFoundMessage", String.class).or("Could not find that material.");
        this.materialSetMessage = getConfig().get("materialMaskSetMessage", String.class).or("Set secondary material to %s");
    }

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
        String materialName = "air";
        if (args.length >= 1)
        {
            materialName = args[0];
            if (sniper.getAliasHandler().hasTarget("material"))
            {
                materialName = sniper.getAliasHandler().getRegistry("material").get().expand(materialName);
            }
            Optional<Material> material = sniper.getWorld().getMaterialRegistry().getMaterial(materialName);
            if (!material.isPresent())
            {
                sniper.sendMessage(this.materialNotFoundMessage);
                return false;
            }
            sniper.sendMessage(this.materialSetMessage, material.get().getName());
            sniper.getBrushVars().set(BrushContext.GLOBAL, BrushKeys.MASK_MATERIAL, material.get());
        } else
        {
            Optional<Block> target = sniper.getTargetBlock();
            if (target.isPresent())
            {
                sniper.sendMessage(this.materialSetMessage, target.get().getMaterial().getName());
                sniper.getBrushVars().set(BrushContext.GLOBAL, BrushKeys.MASK_MATERIAL, target.get().getMaterial());
            } else
            {
                sniper.sendMessage("Could not fetch your target block.");
            }
        }
        return true;
    }

}
