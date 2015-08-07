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
package com.voxelplugineering.voxelsniper.brush.shape;

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
import com.voxelplugineering.voxelsniper.shape.csg.CylinderShape;
import com.voxelplugineering.voxelsniper.util.Direction;
import com.voxelplugineering.voxelsniper.util.math.Maths;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

import com.google.common.base.Optional;

/**
 * A shape brush which defines a cylindrical region.
 */
@BrushInfo(name = "ring", type = BrushPartType.SHAPE, help = "Creates a ring shape", params = {
        @BrushParam(name = BrushKeys.USE_FACE, desc = "Whether to align shape to selected block face (true/false)"),
        @BrushParam(name = BrushKeys.INNER_RADIUS, desc = "The inner radius of the ring (floating-point number)"),
        @BrushParam(name = BrushKeys.HEIGHT, desc = "The height of the ring (number)") })
public class RingBrush implements Brush
{

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        double size = args.get(BrushKeys.BRUSH_SIZE, Double.class).get();
        Optional<Double> oheight = args.get(BrushKeys.HEIGHT, Double.class);
        int height = 1;
        if (oheight.isPresent())
        {
            height = (int) Math.floor(oheight.get());
        }
        Optional<Double> oir = args.get(BrushKeys.INNER_RADIUS, Double.class);
        if (!oir.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingParam, BrushKeys.INNER_RADIUS, "ring");
            return ExecutionResult.abortExecution();
        }
        double ir = oir.get();
        boolean face = args.get(BrushKeys.USE_FACE, Boolean.class).or(VoxelSniperConfiguration.cylDefaultFace);
        ComplexShape s = null;
        if (face)
        {
            Direction d = args.get(BrushKeys.TARGET_FACE, Direction.class).or(Direction.UP);
            switch (d)
            {
            case NORTH:
            case SOUTH:
            {
                CylinderShape outer = new CylinderShape(size, height, size, new Vector3i(size, size, 0), Direction.SOUTH);
                CylinderShape inner = new CylinderShape(ir, height, ir, new Vector3i(ir, ir, 0), Direction.SOUTH);
                s = new ComplexShape(outer);
                for (int x = -Maths.ceil(ir); x < Maths.ceil(ir); x++)
                {
                    for (int y = -Maths.ceil(ir); y < Maths.ceil(ir); y++)
                    {
                        for (int z = 0; z < inner.getHeight(); z++)
                        {
                            if (inner.get(x, y, z, true))
                            {
                                s.unset(x, y, z, true);
                            }
                        }
                    }
                }
                break;
            }
            case EAST:
            case WEST:
            {
                CylinderShape outer = new CylinderShape(size, height, size, new Vector3i(0, size, size), Direction.EAST);
                CylinderShape inner = new CylinderShape(ir, height, ir, new Vector3i(0, ir, ir), Direction.EAST);
                s = new ComplexShape(outer);
                for (int y = -Maths.ceil(ir); y < Maths.ceil(ir); y++)
                {
                    for (int z = -Maths.ceil(ir); z < Maths.ceil(ir); z++)
                    {
                        for (int x = 0; x < inner.getHeight(); x++)
                        {
                            if (inner.get(x, y, z, true))
                            {
                                s.unset(x, y, z, true);
                            }
                        }
                    }
                }
                break;
            }
            default:
            {
                CylinderShape outer = new CylinderShape(size, height, size, new Vector3i(size, 0, size));
                CylinderShape inner = new CylinderShape(ir, height, ir, new Vector3i(ir, 0, ir));
                s = new ComplexShape(outer);
                for (int x = -Maths.ceil(ir); x < Maths.ceil(ir); x++)
                {
                    for (int z = -Maths.ceil(ir); z < Maths.ceil(ir); z++)
                    {
                        for (int y = 0; y < inner.getHeight(); y++)
                        {
                            if (inner.get(x, y, z, true))
                            {
                                s.unset(x, y, z, true);
                            }
                        }
                    }
                }
                break;
            }
            }
        } else
        {
            CylinderShape outer = new CylinderShape(size, height, size, new Vector3i(size, 0, size));
            CylinderShape inner = new CylinderShape(ir, height, ir, new Vector3i(ir, 0, ir));
            s = new ComplexShape(outer);
            for (int x = -Maths.ceil(ir); x < Maths.ceil(ir); x++)
            {
                for (int z = -Maths.ceil(ir); z < Maths.ceil(ir); z++)
                {
                    for (int y = 0; y < inner.getHeight(); y++)
                    {
                        if (inner.get(x, y, z, true))
                        {
                            s.unset(x, y, z, true);
                        }
                    }
                }
            }
        }
        args.set(BrushContext.RUNTIME, BrushKeys.SHAPE, s);
        return ExecutionResult.continueExecution();
    }

}
