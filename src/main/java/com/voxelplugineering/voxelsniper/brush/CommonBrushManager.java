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

import com.voxelplugineering.voxelsniper.util.Context;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * A standard brush manager.
 */
public class CommonBrushManager implements BrushManager
{

    private final Context context;
    private BrushManager parent = null;
    private final Map<String, BrushWrapper> brushes;

    /**
     * Creates a new CommonBrushManager.
     */
    public CommonBrushManager(Context context)
    {
        this(context, null);
    }

    /**
     * Creates a new CommonBrushManager with the subscribed parent
     * 
     * @param parent The parent
     */
    public CommonBrushManager(Context context, BrushManager parent)
    {
        this.context = context;
        this.parent = parent;
        this.brushes = Maps.newHashMap();
    }

    @Override
    public void loadBrush(String identifier, Brush graph)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        checkNotNull(graph, "Brush class cannot be null!");
        this.brushes.put(identifier, new BrushWrapper(graph, this.context));
    }

    @Override
    public Optional<BrushWrapper> getBrush(String identifier)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        if (this.brushes.containsKey(identifier))
        {
            BrushWrapper br = this.brushes.get(identifier);
            return Optional.of(br);
        }
        if (this.parent != null)
        {
            return this.parent.getBrush(identifier);
        }
        return Optional.empty();
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
    public Collection<BrushWrapper> getBrushes()
    {
        return this.brushes.values();
    }

}
