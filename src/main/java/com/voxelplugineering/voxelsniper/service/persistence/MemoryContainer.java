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
package com.voxelplugineering.voxelsniper.service.persistence;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

/**
 * An in-memory {@link DataContainer} implementation which is backed by a {@link Map}.
 */
public class MemoryContainer implements DataContainer
{

    public static final String PATH_SEPARATOR = ".";
    private final String path;
    private final Map<String, Object> data;

    /**
     * Creates a new {@link MemoryContainer}.
     */
    public MemoryContainer()
    {
        this("");
    }

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

    @Override
    public Optional<Byte> getByte(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getByte(path.substring(index + 1));
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

    @Override
    public Optional<Short> getShort(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getShort(path.substring(index + 1));
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

    @Override
    public Optional<Integer> getInt(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getInt(path.substring(index + 1));
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

    @Override
    public Optional<Long> getLong(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getLong(path.substring(index + 1));
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

    @Override
    public Optional<Character> getChar(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getChar(path.substring(index + 1));
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

    @Override
    public Optional<Float> getFloat(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getFloat(path.substring(index + 1));
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

    @Override
    public Optional<Double> getDouble(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getDouble(path.substring(index + 1));
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

    @Override
    public Optional<String> getString(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getString(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return safeCast(this.data.get(path), String.class);
        }
        return Optional.absent();
    }

    @Override
    public Optional<Boolean> getBoolean(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getBoolean(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return safeCast(this.data.get(path), Boolean.class);
        }
        return Optional.absent();
    }

    @Override
    public <T extends DataSerializable> Optional<T> getCustom(String path, Class<T> type)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getCustom(path.substring(index + 1), type);
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
                        if (built != null)
                        {
                            return safeCast(built, type);
                        }
                        return Optional.absent();
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

    @Override
    public Optional<DataContainer> getContainer(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getContainer(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return safeCast(this.data.get(path), DataContainer.class);
        }
        return Optional.absent();
    }

    @Override
    public void setByte(String path, byte data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setByte(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setByte(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public void setShort(String path, short data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setShort(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setShort(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public void setInt(String path, int data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setInt(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setInt(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public void setLong(String path, long data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setLong(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setLong(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public void setChar(String path, char data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setChar(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setChar(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public void setFloat(String path, float data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setFloat(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setFloat(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public void setDouble(String path, double data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setDouble(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setDouble(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public void setString(String path, String data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setString(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setString(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public void setBoolean(String path, boolean data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setBoolean(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setBoolean(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public void setNumber(String key, Number data)
    {
        if (key.contains(PATH_SEPARATOR))
        {
            int index = key.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(key.substring(0, index));
            if (container.isPresent())
            {
                container.get().setNumber(key.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(key.substring(0, index));
                this.data.put(key.substring(0, index), c);
                c.setNumber(key.substring(index + 1), data);
            }
        } else
        {
            this.data.put(key, data);
        }
    }

    @Override
    public <T extends DataSerializable> void setCustom(String path, T data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setCustom(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setCustom(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public void setContainer(String path, DataContainer data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setContainer(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setContainer(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    public DataContainer getOrCreateContainer(String path) {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getOrCreateContainer(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return (DataContainer) this.data.get(path);
        }
        DataContainer c = new MemoryContainer(path);
        this.data.put(path, c);
        return c;
    }

    @Override
    public Set<String> keySet()
    {
        return this.data.keySet();
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet()
    {
        return this.data.entrySet();
    }

    @Override
    public Optional<byte[]> getByteArray(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getByteArray(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return safeCast(this.data.get(path), byte[].class);
        }
        return Optional.absent();
    }

    @Override
    public boolean containsKey(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().containsKey(path.substring(index + 1));
            }
            return false;
        }
        return this.data.containsKey(path);
    }

    @Override
    public void setByteArray(String path, byte[] data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setByteArray(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setByteArray(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Optional<List> getList(String path)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                return container.get().getList(path.substring(index + 1));
            }
        } else if (this.data.containsKey(path))
        {
            return safeCast(this.data.get(path), List.class);
        }
        return Optional.absent();
    }

    @Override
    public void setList(String path, List<?> data)
    {
        if (path.contains(PATH_SEPARATOR))
        {
            int index = path.indexOf(PATH_SEPARATOR);
            Optional<DataContainer> container = this.getContainer(path.substring(0, index));
            if (container.isPresent())
            {
                container.get().setList(path.substring(index + 1), data);
            } else
            {
                DataContainer c = new MemoryContainer(path.substring(0, index));
                this.data.put(path.substring(0, index), c);
                c.setList(path.substring(index + 1), data);
            }
        } else
        {
            this.data.put(path, data);
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void set(String path, Object value)
    {
        Class<?> type = value.getClass();
        if (!DataType.isValidType(type))
        {
            throw new UnsupportedOperationException(type.getName() + " is not a valid data type.");
        }
        if (DataType.CONTAINER.isOfType(type))
        {
            setContainer(path, (DataContainer) value);
        } else if (DataType.CUSTOM.isOfType(type))
        {
            setCustom(path, (DataSerializable) value);
        } else if (DataType.BYTE.isOfType(type))
        {
            setByte(path, (Byte) value);
        } else if (DataType.SHORT.isOfType(type))
        {
            setShort(path, (Short) value);
        } else if (DataType.INT.isOfType(type))
        {
            setInt(path, (Integer) value);
        } else if (DataType.LONG.isOfType(type))
        {
            setLong(path, (Long) value);
        } else if (DataType.CHAR.isOfType(type))
        {
            setChar(path, (Character) value);
        } else if (DataType.FLOAT.isOfType(type))
        {
            setFloat(path, (Float) value);
        } else if (DataType.DOUBLE.isOfType(type))
        {
            setDouble(path, (Double) value);
        } else if (DataType.LIST.isOfType(type))
        {
            setList(path, (List) value);
        } else if (DataType.STRING.isOfType(type))
        {
            setString(path, (String) value);
        } else if (DataType.BYTE_ARRAY.isOfType(type))
        {
            setByteArray(path, (byte[]) value);
        } else if (DataType.BOOLEAN.isOfType(type))
        {
            setBoolean(path, (Boolean) value);
        }
    }

    @Override
    public boolean isEmpty()
    {
        return this.data.isEmpty();
    }

    @Override
    public void setAll(DataContainer container)
    {
        for (Map.Entry<String, Object> entry : container.entrySet())
        {
            set(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean remove(String path)
    {
        return this.data.remove(path) != null;
    }

    @Override
    public void clear()
    {
        this.data.clear();
    }

}
