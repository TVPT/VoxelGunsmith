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
package com.voxelplugineering.voxelsniper.core.service.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceBuilder;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.core.util.nbt.ByteArrayTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.ByteTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.CompoundTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.DoubleTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.FloatTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.IntArrayTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.IntTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.ListTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.LongTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.NBTInputStream;
import com.voxelplugineering.voxelsniper.core.util.nbt.NBTOutputStream;
import com.voxelplugineering.voxelsniper.core.util.nbt.ShortTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.StringTag;
import com.voxelplugineering.voxelsniper.core.util.nbt.Tag;

/**
 * A {@link DataSourceReader} which reads from a file as NBT.
 */
@SuppressWarnings("rawtypes")
public class NBTDataSourceReader implements DataSourceReader
{

    /**
     * A {@link DataSourceBuilder} for nbt data sources.
     */
    /*public static final DataSourceBuilder BUILDER = new DataSourceBuilder()
    {

        @Override
        public Optional<DataSource> build(DataContainer args)
        {
            if (!args.containsKey("source") || !args.containsKey("sourceArgs"))
            {
                Gunsmith.getLogger().warn("Failed to build NBTDataSourceReader, invalid args");
                return Optional.absent();
            }
            String sourceName = args.readString("source").get();
            Optional<DataSource> source = Gunsmith.getPersistence().build(sourceName, args.readContainer("sourceArgs").get());
            if (!source.isPresent())
            {
                Gunsmith.getLogger().warn("Failed to build data source for NBTDataSourceReader");
                return Optional.absent();
            }
            if (!StreamDataSource.class.isAssignableFrom(source.get().getClass()))
            {
                Gunsmith.getLogger().warn("Failed to build NBTDataSourceReader: Source was not a StreamDataSource");
                return Optional.absent();
            }
            StreamDataSource stream = (StreamDataSource) source.get();
            return Optional.<DataSource>of(new JsonDataSourceReader(stream));
        }

    };*/

    private final StreamDataSource source;

    /**
     * Creates a new {@link NBTDataSourceReader} for the given data source.
     * 
     * @param source The DataSource
     */
    public NBTDataSourceReader(StreamDataSource source)
    {
        this.source = source;
    }

    @Override
    public void write(DataSerializable serial) throws IOException
    {
        write(serial.toContainer());
    }

    @Override
    public void write(DataContainer container) throws IOException
    {
        CompoundTag tag = fromContainer(container);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        NBTOutputStream output = new NBTOutputStream(new GZIPOutputStream(baos));

        output.writeTag(tag);
        output.close();

        this.source.write(baos.toByteArray());
    }

    /**
     * Converts the given {@link DataContainer} to an NBT {@link CompoundTag}.
     * 
     * @param container The container to convert
     * @return The tag
     */
    public static CompoundTag fromContainer(DataContainer container)
    {
        Map<String, Tag> tags = Maps.newHashMap();

        for (Map.Entry<String, Object> entry : container.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            Class<?> type = value.getClass();
            if (DataContainer.class.isAssignableFrom(value.getClass()))
            {
                tags.put(key, fromContainer((DataContainer) value));
            } else if (DataSerializable.class.isAssignableFrom(value.getClass()))
            {
                tags.put(key, fromContainer(((DataSerializable) value).toContainer()));
            } else if (type == byte.class || type == Byte.class)
            {
                tags.put(key, new ByteTag(key, (Byte) value));
            } else if (type == short.class || type == Short.class)
            {
                tags.put(key, new ShortTag(key, (Short) value));
            } else if (type == int.class || type == Integer.class)
            {
                tags.put(key, new IntTag(key, (Integer) value));
            } else if (type == long.class || type == Long.class)
            {
                tags.put(key, new LongTag(key, (Long) value));
            } else if (type == float.class || type == Float.class)
            {
                tags.put(key, new FloatTag(key, (Float) value));
            } else if (type == double.class || type == Double.class)
            {
                tags.put(key, new DoubleTag(key, (Double) value));
            } else if (type == Number.class)
            {
                tags.put(key, new DoubleTag(key, ((Number) value).doubleValue()));
            } else if (type == List.class)
            {
                List list = (List) value;
                if (list.size() > 0)
                {
                    if (list.get(0).getClass() == int.class || list.get(0).getClass() == Integer.class)
                    {
                        int[] integers = new int[list.size()];
                        for (int i = 0; i < list.size(); i++)
                        {
                            integers[i] = (Integer) list.get(i);
                        }
                        tags.put(key, new IntArrayTag(key, integers));
                        continue;
                    }
                }
                tags.put(key, convertListToTag(list));
            }
        }

        return new CompoundTag(container.getPath(), tags);
    }

    private static ListTag convertListToTag(List list)
    {
        // TODO
        return null;
    }

    @Override
    public DataContainer read() throws IOException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(this.source.read());
        NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(bais));

        CompoundTag rootTag = (CompoundTag) nbtStream.readTag();
        nbtStream.close();

        DataContainer data = toContainer(rootTag);
        return data;
    }

    /**
     * Convert a {@link CompoundTag} to a {@link DataContainer}.
     * 
     * @param rootTag The tag to convert
     * @return The container representing the tag
     */
    public static DataContainer toContainer(CompoundTag rootTag)
    {
        MemoryContainer data = new MemoryContainer("");

        Map<String, Tag> tags = rootTag.getValue();

        for (Map.Entry<String, Tag> entry : tags.entrySet())
        {
            Tag value = entry.getValue();
            String key = entry.getKey();
            if (value instanceof ByteArrayTag)
            {
                data.writeByteArray(key, ((ByteArrayTag) value).getValue());
            }
            if (value instanceof ByteTag)
            {
                data.writeByte(key, ((ByteTag) value).getValue());
            }
            if (value instanceof CompoundTag)
            {
                data.writeContainer(key, toContainer((CompoundTag) value));
            }
            if (value instanceof DoubleTag)
            {
                data.writeDouble(key, ((DoubleTag) value).getValue());
            }
            if (value instanceof FloatTag)
            {
                data.writeFloat(key, ((FloatTag) value).getValue());
            }
            if (value instanceof IntArrayTag)
            {
                // Convert int array tags to a list of integers
                int[] array = ((IntArrayTag) value).getValue();
                List<Integer> list = Lists.newArrayList();
                for (int i : array)
                {
                    list.add(i);
                }
                data.writeList(key, list);
            }
            if (value instanceof IntTag)
            {
                data.writeInt(key, ((IntTag) value).getValue());
            }
            if (value instanceof ListTag)
            {
                data.writeList(key, convertListFromTag(((ListTag) value).getValue()));
            }
            if (value instanceof LongTag)
            {
                data.writeLong(key, ((LongTag) value).getValue());
            }
            if (value instanceof ShortTag)
            {
                data.writeShort(key, ((ShortTag) value).getValue());
            }
            if (value instanceof StringTag)
            {
                data.writeString(key, ((StringTag) value).getValue());
            }
        }

        return data;
    }

    @SuppressWarnings("unchecked")
    private static List convertListFromTag(List<Tag> value)
    {
        List data = Lists.newArrayList();
        for (Tag t : value)
        {
            if (t instanceof CompoundTag)
            {
                data.add(toContainer((CompoundTag) t.getValue()));
            } else if (t instanceof ListTag)
            {
                data.add(convertListFromTag(((ListTag) t).getValue()));
            } else
            {
                data.add(t.getValue());
            }
        }
        return data;
    }

    @Override
    public <T extends DataSerializable> T read(T object) throws IOException
    {
        object.fromContainer(read());
        return object;
    }

    @Override
    public Optional<String> getName()
    {
        return this.source.getName();
    }

    @Override
    public boolean exists()
    {
        return this.source.exists();
    }

}
