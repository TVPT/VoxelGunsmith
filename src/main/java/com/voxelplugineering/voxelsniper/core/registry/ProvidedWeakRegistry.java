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
package com.voxelplugineering.voxelsniper.core.registry;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.registry.RegistryProvider;
import com.voxelplugineering.voxelsniper.core.util.Pair;

/**
 * A {@link WeakRegistry} which takes a custom provider which is referenced to
 * get the value when a key is not found within the registry.
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public class ProvidedWeakRegistry<K, V> extends WeakRegistry<K, V>
{

    /**
     * The provider.
     */
    private RegistryProvider<K, V> provider;

    /**
     * Creates a new {@link ProvidedWeakRegistry}.
     * 
     * @param provider the provider
     */
    public ProvidedWeakRegistry(RegistryProvider<K, V> provider)
    {
        super();
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    public Optional<V> get(String name)
    {
        Optional<V> value = super.get(name);
        if (!value.isPresent())
        {
            Optional<Pair<K, V>> v = this.provider.get(name);
            if (v.isPresent())
            {
                register(name, v.get().getKey(), v.get().getValue());
                return Optional.of(v.get().getValue());
            }
            return Optional.absent();
        }
        return value;
    }

    /**
     * Sets the provider for this registry.
     * 
     * @param provider the new provider
     */
    public void setProvider(RegistryProvider<K, V> provider)
    {
        this.provider = provider;
    }

}
