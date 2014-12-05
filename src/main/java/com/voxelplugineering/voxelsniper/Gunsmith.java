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
package com.voxelplugineering.voxelsniper;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.thevoxelbox.vsl.api.IGraphCompilerFactory;
import com.thevoxelbox.vsl.classloader.GraphCompilerFactory;
import com.thevoxelbox.vsl.type.Type;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.api.IConfiguration;
import com.voxelplugineering.voxelsniper.api.ICustomInitManager;
import com.voxelplugineering.voxelsniper.api.ILogger;
import com.voxelplugineering.voxelsniper.api.ILoggingDistributor;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.config.Configuration;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.logging.CommonLoggingDistributor;
import com.voxelplugineering.voxelsniper.util.BrushCompiler;

/**
 * The Core of VoxelGunsmith, provides access to handlers and validates initialization is done completely and correctly.
 */
public final class Gunsmith implements ICustomInitManager
{

    /**
     * The internal event bus for events within Gunsmith.
     */
    private EventBus eventBus = null;
    /**
     * The global configuration container for Gunsmith.
     */
    private IConfiguration configuration = null;
    /**
     * The global log distributor for Gunsmith.
     */
    private static ILoggingDistributor logDistributor = null;
    /**
     * The VSL compiler factory for the Brush Managers to reference.
     */
    private IGraphCompilerFactory compilerFactory = null;
    /**
     * The initialization state of Gunsmith.
     */
    private boolean isPluginEnabled = false;


    /**
     * Returns the initialization state of Gunsmith.
     * 
     * @return the initialization state
     */
    public boolean isEnabled()
    {
        return isPluginEnabled;
    }

    /**
     * Gets the instance of the implementation of EventBus.
     *
     * @return The instance of the implementation of EventBus
     */
    public EventBus getEventBus()
    {
        return eventBus;
    }

    /**
     * Gets the instance of the implementation of IConfiguration.
     *
     * @return The instance of the implementation of IConfiguration
     */
    public IConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * Returns the global logger for Gunsmith.
     * 
     * @return the logger
     */
    public static ILogger getLogger()
    {
        return logDistributor;
    }

    /**
     * Returns the logging distribution manager for Gunsmith.
     * 
     * @return the logging distributor
     */
    public ILoggingDistributor getLoggingDistributor()
    {
        return logDistributor;
    }

    /**
     * Returns the compiler factory for compiling VSL scripts.
     * 
     * @return the factory
     */
    public IGraphCompilerFactory getCompilerFactory()
    {
        return compilerFactory;
    }

    /**
     * {@inheritDoc}
     */
    public void beginInit()
    {
        check();

        //Create standard log distributor
        logDistributor = new CommonLoggingDistributor();
        logDistributor.init();
        //TODO gunsmith log4j logger

        getLogger().info("Starting Gunsmith initialization process.");

        //Register vsl types for common impl types
        Type.registerType("COMMONBLOCK", "com/voxelplugineering/voxelsniper/common/CommonBlock");
        Type.registerType("COMMONLOCATION", "com/voxelplugineering/voxelsniper/common/CommonLocation");
        Type.registerType("COMMONMATERIAL", "com/voxelplugineering/voxelsniper/common/CommonMaterial");
        Type.registerType("COMMONVECTOR", "com/voxelplugineering/voxelsniper/common/CommonVector");
        Type.registerType("COMMONWORLD", "com/voxelplugineering/voxelsniper/common/CommonWorld");
        Type.registerType("SHAPE", "com/voxelplugineering/voxelsniper/shape/Shape");

        //Create the eventBus for all Gunsmith events
        eventBus = new AsyncEventBus(java.util.concurrent.Executors.newCachedThreadPool());

        configuration = new Configuration();
        configuration.registerContainer(BaseConfiguration.class);
        configuration.registerContainer(VoxelSniperConfiguration.class);
        //configuration is also setup here so that any values can be overwritten from the specific impl

        //Setup the VSL graph compiler
        compilerFactory = new GraphCompilerFactory();
        compilerFactory.registerCompiler(IBrush.class, new BrushCompiler()); //the compiler for all brushes
    }

    /**
     * {@inheritDoc}
     */
    public void finishInit()
    {
        check();
        getLogger().info("Gunsmith initialization finalized.");
        isPluginEnabled = true;
    }

    /**
     * Checks that Gunsmith is still in the initialization phase.
     */
    private void check()
    {
        if (isPluginEnabled)
        {
            throw new IllegalStateException("VoxelSniper is already enabled!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stop()
    {
        if (!isPluginEnabled)
        {
            throw new IllegalStateException("VoxelSniper has not been enabled yet, cannot stop!");
        }
        isPluginEnabled = false;
        eventBus = null;
        if (logDistributor != null)
        {
            logDistributor.stop();
        }
        logDistributor = null;
    }

}
