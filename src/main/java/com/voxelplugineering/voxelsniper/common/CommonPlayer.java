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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import com.google.common.base.Optional;
import com.thevoxelbox.vsl.VariableScope;
import com.thevoxelbox.vsl.api.IVariableScope;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.api.IBrushManager;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.factory.WeakWrapper;
import com.voxelplugineering.voxelsniper.world.ChangeQueue;

/**
 * A standard player class wrapping a user class from the underlying implementation.
 * 
 * @param <T> the user class from the underlying implementation
 */
public abstract class CommonPlayer<T> extends WeakWrapper<T> implements ISniper
{
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
    private Deque<ChangeQueue> history;
    /**
     * The queue of pending {@link ChangeQueue}s waiting to be processed.
     */
    private Queue<ChangeQueue> pending;

    /**
     * Creates a new CommonPlayer with a weak reference to the player. TODO add constructor for receiving custom parent brush manager
     * 
     * @param player the player object
     */
    protected CommonPlayer(T player)
    {
        super(player);
        //TODO: have player inherit brushes from group rather than the global brush manager always.
        this.personalBrushManager = new CommonBrushManager(Gunsmith.getGlobalBrushManager());
        this.brushVariables = new VariableScope();
        resetSettings();
        this.history = new ArrayDeque<ChangeQueue>();
        this.pending = new LinkedList<ChangeQueue>();
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
        IBrush start = null;
        IBrush last = null;
        for (String brushName : Gunsmith.getConfiguration().get("DEFAULT_BRUSH").get().toString().split(" "))
        {
            IBrush brush = getPersonalBrushManager().getNewBrushInstance(brushName).orNull();
            if (brush == null)
            {
                getPersonalBrushManager().loadBrush(brushName);
                brush = getPersonalBrushManager().getNewBrushInstance(brushName).orNull();
                if (brush == null)
                {
                    sendMessage("Could not find a brush part named " + brushName);
                    break;
                }
            }
            if (start == null)
            {
                start = brush;
                last = brush;
            } else
            {
                last.chain(brush);
                last = brush;
            }

        }
        setCurrentBrush(start);
        sendMessage("Brush set to " + Gunsmith.getConfiguration().get("DEFAULT_BRUSH").get().toString());
        this.brushVariables.set("brushSize", Gunsmith.getConfiguration().get("DEFAULT_BRUSH_SIZE").get());
        sendMessage("Your brush size was changed to " + Gunsmith.getConfiguration().get("DEFAULT_BRUSH_SIZE").get().toString());
        CommonMaterial<?> material =
                getWorld()
                        .getMaterialRegistry()
                        .get(Gunsmith.getConfiguration().get("DEFAULT_BRUSH_MATERIAL").or(getWorld().getMaterialRegistry().getAirMaterial())
                                .toString()).get();
        sendMessage("Set material to " + material.toString());
        getBrushSettings().set("setMaterial", material);
    }

    /**
     * {@inheritDoc}
     */
    public void addHistory(ChangeQueue changeQueue)
    {
        /*checkNotNull(changeQueue, "ChangeQueue cannot be null");
        this.history.addFirst(changeQueue);
        while (this.history.size() > 20)
        {
            this.history.removeLast();
        }*/
    }

    /**
     * {@inheritDoc}
     */
    public void undoHistory(int n)
    {
        while (n > 0 && !this.history.isEmpty())
        {
            ChangeQueue next = this.history.removeFirst();
            this.pending.add(next);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPendingChanges()
    {
        return this.pending.size() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ChangeQueue> getNextPendingChange()
    {
        return (this.pending.isEmpty() ? Optional.<ChangeQueue> absent() : Optional.<ChangeQueue> of(this.pending.peek()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPending(ChangeQueue queue)
    {
        checkNotNull(queue, "ChangeQueue cannot be null");
        this.pending.add(queue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearNextPending()
    {
        if (!this.pending.isEmpty() && this.pending.peek().isFinished())
        {
            this.pending.remove();
        }
    }

}
