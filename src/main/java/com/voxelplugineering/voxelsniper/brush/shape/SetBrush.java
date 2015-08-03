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
import com.voxelplugineering.voxelsniper.brush.BrushInstance;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.brush.BrushWrapper;
import com.voxelplugineering.voxelsniper.brush.ExecutionResult;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.shape.csg.CuboidShape;
import com.voxelplugineering.voxelsniper.util.brush.BrushVarsHelper;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;

import com.google.common.base.Optional;

/**
 * A shape brush that allows the user to select two points and then creates a cuboidal shape
 * covering the region between the two points.
 */
@BrushInfo(name = "set", type = BrushPartType.SHAPE)
public class SetBrush implements Brush
{
    @BrushInstance
    private BrushWrapper instance;

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        Optional<Block> target = BrushVarsHelper.getTargetBlock(args);
        Optional<Location> pointA = args.get(BrushKeys.POINT_A, Location.class);
        if (!pointA.isPresent())
        {
            Location a = target.get().getLocation();
            player.sendMessage("Set first point to (" + a.getFlooredX() + ", " + a.getFlooredY() + ", " + a.getFlooredZ());
            args.set(BrushContext.of(this.instance), BrushKeys.POINT_A, a);
            return ExecutionResult.abortExecution();
        }
        Location a = pointA.get();
        Location b = target.get().getLocation();
        player.sendMessage("Set second point to (" + b.getFlooredX() + ", " + b.getFlooredY() + ", " + b.getFlooredZ());
        int w;
        int h;
        int l;
        int ox;
        int oy;
        int oz;
        if (a.getFlooredX() < b.getFlooredX())
        {
            w = b.getFlooredX() - a.getFlooredX() + 1;
            ox = w - 1;
        } else
        {
            w = a.getFlooredX() - b.getFlooredX() + 1;
            ox = 0;
        }
        if (a.getFlooredY() < b.getFlooredY())
        {
            h = b.getFlooredY() - a.getFlooredY() + 1;
            oy = h - 1;
        } else
        {
            h = a.getFlooredY() - b.getFlooredY() + 1;
            oy = 0;
        }
        if (a.getFlooredZ() < b.getFlooredZ())
        {
            l = b.getFlooredZ() - a.getFlooredZ() + 1;
            oz = l - 1;
        } else
        {
            l = a.getFlooredZ() - b.getFlooredZ() + 1;
            oz = 0;
        }
        Shape s = new CuboidShape(w, h, l, new Vector3i(ox, oy, oz));
        args.set(BrushContext.RUNTIME, BrushKeys.SHAPE, s);
        args.remove(BrushContext.of(this.instance), BrushKeys.POINT_A);
        return ExecutionResult.continueExecution();
    }

}
