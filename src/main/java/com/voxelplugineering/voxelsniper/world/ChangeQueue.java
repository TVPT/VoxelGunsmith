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

import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonWorld;

/**
 * An abstract change queue.
 */
public abstract class ChangeQueue
{

    /**
     * The sniper which this queue is attached to.
     */
    private ISniper sniper;
    /**
     * The world that this queue is changing.
     */
    private CommonWorld<?> world;

    /**
     * Creates a new {@link ChangeQueue}.
     * 
     * @param sniper the player
     * @param world the world
     */
    public ChangeQueue(ISniper sniper, CommonWorld<?> world)
    {
        checkNotNull(world, "World cannot be null");
        checkNotNull(sniper, "Sniper cannot be null");
        this.world = world;
        this.sniper = sniper;
    }

    /**
     * Returns the player that this queue is attached to.
     * 
     * @return the owner
     */
    public ISniper getOwner()
    {
        return this.sniper;
    }

    /**
     * The world that this queue is change
     * 
     * @return the world
     */
    public CommonWorld<?> getWorld()
    {
        return this.world;
    }

    /**
     * Whether this queue has finished executing.
     * 
     * @return is finished
     */
    public abstract boolean isFinished();

    /**
     * Flushes this queue's changes down to the world.
     */
    public abstract void flush();

    /**
     * Inverts the changes in this queue to create a new queue which would undo this queue's changes.
     * 
     * @return the inverse queue
     */
    public abstract ChangeQueue invert();

    /**
     * Applies the next n changes to the world.
     * 
     * @param next the number of changes to apply
     * @return the actual number of changes applied
     */
    public abstract int perform(int next);

    /**
     * Resets the position of this queue's execution.
     */
    public abstract void reset();

}
