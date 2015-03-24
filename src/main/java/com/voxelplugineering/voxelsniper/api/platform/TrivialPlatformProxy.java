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
package com.voxelplugineering.voxelsniper.api.platform;

import java.io.File;
import java.net.URISyntaxException;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.service.AbstractService;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceProvider;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.service.persistence.DirectoryDataSourceProvider;

/**
 * A trivial {@link PlatformProxy} which is used in the case of no platform
 * being present, such as when tests are being run.
 */
public class TrivialPlatformProxy extends AbstractService implements PlatformProxy
{

    /**
     * Creates a new {@link TrivialPlatformProxy}.
     */
    public TrivialPlatformProxy()
    {
        super(4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "platformProxy";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init()
    {

    }

    @Override
    protected void destroy()
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatformName()
    {
        return "none";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion()
    {
        return "none";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFullVersion()
    {
        return "none";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSourceProvider getBrushDataSource()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSourceProvider getRootDataSourceProvider()
    {
        try
        {
            File directory = new File(Gunsmith.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            if (!directory.isDirectory())
            {
                directory = directory.getParentFile();
            }
            return new DirectoryDataSourceProvider(directory);
        } catch (URISyntaxException ignored)
        {
            throw new RuntimeException("Could not create trivial root data source");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSourceReader getMetricsFile()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfPlayersOnline()
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DataSourceReader> getConfigDataSource()
    {
        return Optional.absent();
    }

}
