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

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.brush.Brush;
import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushInfo;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushParam;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.brush.ExecutionResult;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.ComplexShape;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.util.brush.BrushVarsHelper;
import com.voxelplugineering.voxelsniper.util.math.Maths;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;

import com.google.common.base.Optional;

@BrushInfo(name = "checker", type = BrushPartType.MASK, params = {
        @BrushParam(name = BrushKeys.WIDTH, desc = "The X-axis size of the tiles (number)"),
        @BrushParam(name = BrushKeys.HEIGHT, desc = "The Y-axis size of the tiles (number)"),
        @BrushParam(name = BrushKeys.LENGTH, desc = "The Z-axis size of the tiles (number)"),
        @BrushParam(name = BrushKeys.OFFSET_X, desc = "The X offset of the time edge from zero (number)"),
        @BrushParam(name = BrushKeys.OFFSET_Y, desc = "The Y offset of the time edge from zero (number)"),
        @BrushParam(name = BrushKeys.OFFSET_Z, desc = "The Z offset of the time edge from zero (number)") })
public class CheckerMaskBrush implements Brush
{

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingShape, "materialmask");
            return ExecutionResult.abortExecution();
        }
        int xoffset = args.get(BrushKeys.OFFSET_X, Integer.class).or(0);
        int yoffset = args.get(BrushKeys.OFFSET_Y, Integer.class).or(0);
        int zoffset = args.get(BrushKeys.OFFSET_Z, Integer.class).or(0);
        int width = args.get(BrushKeys.WIDTH, Integer.class).or(1);
        int height = args.get(BrushKeys.HEIGHT, Integer.class).or(1);
        int length = args.get(BrushKeys.LENGTH, Integer.class).or(1);
        width = Maths.clamp(width, 1, Integer.MAX_VALUE);
        height = Maths.clamp(height, 1, Integer.MAX_VALUE);
        length = Maths.clamp(length, 1, Integer.MAX_VALUE);
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
            int x0 = (loc.getFlooredX() + x - shape.getOrigin().getX() + xoffset) / width;
            for (int y = 0; y < shape.getHeight(); y++)
            {
                int y0 = (loc.getFlooredY() + y - shape.getOrigin().getY() + yoffset) / height;
                for (int z = 0; z < shape.getLength(); z++)
                {
                    int z0 = (loc.getFlooredZ() + z - shape.getOrigin().getZ() + zoffset) / length;
                    if (!shape.get(x, y, z, false))
                    {
                        continue;
                    }
                    if (x0 % 2 != 0 || y0 % 2 != 0 || z0 % 2 != 0)
                    {
                        shape.unset(x, y, z, false);
                    }
                }
            }
        }

        args.set(BrushContext.RUNTIME, BrushKeys.SHAPE, shape);
        return ExecutionResult.continueExecution();
    }

}
