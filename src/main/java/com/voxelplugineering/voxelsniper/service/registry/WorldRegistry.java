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
package com.voxelplugineering.voxelsniper.service.registry;

import com.voxelplugineering.voxelsniper.service.Service;
import com.voxelplugineering.voxelsniper.world.World;

import java.util.Optional;

/**
 * A registry for world data.
 * 
 * @param <T> The underlying world type.
 */
public interface WorldRegistry<T> extends Service
{

    /**
     * Gets the world with the given name.
     * 
     * @param name The name
     * @return The world, if found
     */
    Optional<World> getWorld(String name);

    /**
     * Gets the world representing the given underlying world object.
     * 
     * @param world The underlying world type
     * @return The world, if found
     */
    Optional<World> getWorld(T world);

    /**
     * Gets a collection of all currently registered worlds.
     * 
     * @return The worlds
     */
    Iterable<World> getLoadedWorlds();

    /**
     * Removes the world from the registry by name.
     * 
     * @param name The name
     */
    void invalidate(String name);

    /**
     * Removes the given world from the registry.
     * 
     * @param world The key
     */
    void invalidate(T world);

}
