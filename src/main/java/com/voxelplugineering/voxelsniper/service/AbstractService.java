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
package com.voxelplugineering.voxelsniper.service;

import java.util.List;

import com.google.common.collect.Lists;
import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.util.Context;

/**
 * An abstract {@link Service} handling state and priority.
 */
public abstract class AbstractService implements Service
{

    private boolean initialized;
    private List<Service> dependants = Lists.newArrayList();

    /**
     * Define a constructor in order to force needing to pass in a Context to all services.
     * 
     * @param context The service context
     */
    public AbstractService(Context context)
    {

    }

    @Override
    public final synchronized void start()
    {
        if (this.initialized)
        {
            throw new IllegalStateException("Cannot initialize and already initialized service.");
        }
        this.initialized = true;
        _init();
        GunsmithLogger.getLogger().info("Started " + getClass().getName() + " service.");
    }

    /**
     * Initializes the service.
     */
    protected abstract void _init();

    @Override
    public final synchronized void shutdown()
    {
        for (Service s : this.dependants)
        {
            s.shutdown();
        }
        _shutdown();
        GunsmithLogger.getLogger().info("Stopped " + getClass().getName() + " service.");
        this.initialized = false;
    }

    /**
     * Shuts down the service.
     */
    protected abstract void _shutdown();

    @Override
    public final boolean isInitialized()
    {
        return this.initialized;
    }

    /**
     * Checks that the service is started and throws an {@link IllegalStateException} with the given
     * message if it is not.
     *
     * @param msg The message to use on error
     */
    protected void check(String msg)
    {
        if (!this.initialized)
        {
            throw new IllegalStateException("Attempted to " + msg + " while service was uninitialized.");
        }
    }

    @Override
    public <T extends Service> void addDependent(T service)
    {
        this.dependants.add(service);
    }

}
