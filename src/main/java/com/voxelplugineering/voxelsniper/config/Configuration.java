package com.voxelplugineering.voxelsniper.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.voxelplugineering.voxelsniper.api.IConfiguration;

public class Configuration implements IConfiguration
{
    
    private Map<String, Object> config;
    private Map<String, Class<?>> containers;
    
    public Configuration()
    {
        this.config = new HashMap<String, Object>();
        this.containers = new HashMap<String, Class<?>>();
    }

    @Override
    public void set(String name, Object value)
    {
        this.config.put(name, value);
    }

    @Override
    public Object get(String name)
    {
        return this.config.get(name);
    }

    @Override
    public void registerContainer(Object container)
    {
        String name = container.getClass().getName();
        if(this.containers.containsKey(name))
        {
            throw new IllegalArgumentException("Cannot register an already registered container");
        }
        this.containers.put(name, container.getClass());
        //Load default values from the container
        for(Field f: container.getClass().getFields())
        {
            String n = f.getName();
            try
            {
                Object v = f.get(container);
                set(n, v);
            } catch (IllegalArgumentException e)
            {
                continue;
            } catch (IllegalAccessException e)
            {
                continue;
            }
        }
    }

    @Override
    public Class<?> getContainer(String containerName)
    {
        return this.containers.get(containerName);
    }

    @Override
    public Class<?>[] getContainers()
    {
        Set<Entry<String, Class<?>>> entries = this.containers.entrySet();
        Class<?>[] containers = new Class<?>[entries.size()];
        int i = 0;
        for(Entry<String, Class<?>> e: entries)
        {
            containers[i++] = e.getValue();
        }
        return containers;
    }
    
}
