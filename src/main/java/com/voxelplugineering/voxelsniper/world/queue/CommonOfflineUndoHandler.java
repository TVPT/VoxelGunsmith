package com.voxelplugineering.voxelsniper.world.queue;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.voxelplugineering.voxelsniper.api.world.queue.OfflineUndoHandler;
import com.voxelplugineering.voxelsniper.api.world.queue.UndoQueue;

/**
 * A standard offline undo handler which caches player {@link UndoQueue}s for a
 * period of 60 minutes before discarding.
 */
public class CommonOfflineUndoHandler implements OfflineUndoHandler
{

    private final Cache<String, UndoQueue> cache;

    /**
     * Creates a new {@link CommonOfflineUndoHandler}.
     */
    public CommonOfflineUndoHandler()
    {
        this.cache = CacheBuilder.newBuilder().expireAfterAccess(60, TimeUnit.MINUTES).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(String name, UndoQueue undo)
    {
        this.cache.put(name, undo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UndoQueue> get(String name)
    {
        return Optional.fromNullable(this.cache.getIfPresent(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invalidate(String name)
    {
        this.cache.invalidate(name);
    }

}
