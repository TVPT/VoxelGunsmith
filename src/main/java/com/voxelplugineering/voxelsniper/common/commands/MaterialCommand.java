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
import com.voxelplugineering.voxelsniper.api.IMaterialRegistry;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;
import com.voxelplugineering.voxelsniper.common.command.Command;
import com.voxelplugineering.voxelsniper.common.command.CommandArgument;
import com.voxelplugineering.voxelsniper.common.command.args.StringEnumArgument;

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
     * 
     * @param materialRegistry the material registry to use for command validation
     * @param configuration the configuration object
     */
    public MaterialCommand(IMaterialRegistry<?> materialRegistry)
    {
        super("material", "Sets your current brush material");
        setAliases("v");
        Iterable<String> materialNames = materialRegistry.getRegisteredNames();
        addArgument(new StringEnumArgument("mat", true, null, materialNames));
        setPermissions("voxelsniper.command.material");
        if (Gunsmith.getConfiguration().has("MATERIAL_NOT_FOUND_MESSAGE"))
        {
            materialNotFoundMessage = Gunsmith.getConfiguration().get("MATERIAL_NOT_FOUND_MESSAGE").toString();
        }
        if (Gunsmith.getConfiguration().has("MATERIAL_SET_MESSAGE"))
        {
            materialSetMessage = Gunsmith.getConfiguration().get("MATERIAL_SET_MESSAGE").toString();
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
        Object mat = args.get("mat").getChoice();
        if (mat == null)
        {
            sniper.sendMessage(this.materialNotFoundMessage);
            return false;
        }
        materialName = mat.toString();
        Optional<?> material = sniper.getWorld().getMaterialRegistry().get(materialName);
        if (!material.isPresent())
        {
            sniper.sendMessage(this.materialNotFoundMessage);
            return false;
        }
        sniper.sendMessage(this.materialSetMessage, material.get().toString());
        sniper.getBrushSettings().set("setMaterial", material.get());
        return true;
    }

}
