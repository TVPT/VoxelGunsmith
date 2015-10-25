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
package com.voxelplugineering.voxelsniper.brush.effect;

import com.voxelplugineering.voxelsniper.brush.Brush;
import com.voxelplugineering.voxelsniper.brush.BrushInfo;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.brush.ExecutionResult;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.service.text.TextFormat;
import com.voxelplugineering.voxelsniper.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.shape.SingleMaterialShape;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.brush.BrushVarsHelper;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.material.Material;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;
import com.voxelplugineering.voxelsniper.world.queue.ShapeChangeQueue;

import java.util.Optional;

/**
 * An effect brush that removes snow layers that are on top of snow layers.
 */
@BrushInfo(name = "cleansnow",
        type = BrushPartType.EFFECT,
        help = "Removes floating snow blocks",
        permission = "voxelsniper.brush.cleansnow")
public class CleanSnowBrush extends Brush
{

    private static final String[] SNOW_NAMES = new String[] { "snow_layer", "snow" };

    private MaterialState air;
    private Material snowLayer;

    @Override
    public void init(Context context)
    {
        MaterialRegistry<?> registry = context.getRequired(MaterialRegistry.class);
        this.air = registry.getAirMaterial().getDefaultState();
        for (String n : SNOW_NAMES)
        {
            Optional<Material> attempt = registry.getMaterial(n);
            if (attempt.isPresent())
            {
                this.snowLayer = attempt.get();
                break;
            }
        }
    }

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        if (this.snowLayer == null)
        {
            player.sendMessage(TextFormat.DARK_RED + "Failed to find snow materials, brush will not funtion.");
            return ExecutionResult.abortExecution();
        }
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage("You must have at least one shape brush before your material brush.");
            return ExecutionResult.abortExecution();
        }
        Optional<Block> l = BrushVarsHelper.getTargetBlock(args);
        Location loc = l.get().getLocation();
        MaterialShape ms = new SingleMaterialShape(s.get(), this.air);
        for (int x = 0; x < ms.getWidth(); x++)
        {
            int x0 = loc.getFlooredX() + x - ms.getOrigin().getX();
            for (int z = 0; z < ms.getLength(); z++)
            {
                int z0 = loc.getFlooredZ() + z - ms.getOrigin().getZ();
                for (int y = ms.getHeight() - 1; y > 0; y--)
                {
                    int y0 = loc.getFlooredY() + y - ms.getOrigin().getY();
                    if (!ms.get(x, y, z, false))
                    {
                        continue;
                    }
                    Optional<Block> oblock = player.getWorld().getBlock(x0, y0, z0);
                    if (!oblock.isPresent())
                    {
                        ms.unset(x, y, z, false);
                        continue;
                    }
                    MaterialState mat = oblock.get().getMaterial();
                    if (!mat.getType().equals(this.snowLayer))
                    {
                        ms.unset(x, y, z, false);
                        continue;
                    }
                    Optional<Block> oblock1 = player.getWorld().getBlock(x0, y0 - 1, z0);
                    if (!oblock1.isPresent())
                    {
                        ms.unset(x, y, z, false);
                        continue;
                    }
                    MaterialState mat1 = oblock1.get().getMaterial();
                    if (!mat1.getType().equals(this.snowLayer))
                    {
                        ms.unset(x, y, z, false);
                    }
                }
            }
        }
        new ShapeChangeQueue(player, l.get().getLocation(), ms).flush();
        return ExecutionResult.continueExecution();
    }
}
