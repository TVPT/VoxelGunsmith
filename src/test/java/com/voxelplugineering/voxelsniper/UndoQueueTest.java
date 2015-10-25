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
package com.voxelplugineering.voxelsniper;

import static org.junit.Assert.assertEquals;

import com.voxelplugineering.voxelsniper.world.queue.ChangeQueue;
import com.voxelplugineering.voxelsniper.world.queue.ChangeQueueOwner;
import com.voxelplugineering.voxelsniper.world.queue.CommonUndoQueue;
import com.voxelplugineering.voxelsniper.world.queue.UndoQueue;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * A set of tests for the {@link CommonUndoQueue}.
 */
public class UndoQueueTest
{

    /**
     * 
     */
    @Test
    public void testUndo()
    {
        ChangeQueueOwner owner = Mockito.mock(ChangeQueueOwner.class);
        ChangeQueue change = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse = Mockito.mock(ChangeQueue.class);
        UndoQueue queue = new CommonUndoQueue(owner);
        queue.addHistory(change, reverse);
        assertEquals(1, queue.undo(1));
        Mockito.verify(owner, Mockito.times(1)).addPending(reverse);
    }

    /**
     * 
     */
    @Test
    public void testMultiUndo()
    {
        ChangeQueueOwner owner = Mockito.mock(ChangeQueueOwner.class);
        ChangeQueue change = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse = Mockito.mock(ChangeQueue.class);
        ChangeQueue change2 = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse2 = Mockito.mock(ChangeQueue.class);
        UndoQueue queue = new CommonUndoQueue(owner);
        queue.addHistory(change, reverse);
        queue.addHistory(change2, reverse2);
        assertEquals(2, queue.undo(2));
        Mockito.verify(owner, Mockito.times(1)).addPending(reverse);
        Mockito.verify(owner, Mockito.times(1)).addPending(reverse2);
    }

    /**
     * 
     */
    @Test
    public void testUndoRedo()
    {
        ChangeQueueOwner owner = Mockito.mock(ChangeQueueOwner.class);
        ChangeQueue change = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse = Mockito.mock(ChangeQueue.class);
        ChangeQueue change2 = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse2 = Mockito.mock(ChangeQueue.class);
        UndoQueue queue = new CommonUndoQueue(owner);
        queue.addHistory(change, reverse);
        queue.addHistory(change2, reverse2);
        assertEquals(1, queue.undo(1));
        assertEquals(1, queue.redo(1));
        Mockito.verify(owner, Mockito.times(1)).addPending(reverse2);
        Mockito.verify(owner, Mockito.times(1)).addPending(change2);
    }

    /**
     * 
     */
    @Test
    public void testCapacity()
    {
        ChangeQueueOwner owner = Mockito.mock(ChangeQueueOwner.class);
        UndoQueue queue = new CommonUndoQueue(owner);
        queue.setMaxBufferSize(3);
        for (int i = 0; i < 4; i++)
        {
            ChangeQueue change = Mockito.mock(ChangeQueue.class);
            ChangeQueue reverse = Mockito.mock(ChangeQueue.class);
            queue.addHistory(change, reverse);
        }
        assertEquals(3, queue.size());
    }

    /**
     * 
     */
    @Test
    public void testTooManyUndos()
    {
        ChangeQueueOwner owner = Mockito.mock(ChangeQueueOwner.class);
        ChangeQueue change = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse = Mockito.mock(ChangeQueue.class);
        UndoQueue queue = new CommonUndoQueue(owner);
        queue.addHistory(change, reverse);
        assertEquals(1, queue.undo(1613));
        Mockito.verify(owner, Mockito.times(1)).addPending(reverse);
    }

    /**
     * 
     */
    @Test
    public void testTooManyRedos()
    {
        ChangeQueueOwner owner = Mockito.mock(ChangeQueueOwner.class);
        ChangeQueue change = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse = Mockito.mock(ChangeQueue.class);
        ChangeQueue change2 = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse2 = Mockito.mock(ChangeQueue.class);
        UndoQueue queue = new CommonUndoQueue(owner);
        queue.addHistory(change, reverse);
        queue.addHistory(change2, reverse2);
        assertEquals(1, queue.undo(1));
        assertEquals(1, queue.redo(155));
        Mockito.verify(owner, Mockito.times(1)).addPending(reverse2);
        Mockito.verify(owner, Mockito.times(1)).addPending(change2);
    }

    /**
     * 
     */
    @Test
    public void testClear()
    {
        ChangeQueueOwner owner = Mockito.mock(ChangeQueueOwner.class);
        ChangeQueue change = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse = Mockito.mock(ChangeQueue.class);
        UndoQueue queue = new CommonUndoQueue(owner);
        queue.addHistory(change, reverse);
        assertEquals(1, queue.size());
        queue.clearHistory();
        assertEquals(0, queue.size());
    }

    /**
     * 
     */
    @Test
    public void testMovePointerNegative()
    {
        ChangeQueueOwner owner = Mockito.mock(ChangeQueueOwner.class);
        ChangeQueue change = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse = Mockito.mock(ChangeQueue.class);
        UndoQueue queue = new CommonUndoQueue(owner);
        queue.addHistory(change, reverse);
        queue.movePointer(-1);
        assertEquals(0, queue.undo(1));
        Mockito.verify(owner, Mockito.times(0)).addPending(reverse);
    }

    /**
     * 
     */
    @Test
    public void testMovePointerPositive()
    {
        ChangeQueueOwner owner = Mockito.mock(ChangeQueueOwner.class);
        ChangeQueue change = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse = Mockito.mock(ChangeQueue.class);
        ChangeQueue change2 = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse2 = Mockito.mock(ChangeQueue.class);
        UndoQueue queue = new CommonUndoQueue(owner);
        queue.addHistory(change, reverse);
        queue.addHistory(change2, reverse2);
        assertEquals(1, queue.undo(1));
        queue.movePointer(1);
        assertEquals(0, queue.redo(1));
        Mockito.verify(owner, Mockito.times(1)).addPending(reverse2);
        Mockito.verify(owner, Mockito.times(0)).addPending(change2);
    }

    /**
     * 
     */
    @Test
    public void testMovePointerPositive2()
    {
        ChangeQueueOwner owner = Mockito.mock(ChangeQueueOwner.class);
        ChangeQueue change = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse = Mockito.mock(ChangeQueue.class);
        ChangeQueue change2 = Mockito.mock(ChangeQueue.class);
        ChangeQueue reverse2 = Mockito.mock(ChangeQueue.class);
        UndoQueue queue = new CommonUndoQueue(owner);
        queue.addHistory(change, reverse);
        queue.addHistory(change2, reverse2);
        assertEquals(2, queue.undo(2));
        queue.movePointer(1);
        assertEquals(1, queue.redo(2));
        Mockito.verify(owner, Mockito.times(1)).addPending(reverse2);
        Mockito.verify(owner, Mockito.times(1)).addPending(reverse);
        Mockito.verify(owner, Mockito.times(1)).addPending(change2);
        Mockito.verify(owner, Mockito.times(0)).addPending(change);
    }

}
