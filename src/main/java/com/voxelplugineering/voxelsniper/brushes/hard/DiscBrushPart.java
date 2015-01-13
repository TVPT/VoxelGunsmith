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
package com.voxelplugineering.voxelsniper.brushes.hard;

import java.util.Queue;

import com.google.common.collect.Queues;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.shape.csg.CuboidShape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.world.queue.SlowShapeChangeQueue;

/**
 * 
 */
public class DiscBrushPart extends CallbackGraph
{

    /**
     * 
     */
    public DiscBrushPart()
    {
        super("voxel");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        double radius = state.getVars().get("brushsize", Double.class).get();
        Location target = state.getVars().get("targetblock", Block.class).get().getLocation();
        int rad = (int) (radius * 2 + 1);
        Shape s = new CuboidShape(rad, rad, rad, new Vector3i(radius, radius, radius));
        Player p = state.getVars().get("__PLAYER__", Player.class).get();
        /* if (s.getVolume() > 2000000 && s.getHeight() > 10)
         {
             p.sendMessage("Volume threshold exceeded, switching to Chunk-based change execution.");
         } else
         {*/
        SlowShapeChangeQueue change = new SlowShapeChangeQueue(p, target, s, this, state);
        SlowShapeChangeQueue undo = new SlowShapeChangeQueue(p, target, target.getWorld().getShapeFromWorld(target, s));
        p.buildChange(change, undo);
        getPending().put(state.getUUID(), Queues.<ShapeCallback>newConcurrentLinkedQueue());
        while (getPending().containsKey(state.getUUID()))
        {
            Queue<ShapeCallback> queue = getPending().get(state.getUUID());
            ShapeCallback next;
            if ((next = queue.poll()) != null)
            {
                if (next.markStarted())
                {
                    state.getVars().set("__CHAINED__shape", next.getShape());
                    this.getNextGraph().exec(state);
                    next.markDone();
                }
            } else
            {
                try
                {
                    Thread.sleep(1);
                } catch (InterruptedException e)
                {
                    getPending().remove(state.getUUID());
                }
            }
        }
        //}
    }

}
