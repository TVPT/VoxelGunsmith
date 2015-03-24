package com.voxelplugineering.voxelsniper.api.config;

import java.lang.reflect.Field;
import java.util.Map;

import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;
import com.voxelplugineering.voxelsniper.service.persistence.MemoryContainer;

/**
 * An abstract configuration container which supports serializing configuration
 * values stored in the fields of the class.
 */
public class AbstractConfigurationContainer implements DataSerializable
{

    /**
     * {@inheritDoc}
     */
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
                        Gunsmith.getLogger().error(
                                "Failed to load config value " + entry.getKey() + ": " + fieldType.getName() + " is not a recognized Number.");
                        continue;
                    }
                } else
                {
                    f.set(this, entry.getValue());
                }
            } catch (Exception e)
            {
                Gunsmith.getLogger().error(e, "Failed to load config value " + entry.getKey());
                continue;
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataContainer toContainer()
    {
        DataContainer data = new MemoryContainer();
        for (Field f : this.getClass().getDeclaredFields())
        {
            f.setAccessible(true);
            try
            {
                data.write(f.getName(), f.get(this));
            } catch (Exception e)
            {
                Gunsmith.getLogger().error(e, "Failed to serialize " + f.getName() + " from configuration.");
                continue;
            }
        }
        return data;
    }

}
