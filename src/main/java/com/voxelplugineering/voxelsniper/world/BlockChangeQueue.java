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
package com.voxelplugineering.voxelsniper.world;

import java.util.ArrayList;
import java.util.List;

import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IChangeQueue;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonWorld;

/**
 * A change queue representing a contained queue of changes relevant to a single world and player. This queue is the representation of a single
 * operation.
 */
public class BlockChangeQueue implements IChangeQueue<BlockChange>
{

    /**
     * A List of the changes contained by this queue.
     */
    private List<BlockChange> changes = new ArrayList<BlockChange>();
    /**
     * The world in which this queue is contained.
     */
    private CommonWorld world;
    /**
     * The owner of this queue.
     */
    private ISniper owner;
    /**
     * The index inside the queue of the first non-falloff material.
     */
    private int intermediate = 0;
    /**
     * The current processing index of this queue.
     */
    private int position = 0;

    /**
     * Creates a new ChangeQueue
     * 
     * @param world the world in which this queue is relevant
     * @param owner the owner of this queue
     */
    public BlockChangeQueue(CommonWorld world, ISniper owner)
    {
        this.world = world;
        this.owner = owner;
    }

    /**
     * Returns the world containing this queue.
     * 
     * @return the world
     */
    public CommonWorld getWorld()
    {
        return this.world;
    }

    /**
     * Returns the owner of this queue.
     * 
     * @return the owner
     */
    public ISniper getOwner()
    {
        return this.owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(BlockChange change)
    {
        if (position != 0)
        {
            Gunsmith.getLogger().error("Attempted to modify a queue currently being processed.");
            return;
        }
        Gunsmith.getLogger().debug("Added change " + change.toString());
        boolean from = change.getFrom().isReliantOnEnvironment() || change.getFrom().isLiquid();
        boolean to = change.getTo().isReliantOnEnvironment() || change.getTo().isLiquid();

        if (from && to) //if both: split the change in two and add one to each side of the queue
        {
            BlockChange first =
                    new BlockChange(change.getX(), change.getY(), change.getZ(), change.getFrom(), Gunsmith.getMaterialFactory().getAirMaterial());
            BlockChange second =
                    new BlockChange(change.getX(), change.getY(), change.getZ(), Gunsmith.getMaterialFactory().getAirMaterial(), change.getTo());
            changes.add(0, first);
            changes.add(second);
        } else if (from) // if only the first: then add the change to the front of the queue
        {
            changes.add(0, change);
            this.intermediate++;
        } else if (to)
        {
            changes.add(change);
        } else
        {
            changes.add(this.intermediate++, change);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush()
    {
        Gunsmith.getLogger().debug("Flushed queue into world");
        this.world.registerChangeQueue(this);
        this.owner.addHistory(this.invert());
        this.owner.resetPersonalQueue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        this.changes.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockChangeQueue invert()
    {
        BlockChangeQueue inverse = new BlockChangeQueue(this.world, this.owner);
        for (BlockChange c : this.changes)
        {
            inverse.add((BlockChange) c.getInverse());
        }
        return inverse;
    }

    /**
     * Applies the next n changes to the world. If the processing index is still less than the intermediate index after n operations then continue
     * until the processing index reaches the intermediate index.
     * 
     * @param next the suggested number of operations to perform
     * @return the actual amount of operations performed
     */
    public int perform(int next)
    {
        int count = 0;

        while ((count < next || position < intermediate) && position < this.changes.size())
        {
            BlockChange nextChange = this.changes.get(position);
            if (this.world.getBlockAt(nextChange.getX(), nextChange.getY(), nextChange.getZ()).getMaterial().equals(nextChange.getFrom()))
            {
                this.world.setBlockAt(nextChange.getX(), nextChange.getY(), nextChange.getZ(), nextChange.getTo());
            }
            position++;
            count++;
        }

        return count;
    }

    /**
     * Returns if the queue is fully processed.
     * 
     * @return is finished
     */
    public boolean isFinished()
    {
        return position >= this.changes.size();
    }

    /**
     * Resets the processing index to 0 in order to reapply all changes to the world.
     */
    public void reset()
    {
        this.position = 0;
    }

}
