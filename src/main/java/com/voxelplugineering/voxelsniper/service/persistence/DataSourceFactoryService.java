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

import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.service.AbstractService;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceBuilder;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceFactory;

/**
 * A {@link DataSourceFactory} service.
 */
public class DataSourceFactoryService extends AbstractService implements DataSourceFactory
{

    private Map<String, DataSourceBuilder<?>> builders;
    private Map<Class<? extends DataSource>, String> names;

    /**
     * Creates a new {@link DataSourceFactoryService}.
     */
    public DataSourceFactoryService()
    {
        super(-1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "persistence";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init()
    {
        this.builders = Maps.newHashMap();
        this.names = Maps.newHashMap();
        Gunsmith.getLogger().info("Initialized Persistence service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroy()
    {
        this.builders = null;
        this.names = null;
        Gunsmith.getLogger().info("Stopped Persistence service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DataSource> void register(String name, Class<T> type, DataSourceBuilder<T> builder)
    {
        check();
        this.builders.put(name, builder);
        this.names.put(type, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Optional<DataSource> build(String name, DataContainer args)
    {
        check();
        if (this.builders.containsKey(name))
        {
            return (Optional<DataSource>) this.builders.get(name).build(args);
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DataSource> build(String name)
    {
        check();
        return build(name, new MemoryContainer(""));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends DataSource> Optional<T> build(Class<T> type)
    {
        check();
        if (this.names.containsKey(type))
        {
            return (Optional<T>) build(this.names.get(type));
        } else
        {
            return Optional.absent();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends DataSource> Optional<T> build(Class<T> type, DataContainer args)
    {
        check();
        if (this.names.containsKey(type))
        {
            return (Optional<T>) build(this.names.get(type), args);
        } else
        {
            return Optional.absent();
        }
    }

}
