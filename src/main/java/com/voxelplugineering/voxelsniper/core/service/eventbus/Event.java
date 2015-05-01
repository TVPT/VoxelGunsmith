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
package com.voxelplugineering.voxelsniper.core.service.eventbus;

import com.voxelplugineering.voxelsniper.api.event.Cancelable;
import com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy;
import com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy.ThreadingPolicy;
import com.voxelplugineering.voxelsniper.core.util.AnnotationHelper;

/**
 * The base event class.
 */
public abstract class Event
{

    private final boolean isCancelable;
    private final ThreadingPolicy allowsAsync;
    private boolean isCanceled = false;

    /**
     * Sets up the event attributes.
     */
    public Event()
    {
        this.isCancelable = AnnotationHelper.doesSuperHaveAnotation(this.getClass(), Cancelable.class);
        EventThreadingPolicy policy = AnnotationHelper.getSuperAnnotation(this.getClass(), EventThreadingPolicy.class).orNull();
        if (policy != null)
        {
            this.allowsAsync = policy.value();
        } else
        {
            this.allowsAsync = ThreadingPolicy.ASYNCHRONOUS_SEQUENTIAL;
        }
    }

    /**
     * Gets if this event may be canceled.
     * 
     * @return Is cancelable
     */
    public boolean isCancelable()
    {
        return this.isCancelable;
    }

    /**
     * Gets the {@link ThreadingPolicy} for the delegation of this event to its handlers.
     * 
     * @return The threading policy
     */
    public ThreadingPolicy getThreadingPolicy()
    {
        return this.allowsAsync;
    }

    /**
     * Gets if this event has been canceled.
     * 
     * @return Is canceled
     */
    public boolean isCanceled()
    {
        return this.isCanceled;
    }

    /**
     * Sets the cancellation state of this event.
     * 
     * @param canceled The new cancellation state
     */
    public void setCanceled(boolean canceled)
    {
        if (!this.isCancelable)
        {
            throw new IllegalArgumentException("Attempted to cancel a uncancelable event");
        }
        this.isCanceled = canceled;
    }

}
