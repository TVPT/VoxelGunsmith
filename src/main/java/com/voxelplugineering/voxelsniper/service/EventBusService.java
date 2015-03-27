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

import com.google.common.util.concurrent.ListenableFuture;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.event.bus.EventBus;
import com.voxelplugineering.voxelsniper.service.eventbus.Event;

/**
 * A service containing an event bus.
 */
public class EventBusService extends AbstractService implements EventBus
{

    private final EventBus wrapped;

    /**
     * Create a new {@link EventBusService}
     * 
     * @param bus The event bus to use within this service
     */
    public EventBusService(EventBus bus)
    {
        super(2);
        this.wrapped = bus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "eventBus";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init()
    {
        this.wrapped.init();
        Gunsmith.getLogger().info("Initialized EventBus service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy()
    {
        this.wrapped.destroy();
        Gunsmith.getLogger().info("Stopping EventBus service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(Object eventHandler)
    {
        check();
        this.wrapped.register(eventHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister(Object eventHandler)
    {
        check();
        this.wrapped.unregister(eventHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListenableFuture<Event> post(Event event)
    {
        check();
        return this.wrapped.post(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists()
    {
        return this.wrapped.exists();
    }

}
