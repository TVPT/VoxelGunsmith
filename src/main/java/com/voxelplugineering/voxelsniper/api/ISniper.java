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
package com.voxelplugineering.voxelsniper.api;

import com.thevoxelbox.vsl.api.IVariableScope;
import com.voxelplugineering.voxelsniper.common.CommonLocation;
import com.voxelplugineering.voxelsniper.common.CommonWorld;
import com.voxelplugineering.voxelsniper.world.BlockChangeQueue;

/**
 * Representation of a user within Gunsmith. Holds all state information relevant to the user.
 */
public interface ISniper
{

    /**
     * Returns the name of the user.
     * 
     * @return the user's name
     */
    String getName();

    /**
     * Sends a message to the user.
     * 
     * @param msg the message to send, cannot be null or empty
     */
    void sendMessage(String msg);

    /**
     * Returns the brush manager specific to this user.
     * 
     * @return the sniper's brush manager
     */
    IBrushManager getPersonalBrushManager();

    /**
     * Returns the location of the user.
     * 
     * @return the location
     */
    CommonLocation getLocation();

    /**
     * Sets the current brush of this sniper.
     * 
     * @param brush the new brush, cannot be null
     */
    void setCurrentBrush(IBrush brush);

    /**
     * Returns the currently selected brush of this sniper.
     * 
     * @return the brush
     */
    IBrush getCurrentBrush();

    /**
     * Returns this sniper's current brush settings.
     * 
     * @return the brush settings
     */
    IVariableScope getBrushSettings();

    /**
     * Resets the players brush settings to the defaults.
     */
    void resetSettings();

    /**
     * Adds a queue to the history buffer for this player. If the size of the history buffer is greater than the maximum allowed size then the oldest
     * stored queues are dropped.
     * 
     * @param invert the new queue for the buffer, this queue is assumed to be an inverse queue to an operation performed in the world, cannot be null
     */
    void addHistory(BlockChangeQueue invert);

    /**
     * Resets the currently used queue.
     */
    void resetPersonalQueue();

    /**
     * Returns the world that this player is currently within.
     * 
     * @return this player's world
     */
    CommonWorld getWorld();

    /**
     * Returns the currently active change queue that this players changes should be pushed to.
     * 
     * @return the active queue
     */
    BlockChangeQueue getActiveQueue();

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
     * Returns the next pending {@link BlockChangeQueue}.
     * 
     * @return the next change queue
     */
    BlockChangeQueue getNextPendingChange();

    /**
     * Adds the given change queue to the pending changes queue.
     * 
     * @param blockChangeQueue the new {@link BlockChangeQueue}, cannot be null
     */
    void addPending(BlockChangeQueue blockChangeQueue);

}
