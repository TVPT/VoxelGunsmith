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
package com.voxelplugineering.voxelsniper.service.scheduler;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.service.Service;

/**
 * A proxy for a specific implementations scheduler.
 */
public interface Scheduler extends Service
{

    /**
     * Starts a new task synchronized to the main thread of the underlying platform.
     * 
     * @param runnable The task runnable, cannot be null
     * @param interval The interval, in milliseconds
     * @return The new task
     */
    Optional<? extends Task> startSynchronousTask(Runnable runnable, int interval);

    /**
     * Starts a new task asynchronously.
     * 
     * @param runnable The task runnable
     * @param interval The interval, in milliseconds
     * @return The new task
     */
    Optional<? extends Task> startAsynchronousTask(Runnable runnable, int interval);

    /**
     * Halts all the currently running tasks.
     */
    void stopAllTasks();

    /**
     * Gets all currently running tasks.
     * 
     * @return The tasks
     */
    Iterable<? extends Task> getAllTasks();

}
