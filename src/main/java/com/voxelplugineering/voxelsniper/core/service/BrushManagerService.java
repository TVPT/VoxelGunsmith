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
package com.voxelplugineering.voxelsniper.core.service;

import java.util.List;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.brushes.Brush;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.brushes.GlobalBrushManager;
import com.voxelplugineering.voxelsniper.api.service.logging.LoggingDistributor;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.core.util.Context;

/**
 * A service containing a {@link BrushManager}.
 */
public class BrushManagerService extends AbstractService implements GlobalBrushManager
{

    private final BrushManager wrapped;
    private final LoggingDistributor logger;

    /**
     * Creates a new {@link BrushManagerService}.
     * 
     * @param manager The manager to use within this service
     */
    public BrushManagerService(Context context, BrushManager manager)
    {
        super(context);
        this.logger = context.getRequired(LoggingDistributor.class, this);
        this.wrapped = manager;
    }

    @Override
    protected void _init()
    {
        this.logger.info("Initialized GlobalBrushManager service");
    }

    @Override
    protected void _shutdown()
    {
        this.logger.info("Stopped GlobalBrushManager service");
    }

    @Override
    public void loadBrush(String identifier, Brush graph)
    {
        check("loadBrush");
        this.wrapped.loadBrush(identifier, graph);
    }

    @Override
    public void loadBrush(String identifier)
    {
        check("loadBrush");
        this.wrapped.loadBrush(identifier);
    }

    @Override
    public void addLoader(DataSourceReader loader)
    {
        check("addLoader");
        this.wrapped.addLoader(loader);
    }

    @Override
    public Optional<Brush> getBrush(String identifier)
    {
        check("getBrush");
        return this.wrapped.getBrush(identifier);
    }

    @Override
    public void setParent(BrushManager parent)
    {
        throw new UnsupportedOperationException("Cannot set the parent of the global brush manager.");
    }

    @Override
    public BrushManager getParent()
    {
        return null;
    }

    @Override
    public List<DataSourceReader> getAllLoaders()
    {
        check("getAllLoaders");
        return this.wrapped.getAllLoaders();
    }

    @Override
    public void clearLoaders()
    {
        check("clearLoaders");
        this.wrapped.clearLoaders();
    }

}
