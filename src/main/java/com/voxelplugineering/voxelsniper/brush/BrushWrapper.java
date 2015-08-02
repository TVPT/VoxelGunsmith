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

public class BrushWrapper
{

    private final Brush brush;
    private final String name;
    private final String help;
    private final BrushPartType type;

    public BrushWrapper(Brush brush)
    {
        this.brush = brush;
        if(!brush.getClass().isAnnotationPresent(BrushInfo.class)) {
            throw new IllegalArgumentException("Cannot wrap brush without BrushInfo annotation.");
        }
        BrushInfo info = brush.getClass().getAnnotation(BrushInfo.class);
        this.name = info.name();
        this.help = info.help();
        this.type = info.type();
    }

    public Brush getBrush()
    {
        return this.brush;
    }

    public String getName()
    {
        return this.name;
    }

    public BrushPartType getType()
    {
        return this.type;
    }

    public String getHelp()
    {
        return this.help;
    }

}
