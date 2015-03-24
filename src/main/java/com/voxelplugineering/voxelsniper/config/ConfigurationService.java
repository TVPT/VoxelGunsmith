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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.config.AbstractConfigurationContainer;
import com.voxelplugineering.voxelsniper.api.config.Configuration;
import com.voxelplugineering.voxelsniper.api.service.AbstractService;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.service.persistence.MemoryContainer;

/**
 * The configuration storage.
 */
public class ConfigurationService extends AbstractService implements Configuration
{

    /**
     * The configuration values.
     */
    private Map<String, Object> config;
    /**
     * A Map of containers imported to this configuration storage.
     */
    private Map<String, AbstractConfigurationContainer> containers;

    /**
     * Constructs a new Configuration
     */
    public ConfigurationService()
    {
        super(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "config";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init()
    {
        Gunsmith.getLogger().info("Initializing Configuration service");
        this.config = Maps.newHashMap();
        this.containers = Maps.newHashMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroy()
    {
        Gunsmith.getLogger().info("Stopping Configuration service");
        this.config = null;
        this.containers = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String name, Object value)
    {
        check();
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        checkNotNull(value, "Value cannot be null!");
        System.out.println("Setting " + name + " to " + value.toString());
        this.config.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Object> get(String name)
    {
        check();
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        return has(name) ? Optional.of(this.config.get(name)) : Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> get(String name, Class<T> expectedType)
    {
        check();
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        if (has(name))
        {
            Object o = this.config.get(name);
            if (expectedType.isAssignableFrom(o.getClass()))
            {
                return Optional.of(expectedType.cast(o));
            }
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends AbstractConfigurationContainer> void registerContainer(Class<T> container)
    {
        check();
        checkNotNull(container, "Container cannot be null!");
        String name = container.getSimpleName();
        if (this.containers.containsKey(name))
        {
            throw new IllegalArgumentException("Cannot register an already registered container");
        }
        // Load default values from the container
        T obj;
        try
        {
            obj = container.newInstance();
            this.containers.put(name, obj);
            Gunsmith.getLogger().info("Loading configuration container: " + name);
            DataContainer data = obj.toContainer();
            for (Map.Entry<String, Object> entry : data.entrySet())
            {
                set(entry.getKey(), entry.getValue());
            }
        } catch (InstantiationException e1)
        {
            Gunsmith.getLogger().error(e1, "Could not create a new instance of the container");
        } catch (IllegalAccessException e1)
        {
            Gunsmith.getLogger().error(e1, "Could not create a new instance of the container");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AbstractConfigurationContainer> getContainer(String containerName)
    {
        check();
        checkNotNull(containerName, "Name cannot be null!");
        checkArgument(!containerName.isEmpty(), "Name cannot be empty");
        return Optional.fromNullable(this.containers.get(containerName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractConfigurationContainer[] getContainers()
    {
        check();
        Set<Entry<String, AbstractConfigurationContainer>> entries = this.containers.entrySet();
        AbstractConfigurationContainer[] containers = new AbstractConfigurationContainer[entries.size()];
        int i = 0;
        for (Entry<String, AbstractConfigurationContainer> e : entries)
        {
            containers[i++] = e.getValue();
        }
        return containers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(String name)
    {
        check();
        return this.config.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fromContainer(DataContainer container)
    {
        for (String s : container.keySet())
        {
            if (this.containers.containsKey(s))
            {
                Optional<DataContainer> data = container.readContainer(s);
                if (data.isPresent())
                {
                    this.containers.get(s).fromContainer(data.get());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataContainer toContainer()
    {
        DataContainer container = new MemoryContainer("");
        for (String key : this.containers.keySet())
        {
            AbstractConfigurationContainer c = this.containers.get(key);
            container.writeContainer(c.getClass().getName(), c.toContainer());
        }
        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshAllContainers()
    {
        for (String key : this.containers.keySet())
        {
            AbstractConfigurationContainer c = this.containers.get(key);
            DataContainer data = c.toContainer();
            for (Map.Entry<String, Object> entry : data.entrySet())
            {
                set(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshContainer(String containerName)
    {
        Optional<AbstractConfigurationContainer> container = getContainer(containerName);
        if (container.isPresent())
        {
            DataContainer data = container.get().toContainer();
            for (Map.Entry<String, Object> entry : data.entrySet())
            {
                set(entry.getKey(), entry.getValue());
            }
        }
    }

}
