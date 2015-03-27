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
package com.voxelplugineering.voxelsniper.service.eventbus;

import java.lang.reflect.Method;

import com.voxelplugineering.voxelsniper.api.event.EventPriority;

/**
 * An event subscriber with a method callback.
 */
public class Subscriber
{

    private final Object container;
    private final Method exec;
    private final Class<? extends Event> eventType;
    private final EventPriority priority;

    /**
     * Creates a new {@link Subscriber}.
     * 
     * @param container The callback object
     * @param exec The callback method
     * @param eventType The event type
     * @param priority The event priority
     */
    public Subscriber(Object container, Method exec, Class<? extends Event> eventType, EventPriority priority)
    {
        this.container = container;
        this.exec = exec;
        this.eventType = eventType;
        this.priority = priority;
        this.exec.setAccessible(true);
    }

    /**
     * Gets the callback object.
     * 
     * @return The object
     */
    public Object getContainer()
    {
        return this.container;
    }

    /**
     * Gets the callback method.
     * 
     * @return The method
     */
    public Method getMethod()
    {
        return this.exec;
    }

    /**
     * Gets the event type this subscriber is registered to.
     * 
     * @return The event type
     */
    public Class<? extends Event> getEventType()
    {
        return this.eventType;
    }

    /**
     * Gets the event priority.
     * 
     * @return The priority
     */
    public EventPriority getPriority()
    {
        return this.priority;
    }

    /**
     * @return The string representation
     */
    public String toString()
    {
        return "Subscriber " + this.exec.getName() + " (" + this.eventType.getName() + " - " + this.priority.name() + ")";
    }
}
