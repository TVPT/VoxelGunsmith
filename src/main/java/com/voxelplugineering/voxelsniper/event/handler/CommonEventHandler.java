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

import java.io.File;
import java.io.IOException;

import com.google.common.eventbus.DeadEvent;
import com.thevoxelbox.vsl.api.variables.VariableScope;
import com.thevoxelbox.vsl.variables.ParentedVariableScope;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.event.EventHandler;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.event.SnipeEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent.SniperCreateEvent;
import com.voxelplugineering.voxelsniper.event.SniperEvent.SniperDestroyEvent;
import com.voxelplugineering.voxelsniper.service.persistence.JsonDataSource;
import com.voxelplugineering.voxelsniper.util.RayTrace;

/**
 * An event handler for the default behavior for events.
 */
public class CommonEventHandler
{

    private final String playerFolderName = Gunsmith.getConfiguration().get("playerDataDirectory", String.class).or("players/");
    private final String aliasFile = Gunsmith.getConfiguration().get("aliasesFileName", String.class).or("aliases.json");
    private final String playerSysvar = Gunsmith.getConfiguration().get("playerSysVarName", String.class).or("__PLAYER__");

    /**
     * Constructs a new CommonEventHandler
     */
    public CommonEventHandler()
    {

    }

    /**
     * An event handler for the {@link SniperCreateEvent}s. This should
     * initialize the players into the player registry, creating their
     * {@link Player} objects and setting their defaults.
     * 
     * @param event the event
     */
    @EventHandler
    public void onPlayerJoin(SniperEvent.SniperCreateEvent event)
    {
        Player player = event.getSniper();
        // TODO use UUID for directory name
        File playerFolder = new File(Gunsmith.getPlatformProxy().getDataFolder(), this.playerFolderName + player.getName());
        playerFolder.mkdirs();
        File aliases = new File(playerFolder, this.aliasFile);
        JsonDataSource data = new JsonDataSource(aliases);
        if (aliases.exists())
        {
            try
            {
                data.read(player.getPersonalAliasHandler());
            } catch (IOException e)
            {
                Gunsmith.getLogger().error(e, "Error loading player aliases!");
            }
        }
        Gunsmith.getOfflineUndoHandler().invalidate(player.getName());
    }

    /**
     * An event handler for {@link SniperDestroyEvent} in order to save
     * player-specific settings.
     * 
     * @param event the event
     */
    @EventHandler
    public void onPlayerLeave(SniperEvent.SniperDestroyEvent event)
    {
        Player player = event.getSniper();
        File playerFolder = new File(Gunsmith.getPlatformProxy().getDataFolder(), this.playerFolderName + player.getUniqueId().toString());
        File aliases = new File(playerFolder, this.aliasFile);
        JsonDataSource data = new JsonDataSource(aliases);

        try
        {
            if (aliases.exists())
            {
                aliases.createNewFile();
            }
            data.write(player.getPersonalAliasHandler());
        } catch (IOException e)
        {
            Gunsmith.getLogger().error(e, "Error saving player aliases!");
        }

        Gunsmith.getOfflineUndoHandler().register(player.getName(), player.getUndoHistory());
    }

    /**
     * Processes the given
     * {@link com.voxelplugineering.voxelsniper.event.SnipeEvent} and performs
     * all necessary checks of the event. This event handler is supports
     * asynchronous callback.
     * 
     * @param event the snipe event to perform
     */
    @EventHandler
    public void onSnipe(SnipeEvent event)
    {
        Player sniper = event.getSniper();
        boolean attemptedNullAction = false;
        try
        {
            Location location = sniper.getLocation();
            double yaw = event.getYaw();
            double pitch = event.getPitch();
            RayTrace ray = new RayTrace(location, yaw, pitch);
            double range = (Double) Gunsmith.getConfiguration().get("rayTraceRange").or(250.0);
            if (sniper.getBrushSettings().hasValue("range"))
            {
                range = sniper.getBrushSettings().get("range", Double.class).get();
            }
            ray.setRange(range);
            ray.trace();

            if (ray.getTargetBlock() == null)
            {
                attemptedNullAction = true;
            }

            VariableScope brushVariables = new ParentedVariableScope(sniper.getBrushSettings());
            brushVariables.setCaseSensitive(false);
            brushVariables.set("origin", location);// TODO move all variable
                                                   // names to configuration
            brushVariables.set("yaw", yaw);
            brushVariables.set("pitch", pitch);
            brushVariables.set("targetBlock", ray.getTargetBlock());
            // brushVariables.set("lastBlock", ray.getLastBlock());
            // TODO support gunpoweder alt action
            brushVariables.set("length", ray.getLength());
            brushVariables.set(this.playerSysvar, sniper);
            Gunsmith.getLogger().info("Snipe at " + ray.getTargetBlock().getLocation().toString());
            sniper.getCurrentBrush().run(brushVariables, sniper.getBrushArguments());
        } catch (Exception e)
        {
            if (!attemptedNullAction)
            {
                sniper.sendMessage("Error executing brush, see console for more details.");
                Gunsmith.getLogger().error(e, "Error executing brush");
            }
        }
    }

    /**
     * Reports on unhandled (aka. dead) events on the event bus.
     * 
     * @param deadEvent the dead event
     */
    @EventHandler
    public void handleDeadEvent(DeadEvent deadEvent)
    {
        Object event = deadEvent.getEvent();
        Gunsmith.getLogger().warn("An unhandled " + event.getClass().getName() + " event was posted to the event bus!");
    }
}
