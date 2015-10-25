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

import com.voxelplugineering.voxelsniper.util.Nameable;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

/**
 * A registry for a name and an object key related to a value.
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public interface Registry<K, V>
{

    /**
     * Registers a new value into this registry.
     * 
     * @param name The name of the new value
     * @param key The object key for the value
     * @param value The new value
     */
    void register(String name, K key, V value);

    /**
     * Registers a new value into this registry. This method may only be used if the value
     * implements the {@link Nameable} interface. If it does not then the
     * {@link #register(String, Object, Object)} method must be used instead.
     * 
     * @param key The object key for the value
     * @param value The new value
     */
    default void register(K key, V value)
    {
        if (!Nameable.class.isAssignableFrom(value.getClass()))
        {
            throw new IllegalArgumentException(
                    value.getClass().getName() + " does not implement Nameable, use #register(String, Object, Object) instead");
        }
        Nameable nameable = (Nameable) value;
        register(nameable.getName(), key, value);
    }

    /**
     * Given the object key returns the associated value.
     * 
     * @param key The object key
     * @return The value
     */
    Optional<V> get(K key);

    /**
     * Given the name returns the associated value.
     * 
     * @param name The name
     * @return The value
     */
    Optional<V> get(String name);

    /**
     * Given a value returns the name of it in the registry.
     * 
     * @param value The value
     * @return The name of the value in the registry
     */
    Optional<String> getNameForValue(V value);

    /**
     * Returns a collection of the names of all keys in this registry.
     * 
     * @return A collection of the names
     */
    Iterable<String> getRegisteredNames();

    /**
     * Returns a set of all key value pairs in this registry.
     * 
     * @return A set of the entries
     */
    Set<Entry<K, V>> getRegisteredValues();

    /**
     * Returns a collection of all values in this registry.
     * 
     * @return A collection of the values
     */
    Collection<V> values();

    /**
     * Removes a value from this registry by name. If no value exists in this registry with the
     * given name then nothing will happen.
     * 
     * @param name The name
     */
    void remove(String name);

    /**
     * Removes a value from this registry by the key object. If no value exists in this registry
     * with the given key then nothing will happen.
     * 
     * @param key The key object
     */
    void remove(K key);

}
