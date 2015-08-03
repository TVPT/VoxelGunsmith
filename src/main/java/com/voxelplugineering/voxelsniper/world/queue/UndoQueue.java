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

/**
 * A manager for the undo history of a {@link ChangeQueueOwner}.
 */
public interface UndoQueue
{

    /**
     * Adds the given change pair to the buffer. All history to the right of the current pointer
     * (eg. any previously undone changes) will be cleared.
     * 
     * @param change The change
     * @param reverse The reverse change
     */
    void addHistory(ChangeQueue change, ChangeQueue reverse);

    /**
     * Attempts to undo the last n changes.
     * 
     * @param n The number of changes to undo
     * @return The actual number of changes undone
     */
    int undo(int n);

    /**
     * Attempts to redo the last n undone changes.
     * 
     * @param n The number of changes to redo
     * @return The actual number of changes redone
     */
    int redo(int n);

    /**
     * Gets the {@link ChangeQueueOwner} associated with this {@link UndoQueue}.
     * 
     * @return The owner
     */
    ChangeQueueOwner getOwner();

    /**
     * Gets the maximum amount of undos and redos that may be stored.
     * 
     * @return The maximum
     */
    int getMaxBufferSize();

    /**
     * Sets a new capacity for the number of undos and redos that will be stored.
     * 
     * @param n The new capacity
     */
    void setMaxBufferSize(int n);

    /**
     * Clears all history.
     */
    void clearHistory();

    /**
     * Moves the pointer forward or backwards in history by the given offset.
     * 
     * @param n The offset
     */
    void movePointer(int n);

    /**
     * Gets the current size of the buffer.
     * 
     * @return The size
     */
    int size();

}
