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
import com.voxelplugineering.voxelsniper.command.Command;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class MaterialCommand extends Command
{
    /**
     * The message sent to the player if their chosen material is not found in the registry.
     */
    private String materialNotFoundMessage = "Could not find that material.";
    /**
     * The message sent to the player when their material is set.
     */
    private String materialSetMessage = "Set material to %s";

    /**
     * Constructs a new BrushCommand
     */
    public MaterialCommand()
    {
        super("material", "Sets your current brush material");
        setAliases("v");
        setPermissions("voxelsniper.command.material");
        if (Gunsmith.getConfiguration().has("materialNotFoundMessage"))
        {
            this.materialNotFoundMessage = Gunsmith.getConfiguration().get("materialNotFoundMessage").get().toString();
        }
        if (Gunsmith.getConfiguration().has("materialSetMessage"))
        {
            this.materialSetMessage = Gunsmith.getConfiguration().get("materialSetMessage").get().toString();
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
        String materialName = "air";
        if (args.length >= 1)
        {
            materialName = args[0];
            Optional<?> material = sniper.getWorld().getMaterialRegistry().getMaterial(materialName);
            if (!material.isPresent())
            {
                sniper.sendMessage(this.materialNotFoundMessage);
                return false;
            }
            sniper.sendMessage(this.materialSetMessage, material.get().toString());
            sniper.getBrushSettings().set("setMaterial", material.get());
        } else
        {
            sniper.sendMessage(this.getHelpMsg());
        }
        return true;
    }

}