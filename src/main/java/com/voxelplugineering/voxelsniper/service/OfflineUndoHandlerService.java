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

import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.world.queue.OfflineUndoHandler;
import com.voxelplugineering.voxelsniper.world.queue.UndoQueue;

/**
 * A standard offline undo handler which caches player {@link UndoQueue}s for a period of 60 minutes
 * before discarding.
 */
public class OfflineUndoHandlerService extends AbstractService implements OfflineUndoHandler
{

    private Cache<String, UndoQueue> cache;

    /**
     * Creates a new {@link OfflineUndoHandlerService}.
     */
    public OfflineUndoHandlerService(Context context)
    {
        super(context);
    }

    @Override
    protected void _init()
    {
        this.cache = CacheBuilder.newBuilder().expireAfterAccess(60, TimeUnit.MINUTES).build();
    }

    @Override
    protected void _shutdown()
    {
        this.cache = null;
    }

    @Override
    public void register(String name, UndoQueue undo)
    {
        check("register");
        this.cache.put(name, undo);
    }

    @Override
    public Optional<UndoQueue> get(String name)
    {
        check("get");
        return Optional.fromNullable(this.cache.getIfPresent(name));
    }

    @Override
    public void invalidate(String name)
    {
        check("invalidate");
        this.cache.invalidate(name);
    }

}
