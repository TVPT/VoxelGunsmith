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
package com.voxelplugineering.voxelsniper.service.config;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.service.AbstractService;
import com.voxelplugineering.voxelsniper.service.meta.AnnotationConsumer;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.DataTranslator;
import com.voxelplugineering.voxelsniper.util.StringUtilities;

import com.google.common.collect.Maps;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * The configuration storage.
 */
public class ConfigurationService extends AbstractService implements Configuration, AnnotationConsumer
{

    private static final String FILE_EXT = ".hocon";

    private final Map<String, ConfigContainer> containers;

    /**
     * Creates a new {@link ConfigurationService}.
     * 
     * @param context The context
     */
    public ConfigurationService(Context context)
    {
        super(context);
        this.containers = Maps.newHashMap();
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
    public void register(Class<?> cls)
    {
        checkArgument(cls.isAnnotationPresent(ConfigurationContainer.class));
        GunsmithLogger.getLogger().debug("Registered configuration container: " + cls.getName());
        ConfigurationContainer container = cls.getAnnotation(ConfigurationContainer.class);
        if (this.containers.containsKey(container.name()))
        {
            throw new IllegalArgumentException("Attempted to register a container with a duplicate name.");
        }
        ConfigContainer conf = new ConfigContainer(container);
        this.containers.put(container.name(), conf);
        for (Field f : cls.getFields())
        {
            if (Modifier.isStatic(f.getModifiers()))
            {
                f.setAccessible(true);
                String name = container.root();
                if (!name.isEmpty() && !name.endsWith("."))
                {
                    name += ".";
                }
                ConfigValue cv = null;
                if (f.isAnnotationPresent(ConfigValue.class))
                {
                    cv = f.getAnnotation(ConfigValue.class);
                    name += cv.section();
                    if (!name.isEmpty() && !name.endsWith("."))
                    {
                        name += ".";
                    }
                    if (cv.name().isEmpty())
                    {
                        name += f.getName();
                    } else
                    {
                        name += cv.name();
                    }
                } else
                {
                    name += f.getName();
                }
                if (conf.entries.containsKey(name))
                {
                    GunsmithLogger.getLogger().warn("Attempted to register duplicate config field " + name);
                    continue;
                }
                ConfigEntry entry = new ConfigEntry(conf, name, f);
                conf.getEntries().put(name, entry);
            }
        }
    }

    @Override
    public void initialize(File dir, boolean includeHidden) throws IOException
    {
        checkNotNull(dir);
        checkArgument(!dir.isFile());
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        for (ConfigContainer conf : this.containers.values())
        {
            String filename = conf.getContainer().name() + FILE_EXT;
            File confFile = new File(dir, filename);
            if (confFile.exists())
            {
                conf.load(confFile);
                conf.save(confFile, includeHidden);
            } else
            {
                if (conf.getContainer().createByDefault() || includeHidden)
                {
                    confFile.createNewFile();
                    conf.save(confFile, includeHidden);
                }
            }
        }
    }

    @Override
    public void save(File dir, boolean includeHidden) throws IOException
    {
        checkNotNull(dir);
        checkArgument(!dir.isFile());
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        for (ConfigContainer conf : this.containers.values())
        {
            String filename = conf.getContainer().name() + FILE_EXT;
            File confFile = new File(dir, filename);
            if (confFile.exists())
            {
                conf.save(confFile, includeHidden);
            } else if (conf.getContainer().createByDefault() || includeHidden)
            {
                confFile.createNewFile();
                conf.save(confFile, includeHidden);
            }
        }
    }

    @Override
    public void load(File dir) throws IOException
    {
        checkNotNull(dir);
        checkArgument(!dir.isFile());
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        for (ConfigContainer conf : this.containers.values())
        {
            String filename = conf.getContainer().name() + FILE_EXT;
            File confFile = new File(dir, filename);
            if (confFile.exists())
            {
                conf.load(confFile);
            }
        }
    }

    @Override
    public void consume(Class<?> cls)
    {
        register(cls);
    }

    private static class ConfigContainer
    {
        private final Map<String, ConfigEntry> entries;
        private final ConfigurationContainer container;

        public ConfigContainer(ConfigurationContainer container)
        {
            this.container = container;
            this.entries = Maps.newHashMap();
        }

        public void save(File confFile, boolean includeHidden) throws IOException
        {
            GunsmithLogger.getLogger().debug("Saving " + this.container.name() + " to " + confFile.getAbsolutePath());
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(confFile).build();
            ConfigurationNode node = loader.load();

            for (ConfigEntry entry : this.entries.values())
            {
                if (entry.annotation != null && entry.annotation.hidden() && !includeHidden)
                {
                    continue;
                }
                Object value = entry.getValue();
                if (value != null)
                {
                    node.getNode((Object[]) entry.getFullPath()).setValue(value);
                }
            }
            loader.save(node);
        }

        public void load(File confFile) throws IOException
        {
            GunsmithLogger.getLogger().debug("Loading " + this.container.name() + " from " + confFile.getAbsolutePath());
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(confFile).build();
            ConfigurationNode node = loader.load();

            for (ConfigEntry entry : this.entries.values())
            {
                ConfigurationNode e = node.getNode((Object[]) entry.getFullPath());
                if (e.getValue() != null)
                {
                    Optional<?> attempt = DataTranslator.attempt(e.getValue(), entry.getType());
                    if (attempt.isPresent())
                    {
                        entry.set(attempt.get());
                    } else
                    {
                        GunsmithLogger.getLogger().warn("Failed to translate config value " + entry.getFullName() + " from " + e.getValue().getClass()
                                + " to " + entry.getType());
                    }
                }
            }
        }

        public ConfigurationContainer getContainer()
        {
            return this.container;
        }

        public Map<String, ConfigEntry> getEntries()
        {
            return this.entries;
        }

    }

    @SuppressWarnings("unused")
    private static class ConfigEntry
    {

        private final ConfigContainer container;
        private final String name;
        private final Field field;
        private final String[] path;
        private ConfigValue annotation;

        public ConfigEntry(ConfigContainer container, String name, Field f)
        {
            this.container = container;
            this.field = f;
            this.annotation = f.getAnnotation(ConfigValue.class);
            if(name.indexOf(".") != -1) {
                String[] split = name.split("\\.");
                this.name = split[split.length-1];
                this.path = Arrays.copyOf(split, split.length-1);
            } else {
                this.name = name;
                this.path = new String[0];
            }
            GunsmithLogger.getLogger().debug("Created config entry: " + getFullName());
        }

        public Class<?> getType()
        {
            return this.field.getType();
        }

        public Object getValue()
        {
            try
            {
                return this.field.get(null);
            } catch (Exception e)
            {
                GunsmithLogger.getLogger().error(e, "Error getting config value from field");
                return null;
            }
        }

        public ConfigContainer getContainer()
        {
            return this.container;
        }

        public String getFullName()
        {
            String p = StringUtilities.join(this.path, ".");
            return p + (p.isEmpty() ? "" : ".") + this.name;
        }

        public String getName()
        {
            return this.name;
        }

        public String[] getPath()
        {
            return this.path;
        }

        public String[] getFullPath()
        {
            String[] full = Arrays.copyOf(this.path, this.path.length + 1);
            full[this.path.length] = this.name;
            return full;
        }

        public void set(Object o)
        {
            try
            {
                this.field.set(null, o);
            } catch (Exception e)
            {
                GunsmithLogger.getLogger().error(e, "Error setting config value into target");
            }
        }

    }

}
