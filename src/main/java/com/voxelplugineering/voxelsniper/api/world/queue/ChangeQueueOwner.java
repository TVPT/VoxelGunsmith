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
package com.voxelplugineering.voxelsniper.api.world.queue;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.world.queue.ChangeQueue;

/**
 * An interface for anything which may be the owner of a change queue.
 */
public interface ChangeQueueOwner
{

    /**
     * Adds a queue to the history buffer for this player. If the size of the history buffer is greater than the maximum allowed size then the oldest
     * stored queues are dropped.
     * 
     * @param invert the new queue for the buffer, this queue is assumed to be an inverse queue to an operation performed in the world, cannot be null
     */
    void addHistory(ChangeQueue invert);

    /**
     * Pushes the top n inverse change queues from the history buffer to the world, effectively undoing previous changes.
     * 
     * @param n the number of past changes to undo, cannot be negative
     */
    void undoHistory(int n);

    /**
     * Returns whether this sniper has pending change queues which have not yet been handled.
     * 
     * @return has pending changes
     */
    boolean hasPendingChanges();

    /**
     * Returns the next pending {@link ChangeQueue}.
     * 
     * @return the next change queue
     */
    Optional<ChangeQueue> getNextPendingChange();

    /**
     * Adds the given change queue to the pending changes queue.
     * 
     * @param blockChangeQueue the new {@link ChangeQueue}, cannot be null
     */
    void addPending(ChangeQueue blockChangeQueue);

    /**
     * Removes the next pending change if it has finished.
     */
    void clearNextPending();
}
