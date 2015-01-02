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
package com.voxelplugineering.voxelsniper.api.platform;

import com.voxelplugineering.voxelsniper.api.brushes.BrushLoader;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.logging.ILogger;
import com.voxelplugineering.voxelsniper.api.permissions.PermissionProxy;
import com.voxelplugineering.voxelsniper.api.registry.BiomeRegistry;
import com.voxelplugineering.voxelsniper.api.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.api.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.api.registry.WorldRegistry;
import com.voxelplugineering.voxelsniper.api.service.scheduler.Scheduler;
import com.voxelplugineering.voxelsniper.command.CommandHandler;

/**
 * A provider for platform specific values during initialization.
 */
public interface PlatformProvider
{

    /**
     * Gets the platforms logger.
     * 
     * @return The logger
     */
    ILogger getLogger();

    /**
     * Gets the name of the platforms logger.
     * 
     * @return The logger name
     */
    String getLoggerName();

    /**
     * Gets the platform access proxy.
     * 
     * @return The platform proxy
     */
    PlatformProxy getPlatformProxy();

    /**
     * Register any platform specific configuration values.
     */
    void registerConfiguration();

    /**
     * Gets the default material registry.
     * 
     * @return The material registry
     */
    MaterialRegistry<?> getDefaultMaterialRegistry();

    /**
     * Gets the world registry.
     * 
     * @return The world registry
     */
    WorldRegistry<?> getWorldRegistry();

    /**
     * Gets the permission proxy.
     * 
     * @return The permission proxy
     */
    PermissionProxy getPermissionProxy();

    /**
     * Gets the Sniper registry.
     * 
     * @return The sniper registry
     */
    PlayerRegistry<?> getSniperRegistry();

    /**
     * Sets up any events required within the specific implementation.
     */
    void registerEventProxies();

    /**
     * Gets the default brush loader.
     * 
     * @return The default brush loader
     */
    BrushLoader getDefaultBrushLoader();

    /**
     * Gets the global brush manager.
     * 
     * @return The global brush manager
     */
    BrushManager getGlobalBrushManager();

    /**
     * Gets the command handler.
     * 
     * @return The command handler
     */
    CommandHandler getCommandHandler();

    /**
     * Gets the scheduler proxy.
     * 
     * @return The scheduler proxy
     */
    Scheduler getSchedulerProxy();

    /**
     * Gets the biome registry.
     * 
     * @return The biome registry
     */
    BiomeRegistry<?> getBiomeRegistry();

}
