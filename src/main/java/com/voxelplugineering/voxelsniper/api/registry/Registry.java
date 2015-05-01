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
package com.voxelplugineering.voxelsniper.api.registry;

import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Optional;

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
     * @param name the name of the new value
     * @param key the object key for the value
     * @param value the new value
     */
    void register(String name, K key, V value);

    /**
     * Given the object key returns the associated value.
     * 
     * @param key the object key
     * @return the value
     */
    Optional<V> get(K key);

    /**
     * Given the name returns the associated value.
     * 
     * @param name the name
     * @return the value
     */
    Optional<V> get(String name);

    /**
     * Given a value returns the name of it in the registry.
     * 
     * @param value the value
     * @return the name of the value in the registry
     */
    Optional<String> getNameForValue(V value);

    /**
     * Returns a collection of the names of all keys in this registry.
     * 
     * @return a collection of the names
     */
    Iterable<String> getRegisteredNames();

    /**
     * Returns a collection of all values in this registry.
     * 
     * @return a collection of the values
     */
    Set<Entry<K, V>> getRegisteredValues();

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
