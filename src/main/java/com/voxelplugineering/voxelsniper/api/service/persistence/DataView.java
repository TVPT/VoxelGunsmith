package com.voxelplugineering.voxelsniper.api.service.persistence;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;

/**
 * A recursive view into a data container.
 */
public interface DataView
{

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<Byte> readByte(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<byte[]> readByteArray(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<Short> readShort(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<Integer> readInt(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<Long> readLong(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<Character> readChar(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<Float> readFloat(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<Double> readDouble(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<String> readString(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<Boolean> readBoolean(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead. If the data at the path is a {@link DataContainer}
     * rather than the given type then the type will be checked for a builder
     * method (as defined here: {@link DataSerializable}) which will be used to
     * create a new instance of the type.
     * 
     * @param path The path to read from
     * @param type The type to read as
     * @return The data
     */
    <T extends DataSerializable> Optional<T> readCustom(String path, Class<T> type);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    Optional<? extends DataView> readContainer(String path);

    /**
     * Reads from the given path. If the path does not exist, or if the type at
     * the path is not compatible then {@link Optional#absent()} will be
     * returned instead.
     * 
     * @param path The path to read from
     * @return The data
     */
    @SuppressWarnings("rawtypes")
    Optional<List> readList(String path);

    /**
     * Gets the root path of this view.
     * 
     * @return The path
     */
    String getPath();

    /**
     * Gets a set of all keys in this data view. This does not include keys of
     * contained views.
     * 
     * @return The keyset
     */
    Set<String> keySet();

    /**
     * Gets a set of all entries in this data view. The returned values are
     * returned as an instance of {@link java.util.Map.Entry}.
     * 
     * @return The entries
     */
    Set<Map.Entry<String, Object>> extrySet();

    /**
     * Gets whether the given path exists.
     * 
     * @param path The path to check
     * @return Whether it exists
     */
    boolean contains(String path);

}