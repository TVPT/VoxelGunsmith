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
package com.voxelplugineering.voxelsniper.core.service;

import com.voxelplugineering.voxelsniper.api.service.Service;

/**
 * An abstract {@link Service} handling state and priority.
 */
public abstract class AbstractService implements Service
{

    private boolean started = false;
    private final int priority;

    /**
     * Initializes {@link Service}
     * 
     * @param priority The service priority
     */
    public AbstractService(int priority)
    {
        this.priority = priority;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPriority()
    {
        return this.priority;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isStarted()
    {
        return this.started;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void start()
    {
        if (this.started)
        {
            throw new IllegalStateException();
        }
        this.started = true;
        init();
    }

    /**
     * Initializes the service.
     */
    protected abstract void init();

    /**
     * {@inheritDoc}
     */
    @Override
    public final void stop()
    {
        if (!this.started)
        {
            throw new IllegalStateException("Service " + getName() + " was attempted to be stopped while not started!");
        }
        destroy();
        this.started = false;
    }

    /**
     * Performs shutdown tasks.
     */
    protected abstract void destroy();

    /**
     * Validates that service is started.
     */
    public void check()
    {
        if (!this.started)
        {
            throw new IllegalStateException("Tried to perform operation against " + getName() + " while it was stopped.");
        }
    }

}
