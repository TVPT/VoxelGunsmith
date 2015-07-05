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

import java.lang.reflect.Field;
import java.util.Map;

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.service.persistence.DataSerializable;
import com.voxelplugineering.voxelsniper.service.persistence.MemoryContainer;

/**
 * An abstract configuration container which supports serializing configuration values stored in the
 * fields of the class.
 */
public abstract class ConfigurationContainer implements DataSerializable
{

    @Override
    public void fromContainer(DataContainer container)
    {
        for (Map.Entry<String, Object> entry : container.entrySet())
        {
            try
            {
                Field f = this.getClass().getDeclaredField(entry.getKey());
                f.setAccessible(true);
                if (entry.getValue() instanceof Number)
                {
                    Number value = (Number) entry.getValue();
                    Class<?> fieldType = f.getType();
                    if (fieldType == Byte.class || fieldType == byte.class)
                    {
                        f.set(this, value.byteValue());
                    } else if (fieldType == Short.class || fieldType == short.class)
                    {
                        f.set(this, value.shortValue());
                    } else if (fieldType == Integer.class || fieldType == int.class)
                    {
                        f.set(this, value.intValue());
                    } else if (fieldType == Float.class || fieldType == float.class)
                    {
                        f.set(this, value.floatValue());
                    } else if (fieldType == Double.class || fieldType == double.class)
                    {
                        f.set(this, value.doubleValue());
                    } else if (fieldType == Long.class || fieldType == long.class)
                    {
                        f.set(this, value.longValue());
                    } else
                    {
                        GunsmithLogger.getLogger().error("Failed to load config value " + entry.getKey() + ": " + fieldType.getName()
                                + " is not a recognized Number.");
                        continue;
                    }
                } else
                {
                    f.set(this, entry.getValue());
                }
            } catch (Exception e)
            {
                GunsmithLogger.getLogger().error(e, "Failed to load config value " + entry.getKey());
                continue;
            }

        }
    }

    @Override
    public DataContainer toContainer()
    {
        DataContainer data = new MemoryContainer();
        for (Field f : this.getClass().getDeclaredFields())
        {
            f.setAccessible(true);
            try
            {
                data.set(f.getName(), f.get(this));
            } catch (Exception e)
            {
                GunsmithLogger.getLogger().error(e, "Failed to serialize " + f.getName() + " from configuration.");
                continue;
            }
        }
        return data;
    }

}
