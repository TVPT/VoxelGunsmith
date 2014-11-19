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

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.thevoxelbox.vsl.VariableScope;
import com.thevoxelbox.vsl.api.IVariableScope;
import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonLocation;
import com.voxelplugineering.voxelsniper.util.RayTrace;

/**
 * An event handler for the default behavior for events.
 */
public class CommonEventHandler
{

    /**
     * Constructs a new CommonEventHandler
     */
    public CommonEventHandler()
    {

    }

    /**
     * Processes the given {@link com.voxelplugineering.voxelsniper.common.event.SnipeEvent} and performs all necessary checks of the event. This
     * event handler is supports asyncronous callback.
     *
     * @param event the snipe event to perform
     */
    @Subscribe
    @AllowConcurrentEvents
    public void onSnipe(SnipeEvent event)
    {
        ISniper sniper = event.getSniper();
        CommonLocation location = sniper.getLocation();
        double yaw = event.getYaw();
        double pitch = event.getPitch();
        RayTrace ray = new RayTrace(location, yaw, pitch);
        ray.trace();

        IVariableScope brushVariables = new VariableScope(sniper.getBrushSettings());
        brushVariables.set("origin", location);
        brushVariables.set("yaw", yaw);
        brushVariables.set("pitch", pitch);
        brushVariables.set("targetBlock", ray.getTargetBlock());
        brushVariables.set("lastBlock", ray.getLastBlock());
        brushVariables.set("length", ray.getLength());
        Gunsmith.getLogger().debug("Snipe at " + ray.getTargetBlock().getLocation().toString());
        sniper.getCurrentBrush().run(brushVariables);
    }

    /**
     * Reports on unhandled (aka. dead) events on the event bus.
     * 
     * @param deadEvent the dead event
     */
    @Subscribe
    @AllowConcurrentEvents
    public void handleDeadEvent(DeadEvent deadEvent)
    {
        Object event = deadEvent.getEvent();
        Gunsmith.getLogger().warn("An unhandled " + event.getClass().getName() + " event was posted to the event bus!");
    }
}
