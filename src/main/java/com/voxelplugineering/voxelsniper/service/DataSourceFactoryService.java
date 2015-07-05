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
package com.voxelplugineering.voxelsniper.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.service.persistence.DataSource;
import com.voxelplugineering.voxelsniper.service.persistence.DataSourceBuilder;
import com.voxelplugineering.voxelsniper.service.persistence.DataSourceFactory;
import com.voxelplugineering.voxelsniper.service.persistence.MemoryContainer;
import com.voxelplugineering.voxelsniper.util.Context;

/**
 * A {@link DataSourceFactory} service.
 */
public class DataSourceFactoryService extends AbstractService implements DataSourceFactory
{

    private Map<String, DataSourceBuilder<?>> builders;
    private Map<Class<? extends DataSource>, String> names;

    /**
     * Creates a new {@link DataSourceFactoryService}.
     * 
     * @param context The context
     */
    public DataSourceFactoryService(Context context)
    {
        super(context);
    }

    @Override
    protected void _init()
    {
        this.builders = Maps.newHashMap();
        this.names = Maps.newHashMap();
    }

    @Override
    protected void _shutdown()
    {
        this.builders = null;
        this.names = null;
    }

    @Override
    public <T extends DataSource> void register(String name, Class<T> type, DataSourceBuilder<T> builder)
    {
        check("register");
        GunsmithLogger.getLogger().info("Registering DataSourceBuilder " + name + " " + type.getName());
        this.builders.put(name, builder);
        this.names.put(type, name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<DataSource> build(String name, DataContainer args)
    {
        check("build");
        checkNotNull(name);
        if (this.builders.containsKey(name))
        {
            return (Optional<DataSource>) this.builders.get(name).build(args);
        }
        System.err.println("Could not find builder for " + name);
        return Optional.absent();
    }

    @Override
    public Optional<DataSource> build(String name)
    {
        return build(name, new MemoryContainer(""));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DataSource> Optional<T> build(Class<T> type)
    {
        check("build");
        if (this.names.containsKey(type))
        {
            return (Optional<T>) build(this.names.get(type));
        }
        return Optional.absent();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DataSource> Optional<T> build(Class<T> type, DataContainer args)
    {
        check("build");
        if (this.names.containsKey(type))
        {
            return (Optional<T>) build(this.names.get(type), args);
        }
        return Optional.absent();
    }

    @Override
    public boolean remove(String name)
    {
        check("remove");
        if (this.builders.containsKey(name))
        {
            this.builders.remove(name);
            for (Iterator<Map.Entry<Class<? extends DataSource>, String>> iter = this.names.entrySet().iterator(); iter.hasNext();)
            {
                Map.Entry<Class<? extends DataSource>, String> entry = iter.next();
                if (entry.getValue().equals(name))
                {
                    iter.remove();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void clear()
    {
        check("clear");
        this.names.clear();
        this.builders.clear();
    }

}
