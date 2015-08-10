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
import com.voxelplugineering.voxelsniper.brush.BrushParam;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.brush.ExecutionResult;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.ComplexShape;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.util.math.Maths;

import com.google.common.base.Optional;

import java.util.Random;

@BrushInfo(name = "splatter", type = BrushPartType.MASK, help = SplatterMaskBrush.HELP, params = {
        @BrushParam(name = BrushKeys.SEED_PERCENT, desc = "The seed percent (floating-point number)"),
        @BrushParam(name = BrushKeys.GROW_PERCENT, desc = "The growth percent (floating-point number)"),
        @BrushParam(name = BrushKeys.RECURSIONS, desc = "The number of growth passes (number)") }, permission = "voxelsniper.brush.splatter")
public class SplatterMaskBrush extends Brush
{

    protected static final String HELP = "Randomly seeds your shape and then grows outwards from the seeds based on the number of recursions";

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage("You must have at least one shape brush before your splatter brush.");
            return ExecutionResult.abortExecution();
        }
        double seed = args.get(BrushKeys.SEED_PERCENT, Double.class).or(VoxelSniperConfiguration.splatterDefaultSeed);
        seed = Maths.clamp(seed, 0, 1);
        double growth = args.get(BrushKeys.GROW_PERCENT, Double.class).or(VoxelSniperConfiguration.splatterDefaultGrowth);
        growth = Maths.clamp(growth, 0, 1);
        int recursions = args.get(BrushKeys.RECURSIONS, Integer.class).or(VoxelSniperConfiguration.splatterDefaultRecursions);
        recursions = Maths.clamp(recursions, 1, VoxelSniperConfiguration.splatterMaxRecursions);
        Shape base = s.get();
        ComplexShape shape = new ComplexShape(base);

        Random rand = new Random();

        for (int x = 0; x < shape.getWidth(); x++)
        {
            for (int y = 0; y < shape.getHeight(); y++)
            {
                for (int z = 0; z < shape.getLength(); z++)
                {
                    if (base.get(x, y, z, false))
                    {
                        if (rand.nextDouble() < seed)
                        {
                            shape.set(x, y, z, false);
                        } else
                        {
                            shape.unset(x, y, z, false);
                        }
                    }
                }
            }
        }

        int growths = 0;
        ComplexShape splat = new ComplexShape(shape);

        for (int r = 0; r < recursions; r++)
        {
            double grow = growth - ((growth / recursions) * r);
            splat.fillFrom(shape);
            for (int x = 0; x < shape.getWidth(); x++)
            {
                for (int y = 0; y < shape.getHeight(); y++)
                {
                    for (int z = 0; z < shape.getLength(); z++)
                    {
                        if (!base.get(x, y, z, false))
                        {
                            continue;
                        }

                        growths = 0;
                        if (!shape.get(x, y, z, false))
                        {
                            if (x != 0 && shape.get(x - 1, y, z, false))
                            {
                                growths++;
                            }
                            if (y != 0 && shape.get(x, y - 1, z, false))
                            {
                                growths++;
                            }
                            if (z != 0 && shape.get(x, y, z - 1, false))
                            {
                                growths++;
                            }
                            if (x != shape.getWidth() - 1 && shape.get(x + 1, y, z, false))
                            {
                                growths++;
                            }
                            if (y != shape.getHeight() - 1 && shape.get(x, y + 1, z, false))
                            {
                                growths++;
                            }
                            if (z != shape.getLength() - 1 && shape.get(x, y, z + 1, false))
                            {
                                growths++;
                            }

                            if (growths >= 1 && rand.nextDouble() < grow)
                            {
                                splat.set(x, y, z, false);
                            }
                        }
                    }
                }
            }
            shape.fillFrom(splat);
        }

        args.set(BrushContext.RUNTIME, BrushKeys.SHAPE, shape);
        return ExecutionResult.continueExecution();
    }

}
