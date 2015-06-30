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

import com.google.common.collect.MapMaker;

public abstract class BrushContext {

    public static final BrushContext GLOBAL = new Fixed("global");
    public static final BrushContext RUNTIME = new Fixed("runtime");
    
    private static final Map<com.voxelplugineering.voxelsniper.brush.Brush, BrushContext> BRUSHES;
    
    static {
        BRUSHES = new MapMaker().weakKeys().makeMap();
    }
    
    public static BrushContext of(com.voxelplugineering.voxelsniper.brush.Brush brush) {
        if(BRUSHES.containsKey(brush)) {
            return BRUSHES.get(brush);
        }
        BrushContext c = new Brush(brush);
        BRUSHES.put(brush, c);
        return c;
    }

    public static class Fixed extends BrushContext {

        private final String name;
        
        public Fixed(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
    }

    public static class Brush extends BrushContext {

        private final com.voxelplugineering.voxelsniper.brush.Brush brush;

        public Brush(com.voxelplugineering.voxelsniper.brush.Brush brush) {
            this.brush = brush;
        }
        
        public com.voxelplugineering.voxelsniper.brush.Brush getBrush() {
            return this.brush;
        }
        
        @Override
        public String toString() {
            return this.brush.getName();
        }
    }

}
