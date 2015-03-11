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
package com.voxelplugineering.voxelsniper.registry;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.commands.CommandSender;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.api.registry.RegistryProvider;
import com.voxelplugineering.voxelsniper.api.service.AbstractService;

/**
 * A standard player registry for players.
 * 
 * @param <T> The underlying player type
 */
public class CommonPlayerRegistry<T> extends AbstractService implements PlayerRegistry<T>
{

    private ProvidedWeakRegistry<T, Player> registry;
    private final CommandSender console;
    private final RegistryProvider<T, Player> provider;

    /**
     * Creates a new {@link CommonPlayerRegistry}.
     * 
     * @param provider The provider for getting new players
     * @param console The console sender proxy
     */
    public CommonPlayerRegistry(RegistryProvider<T, Player> provider, CommandSender console)
    {
        super(8);
        this.console = console;
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "playerRegistry";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init()
    {
        this.registry = new ProvidedWeakRegistry<T, Player>(this.provider);
        Gunsmith.getLogger().info("Initialized PlayerRegistry service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroy()
    {
        this.registry = null;
        Gunsmith.getLogger().info("Stopped PlayerRegistry service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandSender getConsoleSniperProxy()
    {
        return this.console;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> getPlayer(String name)
    {
        check();
        return this.registry.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> getPlayer(T player)
    {
        check();
        return this.registry.get(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player[] getAllPlayers()
    {
        check();
        Set<Map.Entry<T, Player>> playerSet = this.registry.getRegisteredValues();
        Player[] players = new Player[playerSet.size()];
        int i = 0;
        for (Map.Entry<T, Player> e : playerSet)
        {
            players[i++] = e.getValue();
        }
        return players;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String name)
    {
        this.registry.remove(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(T player)
    {
        this.registry.remove(player);
    }

}
