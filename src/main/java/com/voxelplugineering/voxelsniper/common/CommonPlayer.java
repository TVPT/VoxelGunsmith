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

import static com.google.common.base.Preconditions.checkNotNull;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import com.thevoxelbox.vsl.VariableScope;
import com.thevoxelbox.vsl.api.IVariableScope;
import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.api.IBrushManager;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.world.BlockChangeQueue;

/**
 * A standard player class wrapping a user class from the underlying implementation.
 * 
 * @param <T> the user class from the underlying implementation
 */
public abstract class CommonPlayer<T> implements ISniper
{
    /**
     * A weak reference to the underlying player object.
     */
    private WeakReference<T> playerReference;
    /**
     * This users specific brush manager.
     */
    private IBrushManager personalBrushManager;
    /**
     * The currently selected brush.
     */
    private IBrush currentBrush;
    /**
     * The brush settings specific to this player.
     */
    private IVariableScope brushVariables;
    /**
     * The history buffer for this player. Contains an ordered stack of inverse change queues.
     */
    private Deque<BlockChangeQueue> history;
    /**
     * The queue of pending {@link BlockChangeQueue}s waiting to be processed.
     */
    private Queue<BlockChangeQueue> pending;
    /**
     * The currently active change queue.
     */
    private BlockChangeQueue active;

    /**
     * Creates a new CommonPlayer with a weak reference to the player.
     * 
     * @param player the player object
     */
    protected CommonPlayer(T player)
    {
        checkNotNull(player, "Player cannot be null");
        this.playerReference = new WeakReference<T>(player);
        //TODO: have player inherit brushes from group rather than the global brush manager always.
        this.personalBrushManager = new CommonBrushManager(Gunsmith.getGlobalBrushManager());
        this.brushVariables = new VariableScope();
        resetSettings();
        this.history = new ArrayDeque<BlockChangeQueue>();
        this.pending = new LinkedList<BlockChangeQueue>();
        resetPersonalQueue();
    }

    /**
     * Returns the underlying player object.
     *
     * @return The player reference or null
     */
    public final T getPlayerReference()
    {
        return this.playerReference.get();
    }

    /**
     * {@inheritDoc}
     */
    public IBrushManager getPersonalBrushManager()
    {
        return this.personalBrushManager;
    }

    /**
     * {@inheritDoc}
     */
    public void setCurrentBrush(IBrush brush)
    {
        this.currentBrush = brush;
    }

    /**
     * {@inheritDoc}
     */
    public IBrush getCurrentBrush()
    {
        return this.currentBrush;
    }

    /**
     * {@inheritDoc}
     */
    public IVariableScope getBrushSettings()
    {
        return this.brushVariables;
    }

    /**
     * {@inheritDoc}
     */
    public void resetSettings()
    {
        this.currentBrush = this.personalBrushManager.getNewBrushInstance(Gunsmith.getConfiguration().get("DEFAULT_BRUSH").toString());
        sendMessage("Brush set to " + Gunsmith.getConfiguration().get("DEFAULT_BRUSH").toString());
        this.brushVariables.set("brushSize", Gunsmith.getConfiguration().get("DEFAULT_BRUSH_SIZE"));
        sendMessage("Your brush size was changed to " + Gunsmith.getConfiguration().get("DEFAULT_BRUSH_SIZE").toString());
    }

    /**
     * {@inheritDoc}
     */
    public void addHistory(BlockChangeQueue changeQueue)
    {
        checkNotNull(changeQueue, "ChangeQueue cannot be null");
        this.history.addFirst(changeQueue);
        while (this.history.size() > 20)
        {
            this.history.removeLast();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void undoHistory(int n)
    {
        while (n > 0 && !this.history.isEmpty())
        {
            BlockChangeQueue next = this.history.removeFirst();
            this.pending.add(next);
        }
    }

    /**
     * {@inheritDoc}
     */
    public BlockChangeQueue getActiveQueue()
    {
        return this.active;
    }

    /**
     * {@inheritDoc}
     */
    public void resetPersonalQueue()
    {
        this.active = new BlockChangeQueue(getWorld(), this);
        this.pending.clear();
    }

    @Override
    public boolean hasPendingChanges()
    {
        return this.pending.size() != 0;
    }

    @Override
    public BlockChangeQueue getNextPendingChange()
    {
        return this.pending.peek();
    }

    @Override
    public void addPending(BlockChangeQueue queue)
    {
        checkNotNull(queue, "ChangeQueue cannot be null");
        this.pending.add(queue);
    }

    public void clearNextPending()
    {
        if (!this.pending.isEmpty())
        {
            this.pending.remove();
        }
    }

    public void flushActiveQueue()
    {
        this.pending.add(this.active);
        addHistory(this.active.invert());
        resetPersonalQueue();
    }
}
