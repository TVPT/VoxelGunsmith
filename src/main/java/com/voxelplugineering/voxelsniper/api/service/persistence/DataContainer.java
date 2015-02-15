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

}
