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
package com.voxelplugineering.voxelsniper.brush;

import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.util.DataTranslator;

/**
 * A holder for variables pertaining to a single player. Variables are divided into several
 * {@link BrushContext}s.
 * 
 * <p>TODO: add lock to prevent multiple uses of the variables concurrently.</p>
 */
public class BrushVars
{

    private final Map<String, Object> global;
    private final Map<String, Object> runtime;
    private final Map<String, Object> flags;
    private final Map<String, Map<String, Object>> brushes;
    private BrushContext context;

    /**
     * Creates a new {@link BrushVars}.
     */
    public BrushVars()
    {
        this.global = Maps.newHashMap();
        this.runtime = Maps.newHashMap();
        this.flags = Maps.newHashMap();
        this.brushes = Maps.newHashMap();
        this.context = BrushContext.GLOBAL;
    }

    /**
     * Gets the current context which is used for get and has operations.
     * 
     * @return The context
     */
    public BrushContext getCurrentContext()
    {
        return this.context;
    }

    /**
     * Sets the current context.
     * 
     * @param context The new context
     */
    public void setContext(BrushContext context)
    {
        this.context = context;
    }

    /**
     * Clears the runtime variables to start a new execution run.
     */
    public void clearRuntime()
    {
        this.runtime.clear();
    }

    /**
     * Clears all variables.
     */
    public void clear()
    {
        this.brushes.clear();
        this.global.clear();
        this.runtime.clear();
        this.flags.clear();
    }

    /**
     * Gets a variable from the {@link BrushVars} with the given type. If the variable doesn't
     * exist, or it is of a different type then {@link Optional#absent()} is returned.
     * 
     * <p>If the variable type does not match the given type an attempt is made to call
     * {@link DataTranslator#attempt} to convert the variable to the requested type.</p>
     * 
     * <p>The contexts are checked in the order of brushes, then runtime, then global.</p>
     * 
     * @param path The path to retrieve
     * @param type The expected type
     * @param <T> The expected type
     * @return The value, if found
     */
    public <T> Optional<T> get(String path, Class<T> type)
    {
        Map<String, Object> data = null;
        if (this.context instanceof BrushContext.Brush)
        {
            String name = ((BrushContext.Brush) this.context).getBrush().getName();
            if (this.brushes.containsKey(name) && this.brushes.get(name).containsKey(path))
            {
                data = this.brushes.get(name);
            }
        }
        if (data == null)
        {
            if (this.runtime.containsKey(path))
            {
                data = this.runtime;
            } else if (this.global.containsKey(path))
            {
                data = this.global;
            } else if (this.flags.containsKey(path))
            {
                data = this.flags;
            } else
            {
                return Optional.absent();
            }
        }
        Object o = data.get(path);
        if (o != null)
        {
            try
            {
                return Optional.of(type.cast(o));
            } catch (Exception e)
            {
                return DataTranslator.attempt(o, type);
            }
        }
        return Optional.absent();
    }

    /**
     * Gets whether the given path exists within the {@link BrushVars}. nThe contexts are checked in
     * the order of brushes, then runtime, then global.
     * 
     * @param path The path to check
     * @return If the path was found
     */
    public boolean has(String path)
    {
        if (this.context instanceof BrushContext.Brush)
        {
            String name = ((BrushContext.Brush) this.context).getBrush().getName();
            if (this.brushes.containsKey(name) && this.brushes.get(name).containsKey(path))
            {
                return true;
            }
        }
        if (this.runtime.containsKey(path))
        {
            return true;
        }
        if (this.global.containsKey(path))
        {
            return true;
        }
        if (this.flags.containsKey(path))
        {
            return true;
        }
        return false;
    }

    /**
     * Sets the given value to the variable storage of the given context.
     * 
     * @param context The context
     * @param path The path
     * @param value The value
     */
    public void set(BrushContext context, String path, Object value)
    {
        if (context instanceof BrushContext.Brush)
        {
            String name = ((BrushContext.Brush) context).getBrush().getName();
            if (this.brushes.containsKey(name))
            {
                this.brushes.get(name).put(path, value);
                return;
            }
            Map<String, Object> data = Maps.newHashMap();
            data.put(path, value);
            this.brushes.put(name, data);
            return;
        }
        if (context == BrushContext.RUNTIME)
        {
            this.runtime.put(path, value);
            return;
        }
        if (context == BrushContext.GLOBAL)
        {
            this.global.put(path, value);
            return;
        }
        if (context == BrushContext.FLAGS)
        {
            this.flags.put(path, value);
            return;
        }
        throw new UnsupportedOperationException("Unknown context " + context.toString());
    }

    /**
     * Removes the given path from the context.
     * 
     * @param context The context
     * @param path The path to remove
     * @return If a value was removed
     */
    public boolean remove(BrushContext context, String path)
    {
        if (context instanceof BrushContext.Brush)
        {
            String name = ((BrushContext.Brush) this.context).getBrush().getName();
            if (this.brushes.containsKey(name))
            {
                return this.brushes.get(name).remove(path) != null;
            }
            return false;
        }
        if (context == BrushContext.RUNTIME)
        {
            return this.runtime.remove(path) != null;
        }
        if (context == BrushContext.GLOBAL)
        {
            return this.global.remove(path) != null;
        }
        if (context == BrushContext.FLAGS)
        {
            return this.flags.remove(path) != null;
        }
        throw new UnsupportedOperationException("Unknown context " + context.toString());
    }

}
