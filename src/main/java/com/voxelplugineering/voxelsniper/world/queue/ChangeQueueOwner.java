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

import com.voxelplugineering.voxelsniper.service.command.MessageReceiver;

import java.util.Optional;

/**
 * An interface for anything which may be the owner of a change queue.
 */
public interface ChangeQueueOwner extends MessageReceiver
{

    /**
     * Undoes the last n changes to the world.
     * 
     * @param n The number of past changes to undo, cannot be negative
     */
    void undoHistory(int n);

    /**
     * Redoes the last n undone changes to the world.
     * 
     * @param n The number of changes to redo, cannot be negative
     */
    void redoHistory(int n);

    /**
     * Returns whether this sniper has pending change queues which have not yet been handled.
     * 
     * @return Has pending changes
     */
    boolean hasPendingChanges();

    /**
     * Returns the next pending {@link ChangeQueue}.
     * 
     * @return The next change queue
     */
    Optional<ChangeQueue> getNextPendingChange();

    /**
     * Adds the given change queue to the pending changes queue.
     * 
     * @param blockChangeQueue The new {@link ChangeQueue}, cannot be null
     */
    void addPending(ChangeQueue blockChangeQueue);

    /**
     * Removes the next pending change if it has finished.
     */
    void clearNextPending(boolean force);

    /**
     * Gets the undo history manager.
     * 
     * @return The undo history manager
     */
    UndoQueue getUndoHistory();
}
