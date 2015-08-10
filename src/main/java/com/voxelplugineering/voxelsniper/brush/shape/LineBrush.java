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
import com.voxelplugineering.voxelsniper.brush.BrushAction;
import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushInfo;
import com.voxelplugineering.voxelsniper.brush.BrushInstance;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.brush.BrushWrapper;
import com.voxelplugineering.voxelsniper.brush.ExecutionResult;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.ComplexShape;
import com.voxelplugineering.voxelsniper.util.RayTrace;
import com.voxelplugineering.voxelsniper.util.math.Vector3d;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;

import com.google.common.base.Function;
import com.google.common.base.Optional;

/**
 * Defines a region which is a line between the point specified by the alternate action to the
 * target block.
 */
@BrushInfo(name = "line", type = BrushPartType.SHAPE, help = LineBrush.HELP, permission = "voxelsniper.brush.line")
public class LineBrush extends Brush
{

    protected static final String HELP = "Creates a straight line from the point selected with your primary action to the point(s) selected "
            + "with your secondary action";

    @BrushInstance
    private BrushWrapper instance;

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        BrushAction action = args.get(BrushKeys.ACTION, BrushAction.class).get();
        Optional<Block> target = args.get(BrushKeys.TARGET_BLOCK, Block.class);
        if (action == BrushAction.PRIMARY)
        {
            Location a = target.get().getLocation();
            player.sendMessage(VoxelSniperConfiguration.setFirstPoint, a.getFlooredX(), a.getFlooredY(), a.getFlooredZ());
            args.set(BrushContext.of(this.instance), BrushKeys.POINT_A, a);
            return ExecutionResult.delayExecution(this.instance);
        }
        // if its not the primary action we need to create the line
        Optional<Location> pointA = args.get(BrushKeys.POINT_A, Location.class);
        if (!pointA.isPresent())
        {
            player.sendMessage("You must select a starting point first with the primary action.");
            return ExecutionResult.abortExecution();
        }
        Location a = pointA.get();
        Location b = target.get().getLocation();
        player.sendMessage(VoxelSniperConfiguration.setSecondPoint, b.getFlooredX(), b.getFlooredY(), b.getFlooredZ());
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
        final Vector3i min = new Vector3i(Math.min(a.getFlooredX(), b.getFlooredX()), Math.min(a.getFlooredY(), b.getFlooredY()),
                Math.min(a.getFlooredZ(), b.getFlooredZ()));
        final ComplexShape s = new ComplexShape(w, h, l, new Vector3i(ox, oy, oz));
        Vector3d dir = b.toVector().sub(a.toVector());
        RayTrace ray = new RayTrace(a, dir, dir.length(), BaseConfiguration.minimumWorldDepth, BaseConfiguration.maximumWorldHeight,
                BaseConfiguration.rayTraceStep, new Vector3d(0, 0, 0));
        ray.getTraversalBlocks().clear();
        ray.trace(new Function<Block, Boolean>()
        {

            @Override
            public Boolean apply(Block arg0)
            {
                Vector3i vec = arg0.getPosition();
                int x = vec.getX() - min.getX();
                int y = vec.getY() - min.getY();
                int z = vec.getZ() - min.getZ();
                if (x < 0 || x >= s.getWidth() || y < 0 || y >= s.getHeight() || z < 0 || z >= s.getLength())
                {
                    return false;
                }
                s.set(x, y, z, false);
                return true;
            }

        });
        args.set(BrushContext.RUNTIME, BrushKeys.SHAPE, s);
        return ExecutionResult.continueExecution();
    }

}
