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
package com.voxelplugineering.voxelsniper.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.voxelplugineering.voxelsniper.api.IConfiguration;

/**
 * The configuration storage.
 */
public class Configuration implements IConfiguration
{

    /**
     * The configuration values.
     */
    private Map<String, Object> config;
    /**
     * A Map of containers imported to this configuration storage.
     */
    private Map<String, Class<?>> containers;

    /**
     * Constructs a new Configuration
     */
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
    public void registerContainer(Class<?> container)
    {
        String name = container.getName();
        if (this.containers.containsKey(name))
        {
            throw new IllegalArgumentException("Cannot register an already registered container");
        }
        this.containers.put(name, container);
        //Load default values from the container
        for (Field f : container.getDeclaredFields())
        {
            String n = f.getName();
            try
            {
                Object v = f.get(container);
                set(n, v);
                System.out.println("Set configuration value " + n + " to " + v.toString());
            } catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
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
        for (Entry<String, Class<?>> e : entries)
        {
            containers[i++] = e.getValue();
        }
        return containers;
    }

}
