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
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.command.Command;
import com.voxelplugineering.voxelsniper.common.command.CommandArgument;
import com.voxelplugineering.voxelsniper.common.command.args.RawArgument;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class MaskMaterialCommand extends Command
{
    /**
     * The message sent to players if their chosen material is not found within the registry.
     */
    private String materialNotFoundMessage = "Could not find that material.";
    /**
     * The message sent to the player when their material is set.
     */
    private String materialSetMessage = "Set secondary material to %s";

    /**
     * Constructs a new BrushCommand
     */
    public MaskMaterialCommand()
    {
        super("maskmaterial", "Sets your current secondary brush material");
        setAliases("vr");
        addArgument(new RawArgument("raw"));
        setPermissions("voxelsniper.command.materialmask");
        if (Gunsmith.getConfiguration().has("materialNotFoundMessage"))
        {
            materialNotFoundMessage = Gunsmith.getConfiguration().get("materialNotFoundMessage").get().toString();
        }
        if (Gunsmith.getConfiguration().has("materialMaskSetMessage"))
        {
            materialSetMessage = Gunsmith.getConfiguration().get("materialMaskSetMessage").get().toString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(ISniper sniper, Map<String, CommandArgument<?>> args)
    {
        checkNotNull(sniper, "Cannot have a null sniper!");
        String materialName = "air";
        String[] s = ((RawArgument) args.get("raw")).getChoice();
        if (s.length >= 1)
        {
            materialName = s[0];
            Optional<?> material = sniper.getWorld().getMaterialRegistry().get(materialName);
            if (!material.isPresent())
            {
                sniper.sendMessage(this.materialNotFoundMessage);
                return false;
            }
            sniper.sendMessage(this.materialSetMessage, material.get().toString());
            sniper.getBrushSettings().set("maskMaterial", material.get());
        } else
        {
            sniper.sendMessage(this.getHelpMsg());
        }
        return true;
    }

}
