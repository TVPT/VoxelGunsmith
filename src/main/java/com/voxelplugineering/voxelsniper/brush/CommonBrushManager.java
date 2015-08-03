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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * A standard brush manager.
 */
public class CommonBrushManager implements BrushManager
{

    private BrushManager parent = null;
    private final Map<String, BrushWrapper> brushes;

    /**
     * Creates a new CommonBrushManager.
     */
    public CommonBrushManager()
    {
        this(null);
    }

    /**
     * Creates a new CommonBrushManager with the subscribed parent
     * 
     * @param parent the parent
     */
    public CommonBrushManager(BrushManager parent)
    {
        this.parent = parent;
        this.brushes = Maps.newHashMap();
    }

    @Override
    public void loadBrush(String identifier, Brush graph)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        checkNotNull(graph, "Brush class cannot be null!");
        this.brushes.put(identifier, new BrushWrapper(graph));
    }

    @Override
    public Optional<BrushWrapper> getBrush(String identifier)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        BrushWrapper br = this.brushes.get(identifier);
        if (br == null)
        {
            if (this.parent != null)
            {
                return this.parent.getBrush(identifier);
            }
            return Optional.absent();
        }
        return Optional.of(br);
    }

    @Override
    public void setParent(BrushManager parent)
    {
        this.parent = parent;
    }

    @Override
    public BrushManager getParent()
    {
        return this.parent;
    }

    @Override
    public Iterable<BrushWrapper> getBrushes()
    {
        return this.brushes.values();
    }

}
