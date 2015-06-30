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

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

/**
 * An enumeration of data types accepted by a {@link DataContainer}.
 */
@SuppressWarnings("javadoc")
public class DataType
{

    public static final DataType BYTE = new DataType(byte.class, Byte.class);
    public static final DataType BYTE_ARRAY = new DataType(byte[].class, Byte[].class);
    public static final DataType SHORT = new DataType(short.class, Short.class);
    public static final DataType INT = new DataType(int.class, Integer.class);
    public static final DataType LONG = new DataType(long.class, Long.class);
    public static final DataType CHAR = new DataType(char.class, Character.class);
    public static final DataType FLOAT = new DataType(float.class, Float.class);
    public static final DataType DOUBLE = new DataType(double.class, Double.class);
    public static final DataType STRING = new DataType(String.class);
    public static final DataType BOOLEAN = new DataType(boolean.class, Boolean.class);
    public static final DataType CONTAINER = new DataType(DataContainer.class);
    public static final DataType LIST = new DataType(List.class);
    public static final DataType CUSTOM = new DataType(DataSerializable.class);

    private static final Map<Class<?>, DataType> TYPES;

    static
    {
        TYPES = Maps.newHashMap();
        TYPES.put(byte.class, BYTE);
        TYPES.put(Byte.class, BYTE);
        TYPES.put(byte[].class, BYTE_ARRAY);
        TYPES.put(Byte[].class, BYTE_ARRAY);
        TYPES.put(short.class, SHORT);
        TYPES.put(Short.class, SHORT);
        TYPES.put(int.class, INT);
        TYPES.put(Integer.class, INT);
        TYPES.put(long.class, LONG);
        TYPES.put(Long.class, LONG);
        TYPES.put(char.class, CHAR);
        TYPES.put(Character.class, CHAR);
        TYPES.put(float.class, FLOAT);
        TYPES.put(Float.class, FLOAT);
        TYPES.put(double.class, DOUBLE);
        TYPES.put(Double.class, DOUBLE);
        TYPES.put(String.class, STRING);
        TYPES.put(boolean.class, BOOLEAN);
        TYPES.put(Boolean.class, BOOLEAN);
        TYPES.put(DataContainer.class, CONTAINER);
        TYPES.put(List.class, LIST);
        TYPES.put(DataSerializable.class, CUSTOM);
    }

    /**
     * Gets the {@link DataType} which corresponds to the given type.
     * 
     * @param type The requested type
     * @return The data type, if available
     */
    public static Optional<DataType> getTypeFor(Class<?> type)
    {
        return Optional.fromNullable(TYPES.get(type));
    }

    /**
     * Gets whether the given type has a corresponding {@link DataType}.
     * 
     * @param type The requested type
     * @return Is valid
     */
    public static boolean isValidType(Class<?> type)
    {
        return TYPES.containsKey(type);
    }

    private final Class<?>[] types;

    private DataType(Class<?>... types)
    {
        this.types = types;
    }

    /**
     * Gets whether the given type is a valid type for this data type.
     * 
     * @param check The type to check
     * @return Is valid
     */
    public boolean isOfType(Class<?> check)
    {
        for (Class<?> type : this.types)
        {
            if (type.isAssignableFrom(check))
            {
                return true;
            }
        }
        return false;
    }

}
