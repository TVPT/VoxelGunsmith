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

import com.google.common.base.Optional;

import java.util.Random;

@BrushInfo(name = "random", type = BrushPartType.MASK, help = "Randomly removes points from your shape based on a parameterized chance", params = {
        @BrushParam(name = BrushKeys.RANDOM_CHANCE, desc = "The change for points to stay (floating-point number)") }, permission = "voxelsniper.brush.random")
public class RandomMaskBrush extends Brush
{

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingShape, "random");
            return ExecutionResult.abortExecution();
        }
        Optional<Double> ochance = args.get(BrushKeys.RANDOM_CHANCE, Double.class);
        double chance = VoxelSniperConfiguration.randomDefaultChance;
        if (ochance.isPresent())
        {
            chance = ochance.get();
        }
        ComplexShape shape;
        if (s.get() instanceof ComplexShape)
        {
            shape = (ComplexShape) s.get();
        } else
        {
            shape = new ComplexShape(s.get());
        }
        Random rand = new Random();
        for (int x = 0; x < shape.getWidth(); x++)
        {
            for (int y = 0; y < shape.getHeight(); y++)
            {
                for (int z = 0; z < shape.getLength(); z++)
                {
                    if (shape.get(x, y, z, false))
                    {
                        if (rand.nextDouble() > chance)
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
