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

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.brushes.Brush;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.service.AbstractService;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;

/**
 * A service containing a {@link BrushManager}.
 */
public class BrushManagerService extends AbstractService implements BrushManager
{

    private final BrushManager wrapped;

    /**
     * Creates a new {@link BrushManagerService}.
     * 
     * @param manager The manager to use within this service
     */
    public BrushManagerService(BrushManager manager)
    {
        super(9);
        this.wrapped = manager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "globalBrushManager";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init()
    {
        Gunsmith.getLogger().info("Initialized GlobalBrushManager service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroy()
    {
        Gunsmith.getLogger().info("Stopped GlobalBrushManager service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadBrush(String identifier, Brush graph)
    {
        check();
        this.wrapped.loadBrush(identifier, graph);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadBrush(String identifier)
    {
        check();
        this.wrapped.loadBrush(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLoader(DataSourceReader loader)
    {
        check();
        this.wrapped.addLoader(loader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Brush> getBrush(String identifier)
    {
        check();
        return this.wrapped.getBrush(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParent(BrushManager parent)
    {
        check();
        this.wrapped.setParent(parent);
    }

}
