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

import java.lang.ref.WeakReference;

/**
 * A wrapper for a weakly referenced object.
 * 
 * @param <T> the object type
 */
public abstract class WeakWrapper<T>
{

    /**
     * The object weak reference.
     */
    private final WeakReference<T> reference;

    /**
     * Creates a new {@link WeakWrapper}
     * 
     * @param value the reference to wrap
     */
    public WeakWrapper(T value)
    {
        this.reference = new WeakReference<T>(value);
    }

    /**
     * Returns the wrapped value, or null if it has been garbage collected.
     * 
     * @return the wrapped object, may be null
     */
    public T getThis()
    {
        return this.reference.get();
    }

}
