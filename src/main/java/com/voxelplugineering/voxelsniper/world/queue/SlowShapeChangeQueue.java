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
package com.voxelplugineering.voxelsniper.world.queue;

import java.util.concurrent.Future;

import com.google.common.base.Optional;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.api.world.queue.ChangeQueueOwner;
import com.voxelplugineering.voxelsniper.api.world.queue.WorldChange;
import com.voxelplugineering.voxelsniper.brushes.hard.CallbackGraph;
import com.voxelplugineering.voxelsniper.shape.HybridMaterialShape;

/**
 * 
 */
public class SlowShapeChangeQueue implements WorldChange
{

    private final ChangeQueueOwner owner;
    private final Location target;
    private final Location originOffset;
    private final Shape shape;
    private final CallbackGraph graph;
    private final RuntimeState state;
    private long position = 0;
    private int ticks = 0;
    private ExecutionState execState;
    private MaterialShape matshape;
    private Future<MaterialShape> future = null;
    private final World world;

    /**
     * @param owner
     * @param target
     * @param shape
     * @param graph
     * @param state
     */
    public SlowShapeChangeQueue(ChangeQueueOwner owner, Location target, Shape shape, CallbackGraph graph, RuntimeState state)
    {
        this.owner = owner;
        this.target = target;
        this.originOffset = target.add(-shape.getOrigin().getX(), -shape.getOrigin().getY(), -shape.getOrigin().getZ());
        this.shape = shape;
        this.graph = graph;
        this.state = state;
        this.execState = ExecutionState.UNSTARTED;
        this.matshape = new HybridMaterialShape(shape, target.getWorld().getMaterialRegistry().getAirMaterial());
        this.world = target.getWorld();
    }

    /**
     * @param owner
     * @param target
     * @param shape
     */
    public SlowShapeChangeQueue(ChangeQueueOwner owner, Location target, Shape shape)
    {
        this(owner, target, shape, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFinished()
    {
        if (this.position == this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength())
        {
            this.execState = ExecutionState.DONE;
        }
        return this.execState == ExecutionState.DONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int perform(int next)
    {
        int count = 0;
        if (this.execState == ExecutionState.UNSTARTED)
        {
            if (this.graph == null || this.state == null)
            {
                this.position = this.shape.getHeight() - 1;
                this.execState = ExecutionState.BREAKABLE;
                this.ticks = 0;
            } else if (this.future == null)
            {
                this.matshape.reset();
                this.future = this.graph.postPart(this.state.getUUID(), this.matshape);
            } else if (this.future.isDone())
            {
                this.position = this.shape.getHeight() - 1;
                this.execState = ExecutionState.BREAKABLE;
                this.ticks = 0;
            }
        }
        if (this.execState == ExecutionState.BREAKABLE)
        {
            for (; this.position >= 0 && count < next; this.position--)
            {
                int subcount = 0;
                int oy = (int) this.position + this.originOffset.getFlooredY();
                for (int x = 0; x < this.shape.getWidth(); x++)
                {
                    int ox = x + this.originOffset.getFlooredX();
                    for (int z = 0; z < this.shape.getLength(); z++)
                    {
                        int oz = z + this.originOffset.getFlooredZ();
                        Optional<Block> block = this.world.getBlock(ox, oy, oz);
                        if (!block.isPresent())
                        {
                            continue;
                        }
                        Material existingMaterial = block.get().getMaterial();
                        Optional<Material> newMaterial = this.matshape.getMaterial(x, (int) this.position, z, false);
                        if (newMaterial.isPresent() && (existingMaterial.isLiquid() || existingMaterial.isReliantOnEnvironment()))
                        {
                            this.world.setBlock(newMaterial.get(), x + this.originOffset.getFlooredX(),
                                    (int) this.position + this.originOffset.getFlooredY(), z + this.originOffset.getFlooredZ());
                            subcount++;
                        }
                    }
                }
                count += Math.max(next / (100000/(this.shape.getWidth() * this.shape.getLength())), subcount);

            }
            if (this.position < 0)
            {
                this.ticks = 0;
                this.position = 0;
                this.execState = ExecutionState.INCREMENTAL;
            } else if (this.ticks > 10)
            {
                this.ticks = 0;
                this.owner.sendMessage(String.format("Operating on breakable blocks %d out of %d", this.shape.getHeight() - 1 - this.position,
                        this.shape.getHeight() - 1));
            }
        } else if (this.execState == ExecutionState.INCREMENTAL)
        {
            this.ticks++;
            // Gunsmith.getLogger().info("Position at " + this.position);
            for (; this.position < this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength() && count < next; this.position++)
            {
                int z = (int) (this.position / (this.shape.getWidth() * this.shape.getHeight()));
                int y = (int) ((this.position % (this.shape.getWidth() * this.shape.getHeight())) / this.shape.getWidth());
                int x = (int) ((this.position % (this.shape.getWidth() * this.shape.getHeight())) % this.shape.getWidth());
                Optional<Block> block =
                        this.world.getBlock(x + this.originOffset.getFlooredX(), y + this.originOffset.getFlooredY(),
                                z + this.originOffset.getFlooredZ());
                if (!block.isPresent())
                {
                    continue;
                }
                Material existingMaterial = block.get().getMaterial();
                Optional<Material> newMaterial = this.matshape.getMaterial(x, y, z, false);
                if (newMaterial.isPresent() && !(existingMaterial.isLiquid() || existingMaterial.isReliantOnEnvironment()))
                {
                    count++;
                    this.world.setBlock(newMaterial.get(), x + this.originOffset.getFlooredX(), y + this.originOffset.getFlooredY(), z
                            + this.originOffset.getFlooredZ());
                }
            }
            if (this.position == this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength())
            {
                this.owner.sendMessage("Finished %d changes.", this.position);
                this.execState = ExecutionState.DONE;
            } else if (this.ticks > 10)
            {
                this.ticks = 0;
                this.owner.sendMessage("Performed %d out of %d changes.", this.position,
                        this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength());
            }
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset()
    {
        this.execState = ExecutionState.UNSTARTED;
        this.future = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location getOrigin()
    {
        return this.target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape getShape()
    {
        return this.shape;
    }

}

/**
 * The execution state.
 */
enum ExecutionState
{
    UNSTARTED, BREAKABLE, INCREMENTAL, DONE;
}
