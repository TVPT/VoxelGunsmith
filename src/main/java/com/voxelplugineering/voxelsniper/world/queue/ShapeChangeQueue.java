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

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.shape.MaterialShape;

/**
 * A special change queue for setting all of a shape to a single material.
 */
public class ShapeChangeQueue extends ChangeQueue
{

    /**
     * The shape to change.
     */
    private MaterialShape shape;
    /**
     * The origin to set the shape relative to.
     */
    private Location originOffset;
    private Location origin;
    /**
     * The current execution state of this change queue.
     */
    private ExecutionState state;
    /**
     * The position of this queue's execution.
     */
    private long position = 0;

    /**
     * Creates a new {@link ShapeChangeQueue}.
     * 
     * @param sniper
     *            the owner
     * @param origin
     *            the origin of the shape in the world
     * @param shape
     *            the shape
     */
    public ShapeChangeQueue(Player sniper, Location origin, MaterialShape shape)
    {
        super(sniper, origin.getWorld());
        this.originOffset = origin.add(-shape.getOrigin().getX(), -shape.getOrigin().getY(), -shape.getOrigin().getZ());
        this.origin = origin;
        this.state = ExecutionState.UNSTARTED;
        this.shape = shape;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFinished()
    {
        if (this.position == this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength())
        {
            this.state = ExecutionState.DONE;
        }
        return this.state == ExecutionState.DONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush()
    {
        reset();
        this.getOwner()
                .getUndoHistory()
                .addHistory(
                        this,
                        new ShapeChangeQueue(getOwner(), this.origin, this.originOffset.getWorld().getShapeFromWorld(this.origin,
                                this.shape.getShape())));
        this.getOwner().addPending(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int perform(int next)
    {
        int count = 0;
        if (this.state == ExecutionState.UNSTARTED)
        {
            this.position = this.shape.getHeight() - 1;
            this.state = ExecutionState.BREAKABLE;
        }
        if (this.state == ExecutionState.BREAKABLE)
        {
            for (; this.position >= 0 && count < next; this.position--)
            {
                for (int x = 0; x < this.shape.getWidth(); x++)
                {
                    for (int z = 0; z < this.shape.getLength(); z++)
                    {
                        Material existingMaterial =
                                getWorld()
                                        .getBlock(x + this.originOffset.getFlooredX(), (int) this.position + this.originOffset.getFlooredY(),
                                                z + this.originOffset.getFlooredZ()).get().getMaterial();
                        Optional<Material> newMaterial = this.shape.get(x, (int) this.position, z, false);
                        if (newMaterial.isPresent() && (existingMaterial.isLiquid() || existingMaterial.isReliantOnEnvironment()))
                        {
                            getWorld().setBlock(newMaterial.get(), x + this.originOffset.getFlooredX(),
                                    (int) this.position + this.originOffset.getFlooredY(), z + this.originOffset.getFlooredZ());
                            count++;
                        }
                    }
                }
            }
            if (this.position < 0)
            {
                this.position = 0;
                this.state = ExecutionState.INCREMENTAL;
            }
        } else if (this.state == ExecutionState.INCREMENTAL)
        {
            // Gunsmith.getLogger().info("Position at " + this.position);
            for (; this.position < this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength() && count < next; this.position++)
            {
                int z = (int) (this.position / (this.shape.getWidth() * this.shape.getHeight()));
                int y = (int) ((this.position % (this.shape.getWidth() * this.shape.getHeight())) / this.shape.getWidth());
                int x = (int) ((this.position % (this.shape.getWidth() * this.shape.getHeight())) % this.shape.getWidth());
                Material existingMaterial =
                        getWorld()
                                .getBlock(x + this.originOffset.getFlooredX(), y + this.originOffset.getFlooredY(),
                                        z + this.originOffset.getFlooredZ()).get().getMaterial();
                Optional<Material> newMaterial = this.shape.get(x, y, z, false);
                if (newMaterial.isPresent() && !(existingMaterial.isLiquid() || existingMaterial.isReliantOnEnvironment()))
                {
                    count++;
                    getWorld().setBlock(newMaterial.get(), x + this.originOffset.getFlooredX(), y + this.originOffset.getFlooredY(),
                            z + this.originOffset.getFlooredZ());
                }
            }
            if (this.position == this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength())
            {
                this.state = ExecutionState.DONE;
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
        this.state = ExecutionState.UNSTARTED;
    }

}

/**
 * The execution state.
 */
enum ExecutionState
{
    UNSTARTED, BREAKABLE, INCREMENTAL, DONE;
}
