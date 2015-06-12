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
package com.voxelplugineering.voxelsniper.core.service.persistence;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceFactory;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceProvider;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;

/**
 * A {@link DataSourceProvider} for files within a directory.
 */
public class DirectoryDataSourceProvider implements DataSourceProvider
{

    private final DataSourceFactory factory;

    private final File directory;
    // Decided to use a cache due to how often has and read will be called close
    // together looking for the same file.
    private final LoadingCache<String, File> cache;
    private Class<? extends DataSourceReader> reader = null;
    private DataContainer readerArgs = null;

    /**
     * Creates a new {@link DirectoryDataSourceProvider}.
     * 
     * @param dir The directory
     */
    public DirectoryDataSourceProvider(File dir, DataSourceFactory factory)
    {
        checkArgument(dir.isDirectory(), "File passed to DirectoryDataSource was not a directory.");
        this.directory = dir;
        this.factory = factory;
        this.cache = CacheBuilder.newBuilder().expireAfterAccess(60, TimeUnit.SECONDS).build(new CacheLoader<String, File>()
        {

            @Override
            public File load(String ident) throws Exception
            {
                return new File(DirectoryDataSourceProvider.this.directory, ident);
            }

        });
    }

    @Override
    public boolean has(String identifier)
    {
        try
        {
            return this.cache.get(identifier).isFile();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
            return false;
        }
    }

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
        if (file.isFile())
        {
            return Optional.<DataSource>of(new FileDataSource(file));
        }
        return Optional.absent();
    }

    @Override
    public void setReaderType(Class<? extends DataSourceReader> reader, DataContainer args)
    {
        this.reader = reader;
        this.readerArgs = args;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<DataSourceReader> getWithReader(String identifier)
    {
        if (this.reader == null || this.readerArgs == null)
        {
            return Optional.absent();
        }
        return (Optional<DataSourceReader>) getWithReader(identifier, this.reader, this.readerArgs);
    }

    @Override
    public <T extends DataSourceReader> Optional<T> getWithReader(String identifier, Class<T> reader)
    {
        return getWithReader(identifier, reader, new MemoryContainer());
    }

    @Override
    public <T extends DataSourceReader> Optional<T> getWithReader(String identifier, Class<T> reader, DataContainer args)
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
        if (file.isFile() || !file.exists())
        {
            DataContainer sourceArgs = new MemoryContainer("");
            sourceArgs.writeString("path", file.getAbsolutePath());
            args.writeString("source", "file");
            args.writeContainer("sourceArgs", sourceArgs);
            return this.factory.build(reader, args);
        }
        return Optional.absent();
    }

    @Override
    public Optional<? extends DataSourceProvider> getInternalProvider(String identifier)
    {
        //TODO
        return null;
    }

}
