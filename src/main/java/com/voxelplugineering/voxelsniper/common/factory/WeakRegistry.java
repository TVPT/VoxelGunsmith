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
package com.voxelplugineering.voxelsniper.common.factory;

import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.voxelplugineering.voxelsniper.api.IRegistry;

/**
 * A registry for values which are referenced both by a name and a custom key. The key is only weakly referenced and in the event that it is garbage
 * collected the associated value is dereferenced from this registry as well.
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public class WeakRegistry<K, V> implements IRegistry<K, V>
{

    /**
     * The core key-value weakly referencing map.
     */
    Map<K, V> registry;
    /**
     * A map of name to value.
     */
    BiMap<String, V> nameRegistry;
    /**
     * The inverse of the nameRegistry mapping value to name.
     */
    BiMap<V, String> inverseNameRegistry;

    private boolean caseSensitiveKeys = true;

    /**
     * Creates a new {@link WeakRegistry}.
     */
    public WeakRegistry()
    {
        registry = new WeakHashMap<K, V>();
        nameRegistry = HashBiMap.create();
        inverseNameRegistry = nameRegistry.inverse();
    }

    protected void setCaseSensitiveKeys(boolean c)
    {
        this.caseSensitiveKeys = c;
    }

    /**
     * {@inheritDoc}
     */
    public void register(String name, K key, V value)
    {
        registry.put(key, value);
        nameRegistry.put(caseSensitiveKeys ? name : name.toUpperCase(), value);
    }

    /**
     * {@inheritDoc}
     */
    public Optional<V> get(K key)
    {
        return registry.containsKey(key) ? Optional.<V> of(registry.get(key)) : Optional.<V> absent();
    }

    /**
     * {@inheritDoc}
     */
    public Optional<V> get(String name)
    {
        if (!caseSensitiveKeys)
        {
            name = name.toUpperCase();
        }
        return nameRegistry.containsKey(name) ? Optional.<V> of(validate(nameRegistry.get(name))) : Optional.<V> absent();
    }

    /**
     * {@inheritDoc}
     */
    public Optional<String> getNameForValue(V value)
    {
        return inverseNameRegistry.containsKey(value) ? Optional.<String> of(inverseNameRegistry.get(validate(value))) : Optional.<String> absent();
    }

    /**
     * Validates that the given value is still referenced within the weak map.
     * 
     * @param value the value to check
     * @return the value if it is still relevant, else null
     */
    private V validate(V value)
    {
        for (Entry<K, V> e : registry.entrySet())
        {
            if (e.getValue().equals(value))
            {
                return value;
            }
        }
        inverseNameRegistry.remove(value);//This is automatically reflected within the nameRegistry.
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Iterable<String> getRegisteredNames()
    {
        return nameRegistry.keySet();
    }

    /**
     * {@inheritDoc}
     */
    public Iterable<V> getRegisteredValues()
    {
        return inverseNameRegistry.keySet();
    }

}
