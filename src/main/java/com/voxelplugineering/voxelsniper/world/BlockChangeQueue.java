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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;
import com.voxelplugineering.voxelsniper.common.CommonWorld;

/**
 * A change queue representing a contained queue of changes relevant to a single world and player. This queue is the representation of a single
 * operation.
 */
public class BlockChangeQueue extends ChangeQueue
{

    /**
     * A List of the changes contained by this queue.
     */
    private List<BlockChange> changes = new ArrayList<BlockChange>();
    /**
     * The index inside the queue of the first non-falloff material.
     */
    private int intermediate = 0;
    /**
     * The current processing index of this queue.
     */
    private int position = 0;
    /**
     * The material to use as an intermediate block if both the from and to materials are breakable.
     */
    private CommonMaterial<?> intermediateMaterial;

    /**
     * Creates a new ChangeQueue
     * 
     * @param world the world in which this queue is relevant, cannot be null
     * @param owner the owner of this queue, cannot be null
     * @param intermediate the intermediate material
     */
    public BlockChangeQueue(ISniper owner, CommonWorld<?> world, CommonMaterial<?> intermediate)
    {
        super(owner, world);
        this.intermediateMaterial = intermediate;
    }

    /**
     * Adds the given change to this queue.
     * 
     * @param change the new change
     */
    public void add(BlockChange change)
    {
        checkNotNull(change, "Change cannot be null");
        if (position != 0)
        {
            Gunsmith.getLogger().error("Attempted to modify a queue currently being processed.");
            return;
        }
        boolean from = change.getFrom().isReliantOnEnvironment() || change.getFrom().isLiquid();
        boolean to = change.getTo().isReliantOnEnvironment() || change.getTo().isLiquid();

        if (from && to) //if both: split the change in two and add one to each side of the queue
        {
            BlockChange first = new BlockChange(change.getX(), change.getY(), change.getZ(), change.getFrom(), this.intermediateMaterial);
            BlockChange second = new BlockChange(change.getX(), change.getY(), change.getZ(), this.intermediateMaterial, change.getTo());
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
        //if neither add the change to the middle of the queue, marked by the intermediate index
        {
            changes.add(this.intermediate, change);
        }

    }

    /**
     * Clears the queue.
     */
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
        return null;
    }

    /**
     * Applies the next n changes to the world. If the processing index is still less than the intermediate index after n operations then continue
     * until the processing index reaches the intermediate index.
     * 
     * @param next the suggested number of operations to perform
     * @return the actual amount of operations performed
     */
    @Override
    public int perform(int next)
    {
        int count = 0;

        Gunsmith.getLogger().info("performing " + next + " changes");
        while ((count < next || position < intermediate) && position < this.changes.size())
        {
            BlockChange nextChange = this.changes.get(position);
            this.getWorld().setBlockAt(nextChange.getX(), nextChange.getY(), nextChange.getZ(), nextChange.getTo());
            position++;
            count++;
        }
        Gunsmith.getLogger().info("performed " + count + " changes");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush()
    {
        reset();
        this.getOwner().addPending(this);
        this.getOwner().addHistory(this.invert());
    }

}
