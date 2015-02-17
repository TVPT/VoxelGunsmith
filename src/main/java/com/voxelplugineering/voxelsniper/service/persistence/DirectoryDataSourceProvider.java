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
package com.voxelplugineering.voxelsniper.service.persistence;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceProvider;

/**
 * A {@link DataSourceProvider} for files within a directory. Builds child data
 * sources using a {@link FileDataSource.Builder}.
 */
public class DirectoryDataSourceProvider implements DataSourceProvider
{

    final File directory;
    // Decided to use a cache due to how often has and read will be called close
    // together looking for the same file.
    private final LoadingCache<String, File> cache;
    private final FileDataSource.Builder builder;

    /**
     * Creates a new {@link DirectoryDataSourceProvider}.
     * 
     * @param dir The directory
     * @param builder The data source builder
     */
    public DirectoryDataSourceProvider(File dir, FileDataSource.Builder builder)
    {
        this.directory = dir;
        this.cache = CacheBuilder.newBuilder().expireAfterAccess(60, TimeUnit.SECONDS).build(new CacheLoader<String, File>()
        {

            @Override
            public File load(String ident) throws Exception
            {
                return new File(DirectoryDataSourceProvider.this.directory, ident);
            }

        });
        this.builder = builder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(String identifier)
    {
        try
        {
            return this.cache.get(identifier).exists();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DataSource> get(String identifier)
    {
        File file;
        try
        {
            file = this.cache.get(identifier);
        } catch (ExecutionException e)
        {
            e.printStackTrace();
            return Optional.absent();
        }
        if (file.exists())
        {
            return Optional.of(this.builder.build(file));
        }
        return Optional.absent();
    }

}
