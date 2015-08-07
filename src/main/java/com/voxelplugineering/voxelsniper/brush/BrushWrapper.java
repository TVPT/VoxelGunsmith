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

/**
 * A wrapper class for {@link Brush} instances to provide better access to the information in the
 * {@link BrushInfo} annotation.
 */
public class BrushWrapper
{

    private final Brush brush;
    private final String name;
    private final String help;
    private final BrushPartType type;
    private final BrushParam[] params;

    /**
     * Creates a new {@link BrushWrapper} wrapping the given brush instance.
     * 
     * @param brush The brush
     */
    public BrushWrapper(Brush brush)
    {
        this.brush = brush;
        if (!brush.getClass().isAnnotationPresent(BrushInfo.class))
        {
            throw new IllegalArgumentException("Cannot wrap brush without BrushInfo annotation.");
        }
        BrushInfo info = brush.getClass().getAnnotation(BrushInfo.class);
        this.name = info.name();
        this.help = info.help();
        this.type = info.type();
        this.params = info.params();
    }

    /**
     * Gets the wrapped brush.
     * 
     * @return The brush
     */
    public Brush getBrush()
    {
        return this.brush;
    }

    /**
     * Gets the brush name.
     * 
     * @return The brush name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the brush type.
     * 
     * @return The brush type
     */
    public BrushPartType getType()
    {
        return this.type;
    }

    /**
     * Gets the brush help message.
     * 
     * @return The brush help message
     */
    public String getHelp()
    {
        return this.help;
    }

    public BrushParam[] getParameters()
    {
        return this.params;
    }

}
