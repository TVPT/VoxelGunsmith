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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.collect.MapMaker;

/**
 * Represents the context of variables for use within a players {@link BrushVars}.
 */
public abstract class BrushContext
{

    /**
     * The global context. This is the context for settings which do not change between brush
     * executions or brush selections.
     */
    public static final BrushContext GLOBAL = new Fixed("global");
    /**
     * The runtime context. This is the context for variables used during the execution of a brush
     * chain, as well as the information about the ray trace.
     */
    public static final BrushContext RUNTIME = new Fixed("runtime");
    /**
     * The flags context. A place for flags for inter-operation markers.
     */
    public static final BrushContext FLAGS = new Fixed("flags");

    private static final Map<BrushWrapper, BrushContext.Brush> BRUSHES;

    static
    {
        BRUSHES = new MapMaker().weakKeys().makeMap();
    }

    /**
     * Gets a brush context for the given brush.
     * 
     * @param brush The brush
     * @return The context for the given brush
     */
    public static BrushContext of(BrushWrapper brush)
    {
        // TODO could change this to use the Brush instances rather than the wrapper
        checkNotNull(brush);
        if (BRUSHES.containsKey(brush))
        {
            return BRUSHES.get(brush);
        }
        Brush c = new Brush(brush);
        BRUSHES.put(brush, c);
        return c;
    }

    /**
     * A simple named context.
     */
    public static class Fixed extends BrushContext
    {

        private final String name;

        /**
         * Creates a new {@link Fixed} context.
         * 
         * @param name The context name.
         */
        public Fixed(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return this.name;
        }

    }

    /**
     * A brush context.
     */
    public static class Brush extends BrushContext
    {

        private final BrushWrapper brush;

        /**
         * Creates a new {@link Brush} context wrapping the given brush.
         * 
         * @param brush The brush
         */
        public Brush(BrushWrapper brush)
        {
            this.brush = brush;
        }

        /**
         * Gets the brush wrapped by this context.
         * 
         * @return The brush
         */
        public BrushWrapper getBrush()
        {
            return this.brush;
        }

        @Override
        public String toString()
        {
            return this.brush.getName();
        }
    }

}
