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
package com.voxelplugineering.voxelsniper.brushes.hard;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.Futures;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.brushes.BrushNodeGraph;

/**
 * 
 */
public abstract class CallbackGraph extends BrushNodeGraph
{

    private Map<UUID, Queue<ShapeCallback>> pending;

    /**
     * @param name
     */
    public CallbackGraph(String name)
    {
        super(name);
        this.pending = Maps.newConcurrentMap();
    }

    protected Map<UUID, Queue<ShapeCallback>> getPending()
    {
        return this.pending;
    }

    /**
     * @param uuid
     * @param part
     * @return A {@link Future} representing the shape
     */
    public Future<MaterialShape> postPart(UUID uuid, MaterialShape part)
    {
        if (this.pending.containsKey(uuid))
        {
            ShapeCallback future = new ShapeCallback(part);
            this.pending.get(uuid).add(future);
            return future;
        }
        return Futures.immediateFuture(part);
    }

    /**
     * @param uuid
     */
    public void markDone(UUID uuid)
    {
        this.pending.remove(uuid);
    }

    /**
     * 
     */
    public static class ShapeCallback implements Future<MaterialShape>
    {

        private MaterialShape part;
        private boolean done = false;
        private boolean started = false;

        /**
         * @param part
         */
        public ShapeCallback(MaterialShape part)
        {
            this.part = part;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean cancel(boolean mayInterrupt)
        {
            if (this.started && !mayInterrupt)
            {
                return false;
            }
            this.done = true;
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MaterialShape get() throws InterruptedException
        {
            while (!this.done)
            {
                Thread.sleep(1);
            }
            if (isCancelled())
            {
                throw new CancellationException();
            }
            return this.part;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MaterialShape get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException
        {
            long start = System.currentTimeMillis();
            long delta = unit.toMillis(timeout);
            while (!this.done)
            {
                if (System.currentTimeMillis() - start > delta)
                {
                    throw new TimeoutException();
                } else
                {
                    Thread.sleep(1);
                }
            }
            if (isCancelled())
            {
                throw new CancellationException();
            }
            return this.part;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCancelled()
        {
            return this.done && !this.started;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isDone()
        {
            return this.done;
        }

        /**
         * Marks the task as done.
         */
        public void markDone()
        {
            this.done = true;
        }

        /**
         * Marks the task as started.
         * 
         * @return Was already cancelled
         */
        public boolean markStarted()
        {
            if (this.done)
            {
                return false;
            }
            this.started = true;
            return true;
        }

        /**
         * Gets the pending shape.
         * 
         * @return The shape
         */
        public MaterialShape getShape()
        {
            return this.part;
        }

    }
}
