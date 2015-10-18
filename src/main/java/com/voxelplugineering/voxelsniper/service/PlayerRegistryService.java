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

import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.registry.ProvidedWeakRegistry;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.service.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.service.registry.RegistryProvider;
import com.voxelplugineering.voxelsniper.util.Context;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A standard player registry for players.
 * 
 * @param <T> The underlying player type
 */
public class PlayerRegistryService<T> extends AbstractService implements PlayerRegistry<T>
{

    private ProvidedWeakRegistry<T, Player> registry;
    private final CommandSender console;
    private final RegistryProvider<T, Player> provider;

    /**
     * Creates a new {@link PlayerRegistryService}.
     * 
     * @param context The context
     * @param provider The provider for getting new players
     * @param console The console sender proxy
     */
    public PlayerRegistryService(Context context, RegistryProvider<T, Player> provider, CommandSender console)
    {
        super(context);
        this.console = console;
        this.provider = provider;
    }

    @Override
    protected void _init()
    {
        this.registry = new ProvidedWeakRegistry<T, Player>(this.provider);
    }

    @Override
    protected void _shutdown()
    {
        this.registry = null;
    }

    @Override
    public CommandSender getConsoleSniperProxy()
    {
        return this.console;
    }

    @Override
    public Optional<Player> getPlayer(String name)
    {
        check("getPlayer");
        return this.registry.get(name);
    }

    @Override
    public Optional<Player> getPlayer(T player)
    {
        check("getPlayer");
        return this.registry.get(player);
    }

    @Override
    public Iterable<Player> getPlayers()
    {
        check("getPlayers");
        List<Player> players = Lists.newArrayList();
        for (Map.Entry<T, Player> e : this.registry.getRegisteredValues())
        {
            players.add(e.getValue());
        }
        return players;
    }

    @Override
    public void invalidate(String name)
    {
        check("invalidate");
        this.registry.remove(name);
    }

    @Override
    public void invalidate(T player)
    {
        check("invalidate");
        this.registry.remove(player);
    }

}
