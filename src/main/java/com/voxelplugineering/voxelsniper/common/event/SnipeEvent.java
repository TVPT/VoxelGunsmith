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
package com.voxelplugineering.voxelsniper.common.event;

import com.voxelplugineering.voxelsniper.api.ISniper;

/**
 * The event for a sniper action.
 */
public class SnipeEvent extends CommonEvent
{
    private final ISniper sniper;
    private final double yaw;
    private final double pitch;

    /**
     * Constructs a new SnipeEvent for processing
     *
     * @param s the sniper involved
     * @param y the yaw
     * @param p the pitch
     */
    public SnipeEvent(ISniper s, double y, double p)
    {
        this.sniper = s;
        this.yaw = y;
        this.pitch = p;
    }

    /**
     * Returns the player who sent this snipe.
     * 
     * @return the player
     */
    public ISniper getSniper()
    {
        return this.sniper;
    }

    /**
     * The yaw of the direction in which the player was looking.
     * 
     * @return the yaw
     */
    public double getYaw()
    {
        return this.yaw;
    }

    /**
     * The pitch of the direction in which the player was looking.
     * 
     * @return the pitch
     */
    public double getPitch()
    {
        return this.pitch;
    }
}
