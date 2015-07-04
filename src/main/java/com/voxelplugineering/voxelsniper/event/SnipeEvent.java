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

import static com.voxelplugineering.voxelsniper.service.eventbus.EventThreadingPolicy.ThreadingPolicy.ASYNCHRONOUS;

import com.voxelplugineering.voxelsniper.brush.BrushAction;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.eventbus.EventThreadingPolicy;

/**
 * The event for a sniper action.
 */
@EventThreadingPolicy(ASYNCHRONOUS)
public class SnipeEvent extends SniperEvent
{

    private final double yaw;
    private final double pitch;
    private final BrushAction action;

    /**
     * Constructs a new SnipeEvent for processing
     * 
     * @param sniper the sniper involved
     * @param y the yaw
     * @param p the pitch
     */
    public SnipeEvent(Player sniper, double y, double p, BrushAction action)
    {
        super(sniper);
        this.yaw = y;
        this.pitch = p;
        this.action = action;
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

    public BrushAction getAction()
    {
        return this.action;
    }

}
