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


import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.Service;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;

import java.util.Optional;

/**
 * A factory for creating instances of {@link Player} from the specific implementation's user class.
 * 
 * @param <T> the underlying Player class
 */
public interface PlayerRegistry<T> extends Service
{

    /**
     * Returns a special case {@link Player} for the console of the dedicated server. Has no brush
     * context but can recieve messages.
     * 
     * @return An {@link Player} representing the console
     */
    CommandSender getConsoleSniperProxy();

    /**
     * Gets the player with the given name.
     * 
     * @param name The name to fetch
     * @return The player
     */
    Optional<Player> getPlayer(String name);

    /**
     * Gets the player representing the given underlying player object.
     * 
     * @param player The underlying player object
     * @return The gunsmith player object
     */
    Optional<Player> getPlayer(T player);

    /**
     * Gets a collection of all players registered within this registry.
     * 
     * @return The players
     */
    Iterable<Player> getPlayers();

    /**
     * Removes the player from the registry by name.
     * 
     * @param name The name
     */
    void invalidate(String name);

    /**
     * Removes the given player from the registry.
     * 
     * @param player The key
     */
    void invalidate(T player);

}
