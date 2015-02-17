package com.voxelplugineering.voxelsniper.service.persistence;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;

/**
 * An in-memory {@link DataContainer} implementation which is backed by a
 * {@link Map}.
 */
public class MemoryContainer implements DataContainer
{

    private final static String PATH_SEPARATOR = ".";
    private final String path;
    private final Map<String, Object> data;

    /**
     * Creates a new {@link MemoryContainer}.
     * 
     * @param path The path of this container
     */
    public MemoryContainer(String path)
    {
        this.data = Maps.newConcurrentMap();
        if (path.contains(PATH_SEPARATOR))
        {
            String[] sp = path.split(PATH_SEPARATOR);
            this.path = sp[0];
            this.data.put(sp[1], new MemoryContainer(path.substring(path.indexOf(PATH_SEPARATOR) + 1)));
        } else
        {
            this.path = path;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath()
    {
        return this.path;
    }

    private static <T> Optional<T> safeCast(Object value, Class<T> type)
    {
        try
        {
            return Optional.fromNullable(type.cast(value));
        } catch (ClassCastException e)
        {
            return Optional.absent();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Byte> readByte(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readByte(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            try
            {
                return Optional.of(Number.class.cast(this.data.get(path)).byteValue());
            } catch (ClassCastException e)
            {
                return Optional.absent();
            }
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Short> readShort(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readShort(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            try
            {
                return Optional.fromNullable(Number.class.cast(this.data.get(path)).shortValue());
            } catch (ClassCastException e)
            {
                return Optional.absent();
            }
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Integer> readInt(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readInt(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            try
            {
                return Optional.fromNullable(Number.class.cast(this.data.get(path)).intValue());
            } catch (ClassCastException e)
            {
                return Optional.absent();
            }
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Long> readLong(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readLong(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            try
            {
                return Optional.fromNullable(Number.class.cast(this.data.get(path)).longValue());
            } catch (ClassCastException e)
            {
                return Optional.absent();
            }
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Character> readChar(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readChar(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            Object obj = this.data.get(path);
            if (obj.getClass() == Character.class)
            {
                return safeCast(obj, Character.class);
            } else if (obj.getClass() == String.class)
            {
                String s = (String) obj;
                if (s.length() == 1)
                {
                    return Optional.of(s.charAt(0));
                }
            }
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Float> readFloat(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readFloat(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            try
            {
                return Optional.fromNullable(Number.class.cast(this.data.get(path)).floatValue());
            } catch (ClassCastException e)
            {
                return Optional.absent();
            }
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Double> readDouble(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readDouble(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            try
            {
                return Optional.fromNullable(Number.class.cast(this.data.get(path)).doubleValue());
            } catch (ClassCastException e)
            {
                return Optional.absent();
            }
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> readString(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readString(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return safeCast(this.data.get(path), String.class);
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Boolean> readBoolean(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readBoolean(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return safeCast(this.data.get(path), Boolean.class);
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DataSerializable> Optional<T> readCustom(String path, Class<T> type)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readCustom(path.substring(index + 1), type);
            }
        } else if (this.data.containsKey(path))
        {
            Object obj = this.data.get(path);
            if (type.isAssignableFrom(obj.getClass()))
            {
                return safeCast(obj, type);
            } else if (DataContainer.class.isAssignableFrom(obj.getClass()))
            {
                try
                {
                    Method builder = type.getMethod("buildFromContainer", DataContainer.class);
                    if (validateBuilder(builder))
                    {
                        Object built = builder.invoke(null, (DataContainer) obj);
                        if(built != null)
                        {
                            return safeCast(built, type);
                        }
                        else
                        {
                            return Optional.absent();
                        }
                    }
                } catch (Exception e)
                {
                    return Optional.absent();
                }
            }
        }
        return Optional.absent();
    }

    private static boolean validateBuilder(Method m)
    {
        if (!m.isAccessible())
        {
            m.setAccessible(true);
        }
        if (!Modifier.isStatic(m.getModifiers()))
        {
            return false;
        }
        if (m.getParameterTypes().length != 1)
        {
            return false;
        }
        if (m.getParameterTypes()[0] != DataContainer.class)
        {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DataContainer> readContainer(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readContainer(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return safeCast(this.data.get(path), DataContainer.class);
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeByte(String path, byte data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeByte(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeByte(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeShort(String path, short data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeShort(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeShort(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeInt(String path, int data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeInt(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeInt(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeLong(String path, long data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeLong(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeLong(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeChar(String path, char data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeChar(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeChar(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFloat(String path, float data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeFloat(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeFloat(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeDouble(String path, double data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeDouble(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeDouble(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeString(String path, String data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeString(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeString(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeBoolean(String path, boolean data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeBoolean(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeBoolean(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeNumber(String key, Number data)
    {
        if (key.contains(PATH_SEPARATOR))
        {
            int index = key.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(key.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeNumber(key.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(key.substring(0, index));
                this.data.put(key.substring(0, index), c);
                c.writeNumber(key.substring(index + 1), data);
            }
        } else
        {
            this.data.put(key, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DataSerializable> void writeCustom(String path, T data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeCustom(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeCustom(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeContainer(String path, DataContainer data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeContainer(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeContainer(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> keySet()
    {
        return this.data.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Map.Entry<String, Object>> extrySet()
    {
        return this.data.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<byte[]> readByteArray(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readByteArray(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return safeCast(this.data.get(path), byte[].class);
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().contains(path.substring(index + 1));
            } else
            {
                return false;
            }
        } else
        {
            return this.data.containsKey(path);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeByteArray(String path, byte[] data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeByteArray(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeByteArray(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Optional<List> readList(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().readList(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return safeCast(this.data.get(path), List.class);
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeList(String path, List<?> data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.readContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().writeList(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.writeList(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

}
