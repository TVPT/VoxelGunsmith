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

import static com.google.common.base.Preconditions.checkArgument;
import static com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy.ThreadingPolicy.ASYNCHRONOUS;

import com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy;

/**
 * An event which wraps another event to indicate that it was 'dead'. An event
 * being dead means that it was posted but no handlers for it were registered.
 */
@EventThreadingPolicy(ASYNCHRONOUS)
public class DeadEvent extends Event
{

    private final Event event;

    /**
     * Creates a new {@link DeadEvent} wrapping the given event.
     * 
     * @param event The event to wrap
     */
    public DeadEvent(Event event)
    {
        checkArgument(!event.getClass().equals(DeadEvent.class));
        this.event = event;
    }

    /**
     * Gets the dead event.
     * 
     * @return The event
     */
    public Event getDeadEvent()
    {
        return this.event;
    }

}
