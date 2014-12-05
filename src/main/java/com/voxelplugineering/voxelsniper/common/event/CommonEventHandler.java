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
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IConfiguration;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonLocation;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;
import com.voxelplugineering.voxelsniper.util.RayTrace;

/**
 * An event handler for the default behavior for events.
 */
public class CommonEventHandler
{

    /**
     * The air material for the ray tracer traversal material.
     */
    private CommonMaterial<?> air;
    /**
     * The maximum of ray tracing.
     */
    private double range;
    /**
     * The minimum world depth.
     */
    private int minY;
    /**
     * The maximum world height.
     */
    private int maxY;
    /**
     * The step interval of the ray tracer, a smaller interval means a more exact trace (0.2 is a good value).
     */
    private double step;
    /**
     * The eye height of the player's view point relative to their stored position.
     */
    private double eyeHeight;
    
    /**
     * Constructs a new CommonEventHandler
     * 
     * @param air the air material
     * @param configuration the configuration object
     */
    public CommonEventHandler(CommonMaterial<?> air, IConfiguration configuration)
    {
        this.air = air;
        this.range = (Double) configuration.get("RAY_TRACE_MAX_RANGE");
        this.minY = (Integer) configuration.get("MINIMUM_WORLD_DEPTH");
        this.maxY = (Integer) configuration.get("MAXIMUM_WORLD_HEIGHT");
        this.step = (Double) configuration.get("RAY_TRACE_STEP");
        this.eyeHeight = (Double) configuration.get("PLAYER_EYE_HEIGHT");
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
        try
        {

            ISniper sniper = event.getSniper();
            CommonLocation location = sniper.getLocation();
            double yaw = event.getYaw();
            double pitch = event.getPitch();
            RayTrace ray = new RayTrace(location, yaw, pitch, this.air, range, minY, maxY, step, eyeHeight);
            ray.trace();

            IVariableScope brushVariables = new VariableScope(sniper.getBrushSettings());
            brushVariables.set("origin", location);
            brushVariables.set("yaw", yaw);
            brushVariables.set("pitch", pitch);
            brushVariables.set("targetBlock", ray.getTargetBlock());
            brushVariables.set("lastBlock", ray.getLastBlock());
            brushVariables.set("length", ray.getLength());
            boolean requirements = true;
            for (String req : sniper.getCurrentBrush().getRequiredVars())
            {
                if (brushVariables.get(req) == null)
                {
                    requirements = false;
                    sniper.sendMessage("Your current brush requires that the " + req + " variable to be set.");
                }
            }
            if (requirements)
            {
                Gunsmith.getLogger().info(
                        "Snipe at " + ray.getTargetBlock().getLocation().toString() + " : " + sniper.getCurrentBrush().getClass().getName());
                sniper.getCurrentBrush().run(brushVariables, sniper);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
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
