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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.ExecutorService;

/**
 * Annotates an event to specify the threading policy that should be followed by {@link EventBus}s
 * handling of the event when it is posted to the bus.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface EventThreadingPolicy
{

    /**
     * Gets the threading policy for the event.
     */
    ThreadingPolicy value() default ThreadingPolicy.ASYNCHRONOUS_SEQUENTIAL;

    /**
     * Possible threading policies for an event post. <ul> <li>SYNCHRONIZED: handlers will be called
     * from the posting thread in sequential order.</li> <li>ASYNCHRONOUS_SEQUENTIAL: handlers will
     * be called asynchronously to the posting thread, handlers will be called sequentially.</li>
     * <li>ASYNCHRONOUS: handlers will be called asynchronously to the posting thread, depending on
     * if the {@link EventBus}'s {@link ExecutorService} has enough threads available, the handlers
     * may or may not be executed sequentially.</li> </ul>
     */
    @SuppressWarnings("javadoc")
    public static enum ThreadingPolicy
    {
        SYNCHRONIZED,
        ASYNCHRONOUS_SEQUENTIAL,
        ASYNCHRONOUS
    }

}
