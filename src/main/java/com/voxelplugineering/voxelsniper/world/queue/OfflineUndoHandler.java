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
package com.voxelplugineering.voxelsniper.world.queue;


import com.voxelplugineering.voxelsniper.service.Service;

import java.util.Optional;

/**
 * A handler for {@link UndoQueue}s for users who have disconnected from the system. These
 * {@link UndoQueue}s will be invalidated depending on the implementing class's policy.
 */
public interface OfflineUndoHandler extends Service
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
     * Invalidates the given key and drops the associated {@link UndoQueue} from the handler if it
     * exists.
     * 
     * @param key The key
     */
    void invalidate(String key);

}
