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
package com.voxelplugineering.voxelsniper.brush.mask;

import com.voxelplugineering.voxelsniper.brush.Brush;
import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushInfo;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.brush.ExecutionResult;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.ComplexShape;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.util.brush.BrushVarsHelper;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;

import com.google.common.base.Optional;

/**
 * A brush mask which unsets all positions in the shape which are not currently set to the mask
 * material.
 */
@BrushInfo(name = "materialmask", type = BrushPartType.MASK)
public class MaterialMaskBrush implements Brush
{

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage("You must have at least one shape brush before your material mask brush.");
            return ExecutionResult.abortExecution();
        }
        Optional<MaterialState> m = args.get(BrushKeys.MASK_MATERIAL, MaterialState.class);
        if (!m.isPresent())
        {
            player.sendMessage("You must select a secondary material.");
            return ExecutionResult.abortExecution();
        }
        boolean wildcard = args.get(BrushKeys.MASK_MATERIAL_WILDCARD, boolean.class).or(false);
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
                    if (wildcard)
                    {
                        if (!m.get().getType().equals(player.getWorld().getBlock(x0, y0, z0).get().getMaterial().getType()))
                        {
                            shape.unset(x, y, z, false);
                        }
                    } else
                    {
                        if (!m.get().equals(player.getWorld().getBlock(x0, y0, z0).get().getMaterial()))
                        {
                            shape.unset(x, y, z, false);
                        }
                    }
                }
            }
        }

        args.set(BrushContext.RUNTIME, BrushKeys.SHAPE, shape);
        return ExecutionResult.continueExecution();
    }

}
