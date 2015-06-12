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
package com.voxelplugineering.voxelsniper.api.service.platform;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.net.URISyntaxException;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceFactory;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceProvider;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.core.Gunsmith;
import com.voxelplugineering.voxelsniper.core.service.AbstractService;
import com.voxelplugineering.voxelsniper.core.service.persistence.DirectoryDataSourceProvider;
import com.voxelplugineering.voxelsniper.core.util.Context;

/**
 * A trivial {@link PlatformProxy} which is used in the case of no platform being present, such as
 * when tests are being run.
 */
public class TrivialPlatformProxy extends AbstractService implements PlatformProxy
{

    private final DataSourceFactory factory;

    /**
     * Creates a new {@link TrivialPlatformProxy}.
     */
    public TrivialPlatformProxy(Context context)
    {
        super(context);
        Optional<DataSourceFactory> factory = context.get(DataSourceFactory.class);
        checkArgument(factory.isPresent(), "DataSourceFactory service was not found in the current context.");
        this.factory = factory.get();
        this.factory.addDependent(this);
    }

    @Override
    protected void _init()
    {

    }

    @Override
    protected void _shutdown()
    {

    }

    @Override
    public String getPlatformName()
    {
        return "none";
    }

    @Override
    public String getVersion()
    {
        return "none";
    }

    @Override
    public String getFullVersion()
    {
        return "none";
    }

    @Override
    public DataSourceProvider getBrushDataSource()
    {
        return null;
    }

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
            return new DirectoryDataSourceProvider(directory, this.factory);
        } catch (URISyntaxException ignored)
        {
            throw new RuntimeException("Could not create trivial root data source");
        }
    }

    @Override
    public DataSourceReader getMetricsFile()
    {
        return null;
    }

    @Override
    public int getNumberOfPlayersOnline()
    {
        return 0;
    }

    @Override
    public Optional<DataSourceReader> getConfigDataSource()
    {
        return Optional.absent();
    }

}
