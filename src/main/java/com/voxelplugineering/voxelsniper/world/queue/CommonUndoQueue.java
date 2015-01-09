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

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.api.world.queue.ChangeQueueOwner;
import com.voxelplugineering.voxelsniper.api.world.queue.UndoQueue;

/**
 * A standard {@link UndoQueue}.
 */
public class CommonUndoQueue implements UndoQueue
{

    private final ChangeQueueOwner owner;
    private int capacity;
    private Entry pointer = null;
    private Entry first = null;

    /**
     * Creates a new {@link CommonUndoQueue} associated with the given {@link ChangeQueueOwner}.
     * 
     * @param owner The owner
     */
    public CommonUndoQueue(ChangeQueueOwner owner)
    {
        this.owner = owner;
        this.capacity = 30;
    }

    private void init(ChangeQueue change, ChangeQueue reverse)
    {
        this.first = this.pointer = new Entry(change, reverse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addHistory(ChangeQueue change, ChangeQueue reverse)
    {
        checkNotNull(change);
        if (this.pointer == null)
        {
            init(change, reverse);
            return;
        }
        Entry e = new Entry(change, reverse);
        Entry l = this.pointer;
        this.pointer = e;
        this.pointer.last = l;
        l.next = this.pointer;
        enforceCapacity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int undo(int n)
    {
        int count = 0;
        while (this.pointer != null && n > 0)
        {
            this.owner.addPending(this.pointer.undo);
            this.pointer = this.pointer.last;
            n--;
            count++;
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int redo(int n)
    {
        int count = 0;
        if (this.pointer == null && this.first != null)
        {
            this.pointer = this.first;
            this.owner.addPending(this.pointer.redo);
            n--;
            count++;
        }
        if (this.pointer == null)
        {
            return 0;
        }
        while (this.pointer.next != null && n > 0)
        {
            System.out.println("reset");
            this.pointer = this.pointer.next;
            this.owner.addPending(this.pointer.redo);
            n--;
            count++;
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeQueueOwner getOwner()
    {
        return this.owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxBufferSize()
    {
        return this.capacity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxBufferSize(int n)
    {
        this.capacity = n;
        enforceCapacity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearHistory()
    {
        this.first = this.pointer = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void movePointer(int n)
    {
        if (this.pointer == null && n > 0)
        {
            this.pointer = this.first;
            n--;
        }
        if (this.pointer == null)
        {
            return;
        }
        while (n != 0)
        {
            if (n < 0)
            {
                this.pointer = this.pointer.last;
                n++;
            } else if (n > 0)
            {
                this.pointer = this.pointer.next;
                n--;
            }
            if (this.pointer == null)
            {
                return;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public int size()
    {
        int count = 0;
        Entry c = this.first;
        while (c != null)
        {
            count++;
            c = c.next;
        }
        return count;
    }

    private void enforceCapacity()
    {
        int size = size();
        while (size > this.capacity && this.first != null)
        {
            this.first = this.first.next;
            this.first.last = null;
            size--;
        }
    }

}

class Entry
{

    Entry next = null;
    Entry last = null;
    ChangeQueue undo;
    ChangeQueue redo;

    public Entry(ChangeQueue r, ChangeQueue u)
    {
        this.undo = u;
        this.redo = r;
    }

}
