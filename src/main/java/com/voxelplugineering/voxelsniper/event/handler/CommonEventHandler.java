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
package com.voxelplugineering.voxelsniper.event.handler;

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.event.SnipeEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent.SniperCreateEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent.SniperDestroyEvent;
import com.voxelplugineering.voxelsniper.service.eventbus.EventHandler;
import com.voxelplugineering.voxelsniper.service.permission.PermissionProxy;
import com.voxelplugineering.voxelsniper.service.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.RayTrace;
import com.voxelplugineering.voxelsniper.util.math.Vector3d;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.queue.OfflineUndoHandler;

import com.google.common.eventbus.DeadEvent;

import java.io.IOException;

/**
 * An event handler for the default behavior for events.
 */
public class CommonEventHandler
{

    private final PlayerRegistry<?> players;
    private final OfflineUndoHandler undo;
    private final PermissionProxy perms;

//    private final String playerFolderName = this.conf.get("playerDataDirectory", String.class).or("players/");
//    private final String aliasFile = this.conf.get("aliasesFileName", String.class).or("aliases.json");

    /**
     * Constructs a new {@link CommonEventHandler}.
     * 
     * @param context The context
     */
    public CommonEventHandler(Context context)
    {
        this.players = context.getRequired(PlayerRegistry.class);
        this.undo = context.getRequired(OfflineUndoHandler.class);
        this.perms = context.getRequired(PermissionProxy.class);
    }

    /**
     * An event handler for the {@link SniperCreateEvent}s. This should initialize the players into
     * the player registry, creating their {@link Player} objects and setting their defaults.
     * 
     * @param event The event
     */
    @EventHandler
    public void onPlayerJoin(SniperEvent.SniperCreateEvent event)
    {
        try
        {
            event.getSniper().getAliasHandler().load();
        } catch (IOException e)
        {
            GunsmithLogger.getLogger().error(e, "Error loading player aliases");
        }
    }

    /**
     * An event handler for {@link SniperDestroyEvent} in order to save player-specific settings.
     * 
     * @param event The event
     */
    @EventHandler
    public void onPlayerLeave(SniperEvent.SniperDestroyEvent event)
    {
        Player player = event.getSniper();

        try
        {
            player.getAliasHandler().save();
        } catch (IOException e)
        {
            GunsmithLogger.getLogger().error(e, "Error saving player aliases");
        }

        this.undo.register(player.getName(), player.getUndoHistory());
        this.players.invalidate(player.getName());
    }

    /**
     * Processes the given {@link com.voxelplugineering.voxelsniper.event.SnipeEvent} and performs
     * all necessary checks of the event. This event handler is supports asynchronous callback.
     * 
     * @param event The snipe event to perform
     */
    @EventHandler
    public void onSnipe(SnipeEvent event)
    {
        Player sniper = event.getSniper();
        if (!this.perms.hasPermission(sniper, "voxelsniper.sniper"))
        {
            return;
        }
        boolean attemptedNullAction = false;
        try
        {
            Location location = sniper.getLocation();
            double yaw = event.getYaw();
            double pitch = event.getPitch();
            // TODO move min/max values form conf to world
            int minY = BaseConfiguration.minimumWorldDepth;
            int maxY = BaseConfiguration.maximumWorldHeight;
            double step = BaseConfiguration.rayTraceStep;
            Vector3d eyeOffs = new Vector3d(0, BaseConfiguration.playerEyeHeight, 0);
            double range = VoxelSniperConfiguration.rayTraceRange;
            if (sniper.getBrushVars().has(BrushKeys.RANGE))
            {
                range = sniper.getBrushVars().get(BrushKeys.RANGE, Double.class).get();
            }
            RayTrace ray = new RayTrace(location, yaw, pitch, range, minY, maxY, step, eyeOffs);
            ray.trace();

            if (ray.getTargetBlock() == null)
            {
                attemptedNullAction = true;
            }
            BrushVars vars = sniper.getBrushVars();
            vars.clearRuntime();
            vars.set(BrushContext.RUNTIME, BrushKeys.ORIGIN, location);
            vars.set(BrushContext.RUNTIME, BrushKeys.PLAYER_YAW, yaw);
            vars.set(BrushContext.RUNTIME, BrushKeys.PLAYER_PITCH, pitch);
            vars.set(BrushContext.RUNTIME, BrushKeys.TARGET_BLOCK, ray.getTargetBlock());
            vars.set(BrushContext.RUNTIME, BrushKeys.TARGET_FACE, ray.getTargetFace());
            vars.set(BrushContext.RUNTIME, BrushKeys.LAST_BLOCK, ray.getLastBlock());
            vars.set(BrushContext.RUNTIME, BrushKeys.LAST_FACE, ray.getLastFace());
            vars.set(BrushContext.RUNTIME, BrushKeys.ACTION, event.getAction());
            vars.set(BrushContext.RUNTIME, BrushKeys.LENGTH, ray.getLength());
//            Gunsmith.getLogger().info("Snipe at " + ray.getTargetBlock().getLocation().toString());
            sniper.getCurrentBrush().run(sniper, vars);
        } catch (Throwable e)
        {
            if (!attemptedNullAction)
            {
                sniper.sendMessage(VoxelSniperConfiguration.brushExecError);
                GunsmithLogger.getLogger().error(e, "Error executing brush");
            }
        }
    }

    /**
     * Reports on unhandled (aka. dead) events on the event bus.
     * 
     * @param deadEvent The dead event
     */
    @EventHandler
    public void handleDeadEvent(DeadEvent deadEvent)
    {
        Object event = deadEvent.getEvent();
        GunsmithLogger.getLogger().warn("An unhandled " + event.getClass().getName() + " event was posted to the event bus!");
    }
}
