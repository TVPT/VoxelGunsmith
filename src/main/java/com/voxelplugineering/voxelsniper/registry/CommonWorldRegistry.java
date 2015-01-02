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

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.registry.RegistryProvider;
import com.voxelplugineering.voxelsniper.api.registry.WorldRegistry;
import com.voxelplugineering.voxelsniper.api.world.World;

/**
 * A standard world registry.
 * 
 * @param <T> The underlying world type
 */
public class CommonWorldRegistry<T> implements WorldRegistry<T>
{

    private final ProvidedWeakRegistry<T, World> registry;

    /**
     * Creates a new {@link CommonWorldRegistry}.
     * 
     * @param provider The provider for new worlds
     */
    public CommonWorldRegistry(RegistryProvider<T, World> provider)
    {
        this.registry = new ProvidedWeakRegistry<T, World>(provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<World> getWorld(String name)
    {
        return this.registry.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<World> getWorld(T world)
    {
        return this.registry.get(world);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<World> getLoadedWorlds()
    {
        return this.registry.getRegisteredValues();
    }

}
