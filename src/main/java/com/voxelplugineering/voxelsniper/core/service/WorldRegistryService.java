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
package com.voxelplugineering.voxelsniper.core.service;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.voxelplugineering.voxelsniper.api.registry.RegistryProvider;
import com.voxelplugineering.voxelsniper.api.registry.WorldRegistry;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.core.Gunsmith;
import com.voxelplugineering.voxelsniper.core.registry.ProvidedWeakRegistry;

/**
 * A standard world registry.
 * 
 * @param <T> The underlying world type
 */
public class WorldRegistryService<T> extends AbstractService implements WorldRegistry<T>
{

    private ProvidedWeakRegistry<T, World> registry;
    private final RegistryProvider<T, World> provider;

    /**
     * Creates a new {@link WorldRegistryService}.
     * 
     * @param provider The provider for new worlds
     */
    public WorldRegistryService(RegistryProvider<T, World> provider)
    {
        super(WorldRegistry.class, 6);
        this.provider = provider;
    }

    @Override
    public String getName()
    {
        return "worldRegistry";
    }

    @Override
    protected void init()
    {
        this.registry = new ProvidedWeakRegistry<T, World>(this.provider);
        Gunsmith.getLogger().info("Initialized WorldRegistry service");
    }

    @Override
    protected void destroy()
    {
        this.registry = null;
        Gunsmith.getLogger().info("Stopped WorldRegistry service");
    }

    @Override
    public Optional<World> getWorld(String name)
    {
        check();
        return this.registry.get(name);
    }

    @Override
    public Optional<World> getWorld(T world)
    {
        check();
        return this.registry.get(world);
    }

    @Override
    public Iterable<World> getLoadedWorlds()
    {
        check();
        List<World> worlds = Lists.newArrayList();
        for (Map.Entry<T, World> e : this.registry.getRegisteredValues())
        {
            worlds.add(e.getValue());
        }
        return worlds;
    }

    @Override
    public void invalidate(String name)
    {
        check();
        this.registry.remove(name);
    }

    @Override
    public void invalidate(T world)
    {
        check();
        this.registry.remove(world);
    }

}
