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
package com.voxelplugineering.voxelsniper.event;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy.ThreadingPolicy.ASYNCHRONOUS;

import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy;

/**
 * An abstract event for any event focused on a particular player.
 */
public abstract class SniperEvent extends Event
{

    /**
     * The sniper.
     */
    private final Player sniper;

    /**
     * Sets up a {@link SniperEvent}.
     * 
     * @param sniper The sniper involved with the event
     */
    protected SniperEvent(Player sniper)
    {
        checkNotNull(sniper, "Sniper cannot be null!");
        this.sniper = sniper;
    }

    /**
     * Returns the sniper.
     * 
     * @return the sniper
     */
    public Player getSniper()
    {
        return this.sniper;
    }

    /**
     * An event for handling the creation of new players.
     */
    @EventThreadingPolicy(ASYNCHRONOUS)
    public static class SniperCreateEvent extends SniperEvent
    {

        /**
         * Creates a new {@link SniperCreateEvent}.
         * 
         * @param sniper The player
         */
        public SniperCreateEvent(Player sniper)
        {
            super(sniper);
        }

    }

    /**
     * An event to handle the removal of a player.
     */
    @EventThreadingPolicy(ASYNCHRONOUS)
    public static class SniperDestroyEvent extends SniperEvent
    {

        /**
         * Creates a new {@link SniperDestroyEvent}
         * 
         * @param sniper The player
         */
        public SniperDestroyEvent(Player sniper)
        {
            super(sniper);
        }

    }
}
