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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.MapMaker;
import com.voxelplugineering.voxelsniper.api.registry.Registry;

/**
 * A registry for values which are referenced both by a name and a custom key. The key is only
 * weakly referenced and in the event that it is garbage collected the associated value is
 * dereferenced from this registry as well.
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public class WeakRegistry<K, V> implements Registry<K, V>
{

    /**
     * The core key-value weakly referencing map.
     */
    Map<K, V> registry;
    /**
     * A map of name to value.
     */
    Map<String, K> nameRegistry;

    private boolean caseSensitiveKeys = true;

    /**
     * Creates a new {@link WeakRegistry}.
     */
    public WeakRegistry()
    {
        this.registry = new MapMaker().weakKeys().makeMap();
        this.nameRegistry = new MapMaker().weakValues().makeMap();
    }

    /**
     * Sets whether the keys of this registry are case sensitive. If the keys are case insensitive
     * then all keys in all operations are cast to upper case.
     * 
     * @param c is case sensitive
     */
    public void setCaseSensitiveKeys(boolean c)
    {
        this.caseSensitiveKeys = c;
    }

    @Override
    public void register(String name, K key, V value)
    {
        checkNotNull(key);
        checkNotNull(value);
        this.registry.put(key, value);
        if (name != null)
        {
            this.nameRegistry.put(this.caseSensitiveKeys ? name : name.toUpperCase(), key);
        }
    }

    @Override
    public Optional<V> get(K key)
    {
        checkNotNull(key);
        return Optional.fromNullable(this.registry.get(key));
    }

    @Override
    public Optional<V> get(String name)
    {
        checkNotNull(name);
        if (!this.caseSensitiveKeys)
        {
            name = name.toUpperCase();
        }
        if (!this.nameRegistry.containsKey(name))
        {
            return Optional.absent();
        }
        K key = this.nameRegistry.get(name);
        if (!this.registry.containsKey(key))
        {
            this.nameRegistry.remove(name);
            return Optional.absent();
        }
        return Optional.of(this.registry.get(key));
    }

    @Override
    public Optional<String> getNameForValue(V value)
    {
        checkNotNull(value);
        K key = null;
        for (Map.Entry<K, V> entry : this.registry.entrySet())
        {
            if (entry.getValue() == value)
            {
                key = entry.getKey();
                break;
            }
        }
        if (key != null)
        {
            for (Map.Entry<String, K> entry : this.nameRegistry.entrySet())
            {
                if (entry.getValue() == key)
                {
                    return Optional.of(entry.getKey());
                }
            }
        }
        return Optional.absent();
    }

    @Override
    public Iterable<String> getRegisteredNames()
    {
        return this.nameRegistry.keySet();
    }

    @Override
    public Set<Entry<K, V>> getRegisteredValues()
    {
        return this.registry.entrySet();
    }

    @Override
    public void remove(String name)
    {
        checkNotNull(name);
        this.nameRegistry.remove(name);
    }

    @Override
    public void remove(K key)
    {
        checkNotNull(key);
        this.registry.remove(key);
    }

}
