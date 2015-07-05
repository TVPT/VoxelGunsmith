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
package com.voxelplugineering.voxelsniper.util;

/**
 * A Connected pair of values. Named here as a key and value.
 * 
 * @param <K> the first type
 * @param <V> the second type
 */
public class Pair<K, V>
{

    /**
     * The first value.
     */
    private K key;
    /**
     * The second value.
     */
    private V value;

    /**
     * Creates a new part.
     * 
     * @param k the first value
     * @param v the second value
     */
    public Pair(K k, V v)
    {
        this.key = k;
        this.value = v;
    }

    /**
     * Gets the first value.
     * 
     * @return the first
     */
    public K getKey()
    {
        return this.key;
    }

    /**
     * Gets the second value.
     * 
     * @return the second
     */
    public V getValue()
    {
        return this.value;
    }
}
