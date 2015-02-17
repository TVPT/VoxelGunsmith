package com.voxelplugineering.voxelsniper.service.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;
import com.voxelplugineering.voxelsniper.util.nbt.ByteArrayTag;
import com.voxelplugineering.voxelsniper.util.nbt.ByteTag;
import com.voxelplugineering.voxelsniper.util.nbt.CompoundTag;
import com.voxelplugineering.voxelsniper.util.nbt.DoubleTag;
import com.voxelplugineering.voxelsniper.util.nbt.FloatTag;
import com.voxelplugineering.voxelsniper.util.nbt.IntArrayTag;
import com.voxelplugineering.voxelsniper.util.nbt.IntTag;
import com.voxelplugineering.voxelsniper.util.nbt.ListTag;
import com.voxelplugineering.voxelsniper.util.nbt.LongTag;
import com.voxelplugineering.voxelsniper.util.nbt.NBTInputStream;
import com.voxelplugineering.voxelsniper.util.nbt.ShortTag;
import com.voxelplugineering.voxelsniper.util.nbt.StringTag;
import com.voxelplugineering.voxelsniper.util.nbt.Tag;

/**
 * A {@link DataSource} which reads from a file as NBT.
 */
@SuppressWarnings("rawtypes")
public class NBTDataSource extends FileDataSource
{

    /**
     * A {@link FileDataSource.Builder} for building NBT data sources.
     */
    public static Builder BUILDER = new Builder()
    {

        @Override
        public DataSource build(File f)
        {
            return new NBTDataSource(f);
        }

    };

    /**
     * Creates a new {@link NBTDataSource} for the given file.
     * 
     * @param file The file
     */
    public NBTDataSource(File file)
    {
        super(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(DataSerializable serial) throws IOException
    {
        write(serial.toContainer());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(DataContainer container) throws IOException
    {

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

        for (Map.Entry<String, Object> entry : container.extrySet())
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
                tags.put(key, new ByteTag(key, (byte) value));
            } else if (type == short.class || type == Short.class)
            {
                tags.put(key, new ShortTag(key, (short) value));
            } else if (type == int.class || type == Integer.class)
            {
                tags.put(key, new IntTag(key, (int) value));
            } else if (type == long.class || type == Long.class)
            {
                tags.put(key, new LongTag(key, (long) value));
            } else if (type == float.class || type == Float.class)
            {
                tags.put(key, new FloatTag(key, (float) value));
            } else if (type == double.class || type == Double.class)
            {
                tags.put(key, new DoubleTag(key, (double) value));
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
                            integers[i] = (int) list.get(i);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public DataContainer read() throws IOException
    {
        FileInputStream stream = new FileInputStream(this.file);
        NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(stream));

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

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DataSerializable> T read(T object) throws IOException
    {
        object.fromContainer(read());
        return object;
    }

}
