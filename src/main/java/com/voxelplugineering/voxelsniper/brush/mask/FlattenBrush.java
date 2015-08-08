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
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.ComplexShape;
import com.voxelplugineering.voxelsniper.shape.Shape;

import com.google.common.base.Optional;

@BrushInfo(name = "flatten", type = BrushPartType.MASK, help = "Flattens your shape into a 2d shape")
public class FlattenBrush extends Brush
{

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingShape, "flatten");
            return ExecutionResult.abortExecution();
        }
        Shape base = s.get();
        ComplexShape shape = new ComplexShape(base.getWidth(), 1, base.getLength(), base.getOrigin().getX(), 0, base.getOrigin().getZ());

        for (int x = 0; x < base.getWidth(); x++)
        {
            for (int z = 0; z < base.getLength(); z++)
            {
                for (int y = 0; y < base.getHeight(); y++)
                {
                    if (base.get(x, y, z, false))
                    {
                        shape.set(x, 0, z, false);
                        break;
                    }
                }
            }
        }

        args.set(BrushContext.RUNTIME, BrushKeys.SHAPE, shape);
        return ExecutionResult.continueExecution();
    }

}
