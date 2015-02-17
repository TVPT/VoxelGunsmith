package com.voxelplugineering.voxelsniper.api.world.queue;

import com.google.common.base.Optional;

/**
 * A handler for {@link UndoQueue}s for users who have disconnected from the
 * system. These {@link UndoQueue}s will be invalidated depending on the
 * implementing class's policy.
 */
public interface OfflineUndoHandler
{

    /**
     * Registers a new {@link UndoQueue} under the given key.
     * 
     * @param key The key
     * @param undo The queue
     */
    void register(String key, UndoQueue undo);

    /**
     * Gets the {@link UndoQueue} for the given key, if available.
     * 
     * @param key The key
     * @return The undo queue, if available
     */
    Optional<UndoQueue> get(String key);

    /**
     * Invalidates the given key and drops the associated {@link UndoQueue} from
     * the handler if it exists.
     * 
     * @param key The key
     */
    void invalidate(String key);

}
