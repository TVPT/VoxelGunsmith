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

public class DataSourceFactoryService extends AbstractService implements DataSourceFactory
{

    private Map<String, DataSourceBuilder> builders;
    private Map<Class<? extends DataSource>, String> names;

    public DataSourceFactoryService()
    {
        super(-1);
    }

    @Override
    public String getName()
    {
        return "persistence";
    }

    @Override
    protected void init()
    {
        this.builders = Maps.newHashMap();
        this.names = Maps.newHashMap();
        Gunsmith.getLogger().info("Initialized Persistence service");
    }

    @Override
    protected void destroy()
    {
        this.builders = null;
        this.names = null;
        Gunsmith.getLogger().info("Stopped Persistence service");
    }

    @Override
    public <T extends DataSource> void register(String name, Class<T> type, DataSourceBuilder builder)
    {
        check();
        this.builders.put(name, builder);
        this.names.put(type, name);
    }

    @Override
    public Optional<DataSource> build(String name, DataContainer args)
    {
        check();
        if (this.builders.containsKey(name))
        {
            return this.builders.get(name).build(args);
        }
        return Optional.absent();
    }

    @Override
    public Optional<DataSource> build(String name)
    {
        return build(name, new MemoryContainer(""));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataSource> Optional<T> build(Class<T> type)
    {
        if(this.names.containsKey(type))
        {
            return (Optional<T>) build(this.names.get(type));
        }
        else
        {
            return Optional.absent();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataSource> Optional<T> build(Class<T> type, DataContainer args)
    {
        if(this.names.containsKey(type))
        {
            return (Optional<T>) build(this.names.get(type), args);
        }
        else
        {
            return Optional.absent();
        }
    }

}
