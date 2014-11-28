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

import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;
import com.voxelplugineering.voxelsniper.common.command.Command;
import com.voxelplugineering.voxelsniper.common.command.CommandArgument;
import com.voxelplugineering.voxelsniper.common.command.args.RawArgument;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class MaterialCommand extends Command
{

    /**
     * Constructs a new BrushCommand
     */
    public MaterialCommand()
    {
        super("material", "Sets your current brush material");
        setAliases("v");
        addArgument(new RawArgument("raw"));
        setPermissions("voxelsniper.command.material");
    }

    @Override
    public boolean execute(ISniper sniper, Map<String, CommandArgument<?>> args)
    {
        checkNotNull(sniper, "Cannot have a null sniper!");
        String[] s = ((RawArgument) args.get("raw")).getChoice();
        if (s.length >= 1)
        {
            String materialName = s[0];
            CommonMaterial<?> material = Gunsmith.getMaterialFactory().getMaterial(materialName);
            if (material == null)
            {
                sniper.sendMessage("Could not find that material.");
                return false;
            }
            sniper.sendMessage("Set material to " + material.toString());
            sniper.getBrushSettings().set("setMaterial", material);
            return true;
        }
        return false;
    }

}
