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
package com.voxelplugineering.voxelsniper.brushes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceProvider;

/**
 * A standard brush manager.
 */
public class CommonBrushManager implements BrushManager
{

    /**
     * The parent brush manager, referenced by the brush getter if it cannot be
     * found within this brush manager.
     */
    private BrushManager parent = null;
    /**
     * A map of brushes loaded in this manager.
     */
    private Map<String, BrushNodeGraph> brushes;
    /**
     * An ordered list of loaders used to load brushes by name.
     */
    private List<DataSourceProvider> loaders;

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
        this.loaders = Lists.newArrayList();
        if (Gunsmith.getPlatformProxy() != null)
        {
            DataSourceProvider loader = Gunsmith.getPlatformProxy().getBrushDataSource();
            if(loader != null)
            {
                this.loaders.add(loader);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addLoader(DataSourceProvider loader)
    {
        this.loaders.add(0, loader);
    }

    /**
     * {@inheritDoc}
     */
    public void loadBrush(String identifier, BrushNodeGraph graph)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        checkNotNull(graph, "Brush class cannot be null!");
        // TODO: Check version if already loaded
        this.brushes.put(identifier, graph);
    }

    /**
     * {@inheritDoc}
     */
    public void loadBrush(String identifier)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        BrushNodeGraph graph = null;
        for (Iterator<DataSourceProvider> iter = this.loaders.iterator(); iter.hasNext();)
        {
            DataSourceProvider loader = iter.next();
            Optional<DataSource> source = loader.get(identifier);
            if (source.isPresent())
            {
                try
                {
                    BrushNodeGraph attempt = BrushNodeGraph.buildFromContainer(source.get().read());
                    if (attempt != null)
                    {
                        graph = attempt;
                        break;
                    }
                } catch (IOException e)
                {
                    continue;
                }
            }
        }
        if (graph != null)
        {
            loadBrush(identifier, graph);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Optional<BrushNodeGraph> getBrush(String identifier)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        BrushNodeGraph br = this.brushes.get(identifier);
        if (br == null)
        {
            if (this.parent != null)
            {
                return this.parent.getBrush(identifier);
            }
            return Optional.absent();
        }
        return Optional.<BrushNodeGraph>of(br);
    }

    /**
     * {@inheritDoc}
     */
    public void setParent(BrushManager parent)
    {
        this.parent = parent;
    }

}
