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
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.ComplexShape;
import com.voxelplugineering.voxelsniper.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.shape.SingleMaterialShape;
import com.voxelplugineering.voxelsniper.util.brush.BrushVarsHelper;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;
import com.voxelplugineering.voxelsniper.world.queue.ShapeChangeQueue;

import java.util.Optional;

/**
 * An effect brush which drains all liquids from the current shape.
 */
@BrushInfo(name = "drain", type = BrushPartType.EFFECT, help = "An effect brush which removes all liquids from within your shape", permission = "voxelsniper.brush.drain")
public class DrainBrush extends Brush
{

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingShape, "material");
            return ExecutionResult.abortExecution();
        }
        Optional<MaterialState> m = args.get(BrushKeys.AIR_MATERIAL, MaterialState.class);
        if (!m.isPresent())
        {
            m = args.get(BrushKeys.MATERIAL, MaterialState.class);
            if (!m.isPresent())
            {
                player.sendMessage(VoxelSniperConfiguration.missingMaterial);
                return ExecutionResult.abortExecution();
            }
        }
        Optional<Block> l = BrushVarsHelper.getTargetBlock(args);
        Location loc = l.get().getLocation();
        ComplexShape shape;
        if (s.get() instanceof ComplexShape)
        {
            shape = (ComplexShape) s.get();
        } else
        {
            shape = new ComplexShape(s.get());
        }

        for (int x = 0; x < shape.getWidth(); x++)
        {
            int x0 = loc.getFlooredX() + x - shape.getOrigin().getX();
            for (int y = 0; y < shape.getHeight(); y++)
            {
                int y0 = loc.getFlooredY() + y - shape.getOrigin().getY();
                for (int z = 0; z < shape.getLength(); z++)
                {
                    int z0 = loc.getFlooredZ() + z - shape.getOrigin().getZ();
                    if (!shape.get(x, y, z, false))
                    {
                        continue;
                    }
                    if (!player.getWorld().getBlock(x0, y0, z0).get().getMaterial().getType().isLiquid())
                    {
                        shape.unset(x, y, z, false);
                    }
                }
            }
        }
        MaterialShape ms = new SingleMaterialShape(shape, m.get());
        new ShapeChangeQueue(player, loc, ms).flush();
        return ExecutionResult.continueExecution();
    }

}
