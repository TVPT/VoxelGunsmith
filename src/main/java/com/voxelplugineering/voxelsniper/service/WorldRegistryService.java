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

import com.google.common.collect.Lists;
import com.voxelplugineering.voxelsniper.registry.ProvidedWeakRegistry;
import com.voxelplugineering.voxelsniper.service.registry.RegistryProvider;
import com.voxelplugineering.voxelsniper.service.registry.WorldRegistry;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.world.World;

import java.util.Optional;

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
     * @param context The context
     * @param provider The provider for new worlds
     */
    public WorldRegistryService(Context context, RegistryProvider<T, World> provider)
    {
        super(context);
        this.provider = provider;
    }

    @Override
    protected void _init()
    {
        this.registry = new ProvidedWeakRegistry<T, World>(this.provider);
    }

    @Override
    protected void _shutdown()
    {
        this.registry = null;
    }

    @Override
    public Optional<World> getWorld(String name)
    {
        check("getWorld");
        return this.registry.get(name);
    }

    @Override
    public Optional<World> getWorld(T world)
    {
        check("getWorld");
        return this.registry.get(world);
    }

    @Override
    public Iterable<World> getLoadedWorlds()
    {
        check("getLoadedWorlds");
        return Lists.newArrayList(this.registry.values());
    }

    @Override
    public void invalidate(String name)
    {
        check("invalidate");
        this.registry.remove(name);
    }

    @Override
    public void invalidate(T world)
    {
        check("invalidate");
        this.registry.remove(world);
    }

}
