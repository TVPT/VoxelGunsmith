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
package com.voxelplugineering.voxelsniper.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thevoxelbox.vsl.type.Type;
import com.thevoxelbox.vsl.type.TypeDepth;
import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.world.BlockChangeQueue;

/**
 * The representation of a world or scene of voxels. X and Z axes are assumed to be the horizontal axes and Y the vertical axis.
 */
public abstract class CommonWorld
{

    /**
     * The {@link com.thevoxelbox.vsl.type.Type} for materials.
     */
    public static Type COMMONVECTOR_TYPE = Type.getType("COMMONWORLD", "com/voxelplugineering/voxelsniper/common/CommonWorld", TypeDepth.SINGLE);
    /**
     * A List of Change Queues which are still in the process of being applied to the world.
     */
    List<BlockChangeQueue> pendingChanges = new ArrayList<BlockChangeQueue>();

    /**
     * Returns the name of the world.
     * 
     * @return the name
     */
    public abstract String getName();

    /**
     * Gets the chunk at the given coordinates.
     * 
     * @param x the chunk's x-axis position
     * @param y the chunk's y-axis position
     * @param z the chunk's z-axis position
     * @return the chunk
     */
    public abstract CommonChunk getChunkAt(int x, int y, int z);

    /**
     * Returns the chunk at the given x, z coordinates. Assumes no y-axis arrangement of chunks (uses 0 for the y-axis). This method exists to better
     * support the minecraft philosophy of chunk arrangement, without restricting the implementation from using y-axis chunks.
     * 
     * @param x the chunk's x-axis position
     * @param z the chunk's z-axis position
     * @return the chunk
     */
    public CommonChunk getChunkAt(int x, int z)
    {
        return getChunkAt(x, 0, z);
    }

    /**
     * Returns the chunk at the given location
     * 
     * @param location the location
     * @return the block
     */
    public CommonBlock getBlockAt(CommonLocation location)
    {
        return getBlockAt(location.getFlooredX(), location.getFlooredY(), location.getFlooredZ());
    }

    /**
     * Returns the block at the given coordinates.
     * 
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the block
     */
    public abstract CommonBlock getBlockAt(int x, int y, int z);

    /**
     * Sets the block at the given location to the given material.
     * 
     * @param location the location
     * @param material the new material
     */
    public void setBlockAt(CommonLocation location, CommonMaterial<?> material)
    {
        setBlockAt(location.getFlooredX(), location.getFlooredY(), location.getFlooredZ(), material);
    }

    /**
     * Sets the voxel at the given coordinates to the given material.
     * 
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @param material the new material
     */
    public abstract void setBlockAt(int x, int y, int z, CommonMaterial<?> material);

    /**
     * Apply the next set of changes to the world. This method should be called in a regular manner 5x per second from the specific implementation.
     * <p>
     * TODO: perhaps we should implements a scheduler within Gunsmith and tick the worlds from there rather than from the specific implementation.
     */
    public void tickChanges()
    {
        if (!Thread.currentThread().equals(Gunsmith.getVoxelSniper().getMainThread()))
        {
            Gunsmith.getLogger().warn("Attempted to tick world changes from invalid thread!");
            return;
        }
        int total = (Integer) Gunsmith.getConfiguration().get("BLOCK_CHANGES_PER_TICK");
        int n = this.pendingChanges.size();
        for (Iterator<BlockChangeQueue> iterator = this.pendingChanges.iterator(); iterator.hasNext();)
        {
            int next = total / (n--);
            BlockChangeQueue nextQueue = iterator.next();
            int actual = nextQueue.perform(next);
            total -= actual;
            if (nextQueue.isFinished())
            {
                //TODO add this queue to the player's history buffer here rather than when the queue is flushed to the world.
                iterator.remove();
            }
            if (total < 0)
            {
                break;
            }
        }
    }

    /**
     * Adds a change queue to this world to be processed.
     * 
     * @param changeQueue the new queue
     */
    public void registerChangeQueue(BlockChangeQueue changeQueue)
    {
        this.pendingChanges.add(changeQueue);
    }

}
