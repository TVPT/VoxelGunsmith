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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.service.config.Configuration;
import com.voxelplugineering.voxelsniper.service.config.ConfigurationContainer;
import com.voxelplugineering.voxelsniper.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.service.persistence.MemoryContainer;
import com.voxelplugineering.voxelsniper.util.Context;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;

import java.util.Map;

/**
 * The configuration storage.
 */
public class ConfigurationService extends AbstractService implements Configuration
{

    private Map<String, Object> config;
    private Map<String, ConfigurationContainer> containers;

    /**
     * Constructs a new {@link ConfigurationService}.
     * 
     * @param context The context
     */
    public ConfigurationService(Context context)
    {
        super(context);
    }

    @Override
    protected void _init()
    {
        this.config = Maps.newHashMap();
        this.containers = Maps.newHashMap();
    }

    @Override
    protected void _shutdown()
    {
        this.config = null;
        this.containers = null;
    }

    @Override
    public void set(String name, Object value)
    {
        check("set");
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        checkNotNull(value, "Value cannot be null!");
        GunsmithLogger.getLogger().debug("Set config: " + name + " " + value.toString());
        this.config.put(name, value);
    }

    @Override
    public Optional<Object> get(String name)
    {
        check("get");
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        return has(name) ? Optional.of(this.config.get(name)) : Optional.absent();
    }

    @Override
    public <T> Optional<T> get(String name, Class<T> expectedType)
    {
        check("get");
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        if (expectedType.isPrimitive())
        {
            expectedType = Primitives.wrap(expectedType);
        }
        if (has(name))
        {
            Object o = this.config.get(name);
            if (expectedType.isAssignableFrom(o.getClass()))
            {
                return Optional.of(expectedType.cast(o));
            }
            GunsmithLogger.getLogger().warn(
                    "Cannot get config value " + name + " with expected type " + expectedType.getSimpleName() + " has type "
                            + o.getClass().getSimpleName());
        }
        return Optional.absent();
    }

    @Override
    public <T extends ConfigurationContainer> void registerContainer(Class<T> container)
    {
        check("registerContainer");
        checkNotNull(container, "Container cannot be null!");
        GunsmithLogger.getLogger().info("Registering config values from " + container.getName());
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
            GunsmithLogger.getLogger().debug("Loading configuration container: " + name);
            DataContainer data = obj.toContainer();
            for (Map.Entry<String, Object> entry : data.entrySet())
            {
                set(entry.getKey(), entry.getValue());
            }
        } catch (InstantiationException e1)
        {
            GunsmithLogger.getLogger().error(e1, "Could not create a new instance of the container");
        } catch (IllegalAccessException e1)
        {
            GunsmithLogger.getLogger().error(e1, "Could not create a new instance of the container");
        } catch (IllegalArgumentException e)
        {
            GunsmithLogger.getLogger().error(e, "Could not create a new instance of the container");
        } catch (SecurityException e)
        {
            GunsmithLogger.getLogger().error(e, "Could not create a new instance of the container");
        }

    }

    @Override
    public Optional<ConfigurationContainer> getContainer(String containerName)
    {
        check("getContainer");
        checkNotNull(containerName, "Name cannot be null!");
        checkArgument(!containerName.isEmpty(), "Name cannot be empty");
        return Optional.fromNullable(this.containers.get(containerName));
    }

    @Override
    public ConfigurationContainer[] getContainers()
    {
        check("getContainers");
        return this.containers.values().toArray(new ConfigurationContainer[this.containers.values().size()]);
    }

    @Override
    public boolean has(String name)
    {
        check("has");
        return this.config.containsKey(name);
    }

    @Override
    public void fromContainer(DataContainer container)
    {
        check("fromContainer");
        for (String s : container.keySet())
        {
            if (this.containers.containsKey(s))
            {
                Optional<DataContainer> data = container.getContainer(s);
                if (data.isPresent())
                {
                    this.containers.get(s).fromContainer(data.get());
                }
            }
        }
    }

    @Override
    public DataContainer toContainer()
    {
        check("toContainer");
        DataContainer container = new MemoryContainer("");
        for (String key : this.containers.keySet())
        {
            ConfigurationContainer c = this.containers.get(key);
            container.setContainer(c.getClass().getName(), c.toContainer());
        }
        return container;
    }

    @Override
    public void refreshAllContainers()
    {
        check("refreshAllContainers");
        for (String key : this.containers.keySet())
        {
            ConfigurationContainer c = this.containers.get(key);
            DataContainer data = c.toContainer();
            for (Map.Entry<String, Object> entry : data.entrySet())
            {
                set(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void refreshContainer(String containerName)
    {
        check("refreshContainer");
        Optional<ConfigurationContainer> container = getContainer(containerName);
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
