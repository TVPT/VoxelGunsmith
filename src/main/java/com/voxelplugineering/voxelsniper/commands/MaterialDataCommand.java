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
import com.voxelplugineering.voxelsniper.util.RayTrace;
import com.voxelplugineering.voxelsniper.util.math.Vector3d;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.material.DataSnapshot;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class MaterialDataCommand extends Command
{

    private String materialSetMessage;

    /**
     * Constructs a new {@link MaterialDataCommand}.
     * 
     * @param context The context
     */
    public MaterialDataCommand(Context context)
    {
        super("materialdata", "Sets your current material data", context);
        setAliases("vi");
        setPermissions("voxelsniper.command.materialdata");
        this.materialSetMessage = getConfig().get("materialDataSetMessage", String.class).or("Set material data to %s");
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
        if (args.length >= 1)
        {
            if ("clear".equalsIgnoreCase(args[0]))
            {
                sniper.getBrushVars().remove(BrushContext.GLOBAL, BrushKeys.MATERIALDATA);
            }
        }
        Location location = sniper.getLocation();
        double yaw = sniper.getRotation().getX();
        double pitch = sniper.getRotation().getY();
        // TODO move min/max values form conf to world
        int minY = getConfig().get("minimumWorldDepth", int.class).or(0);
        int maxY = getConfig().get("maximumWorldHeight", int.class).or(255);
        double step = getConfig().get("rayTraceStep", double.class).or(0.2);
        Vector3d eyeOffs = new Vector3d(0, getConfig().get("playerEyeHeight", double.class).or(1.62), 0);
        double rayTraceRange = getConfig().get("rayTraceRange", Double.class).or(250.0);
        RayTrace ray = new RayTrace(location, yaw, pitch, rayTraceRange, minY, maxY, step, eyeOffs);
        ray.trace();
        if (ray.getTargetBlock() != null)
        {
            Optional<DataSnapshot> data = ray.getTargetBlock().getDataShapshot();
            if (!data.isPresent())
            {
                sender.sendMessage("Failed to get data from target block");
            }
            sniper.getBrushVars().set(BrushContext.GLOBAL, BrushKeys.MATERIALDATA, data.get());
        } else
        {
            sender.sendMessage("Invalid target block");
        }
        return true;
    }

}
