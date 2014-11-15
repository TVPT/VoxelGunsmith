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

import java.lang.ref.WeakReference;

import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrushManager;
import com.voxelplugineering.voxelsniper.api.ISniper;

/**
 * A standard player class wrapping a user class from the underlying implementation.
 * 
 * @param <T>
 *            the user class from the underlying implementation
 */
public abstract class CommonPlayer<T> implements ISniper
{
    /**
     * A weak reference to the underlying player object.
     */
    WeakReference<T> playerReference;
    /**
     * This users specific brush manager.
     */
    IBrushManager personalBrushManager;

    /**
     * Creates a new CommonPlayer with a weak reference to the player.
     * @param player the player object
     */
    protected CommonPlayer(T player)
    {
        this.playerReference = new WeakReference<T>(player);
        //TODO: have player inherit brushes from group rather than the global brush manager always.
        this.personalBrushManager = new CommonBrushManager(Gunsmith.getGlobalBrushManager());
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
     * Gets the personalized brush manager instance for this player.
     *
     * @return the personalized brush manager
     */
    public IBrushManager getPersonalBrushManager()
    {
        return this.personalBrushManager;
    }
}
