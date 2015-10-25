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
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.shape.csg.EllipsoidShape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * A shape brush which defines an ellipsoid.
 */
@BrushInfo(name = "ellipsoid",
        type = BrushPartType.SHAPE,
        help = "Creates an ellipsoidal shape",
        params = { @BrushParam(name = BrushKeys.RADIUS_X,
                desc = "The x radius (floating-point number)"),
                @BrushParam(name = BrushKeys.RADIUS_Y,
                        desc = "The y radius (floating-point number)"),
                @BrushParam(name = BrushKeys.RADIUS_Z,
                        desc = "The z radius (floating-point number)") },
        permission = "voxelsniper.brush.ellipsoid")
public class EllipsoidBrush extends Brush
{

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        if (!args.has(BrushKeys.RADIUS_X))
        {
            player.sendMessage(VoxelSniperConfiguration.missingParam, BrushKeys.RADIUS_X, "ellipse");
            return ExecutionResult.abortExecution();
        }
        if (!args.has(BrushKeys.RADIUS_Y))
        {
            player.sendMessage(VoxelSniperConfiguration.missingParam, BrushKeys.RADIUS_Y, "ellipse");
            return ExecutionResult.abortExecution();
        }
        if (!args.has(BrushKeys.RADIUS_Z))
        {
            player.sendMessage(VoxelSniperConfiguration.missingParam, BrushKeys.RADIUS_Z, "ellipse");
            return ExecutionResult.abortExecution();
        }
        double rx = args.get(BrushKeys.RADIUS_X, Double.class).get();
        double ry = args.get(BrushKeys.RADIUS_Y, Double.class).get();
        double rz = args.get(BrushKeys.RADIUS_Z, Double.class).get();
        Shape s = new EllipsoidShape(rx, ry, rz, new Vector3i(rx, ry, rz));
        args.set(BrushContext.RUNTIME, BrushKeys.SHAPE, s);
        return ExecutionResult.continueExecution();
    }

}
