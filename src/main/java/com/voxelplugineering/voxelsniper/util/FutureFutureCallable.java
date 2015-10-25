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
package com.voxelplugineering.voxelsniper.util;

import com.voxelplugineering.voxelsniper.service.event.Event;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * A {@link Callable} which waits for all {@link Future}s from a collection of futures to complete.
 */
public class FutureFutureCallable implements Callable<Event>
{

    private final Collection<Future<Event>> watchList;
    private final Event returnValue;

    /**
     * Creates a new {@link FutureFutureCallable}.
     * 
     * @param futures The collection of futures to watch
     * @param event The event to return once the futures are complete
     */
    public FutureFutureCallable(Collection<Future<Event>> futures, Event event)
    {
        this.watchList = futures;
        this.returnValue = event;
    }

    @Override
    public Event call() throws Exception
    {
        for (Iterator<Future<Event>> it = this.watchList.iterator(); it.hasNext();)
        {
            Future<Event> next = it.next();
            try
            {
                next.get();
            } catch (Exception ignored)
            {
            } finally
            {
                it.remove();
            }
        }
        return this.returnValue;
    }

}
