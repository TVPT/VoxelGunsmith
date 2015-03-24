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
package com.voxelplugineering.voxelsniper.api.service.persistence;

import java.util.List;

import com.google.common.base.Optional;

/**
 * Represents a mutable container of values.
 */
public interface DataContainer extends DataView
{

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<DataContainer> readContainer(String path);

    /**
     * Writes a byte to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeByte(String path, byte data);

    /**
     * Writes a byte array to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeByteArray(String path, byte[] data);

    /**
     * Writes a short to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeShort(String path, short data);

    /**
     * Writes an integer to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeInt(String path, int data);

    /**
     * Writes a long to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeLong(String path, long data);

    /**
     * Writes a character to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeChar(String path, char data);

    /**
     * Writes a float to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeFloat(String path, float data);

    /**
     * Writes a double to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeDouble(String path, double data);

    /**
     * Writes a String to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeString(String path, String data);

    /**
     * Writes a boolean to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeBoolean(String path, boolean data);

    /**
     * Writes a type extending {@link DataSerializable} to the given path. The
     * type is written as if by calling {@link DataSerializable#toContainer()}
     * and passing it to {@link #writeContainer}.
     * 
     * @param path The path
     * @param data The value
     * @param <T> The {@link DataSerializable} type
     */
    <T extends DataSerializable> void writeCustom(String path, T data);

    /**
     * Writes a {@link DataContainer} to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeContainer(String path, DataContainer data);

    /**
     * Writes a Number to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeNumber(String path, Number data);

    /**
     * Writes a List to the given path.
     * 
     * @param path The path
     * @param data The value
     */
    void writeList(String path, List<?> data);

    /**
     * Attempts to write the given object to the container.
     * 
     * @param path The path
     * @param value The value
     */
    void write(String path, Object value);
    
    /**
     * Writes all values from the given container to this container.
     * 
     * @param container The source container
     */
    void writeAll(DataContainer container);

    /**
     * Removes the given path from this container.
     * 
     * @param path The path
     * @return If the path was set within this container
     */
    boolean remove(String path);

    /**
     * Clears all data from this container.
     */
    void clear();

}
