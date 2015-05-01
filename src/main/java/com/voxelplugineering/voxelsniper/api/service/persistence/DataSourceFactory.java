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

import com.google.common.base.Optional;

/**
 * A factory into which data source types may be registered along with their
 * {@link DataSourceBuilder}.
 */
public interface DataSourceFactory
{

    /**
     * Requests a new instance of a {@link DataSource} by name.
     * 
     * @param name The source name
     * @return A new instance of the data source, if successful
     */
    Optional<DataSource> build(String name);

    /**
     * Requests a new instance of a {@link DataSource} by name with the given arguments.
     * 
     * @param name The source name
     * @param args The source arguments
     * @return A new instance of the data source, if successful
     */
    Optional<DataSource> build(String name, DataContainer args);

    /**
     * Requests a new instance of a {@link DataSource} from a class reference.
     * 
     * @param type The data source type
     * @param <T> The type
     * @return A new instance of the data source, if successful
     */
    <T extends DataSource> Optional<T> build(Class<T> type);

    /**
     * Requests a new instance of a {@link DataSource} from a class reference, with the given
     * arguments.
     * 
     * @param type The data source type
     * @param args The source arguments
     * @param <T> The type
     * @return A new instance of the data source, if successful
     */
    <T extends DataSource> Optional<T> build(Class<T> type, DataContainer args);

    /**
     * Registers a new {@link DataSource} to this factory with an associated
     * {@link DataSourceBuilder}.
     * 
     * @param name The data source name
     * @param type The data source class
     * @param <T> The type
     * @param builder The data source builder
     */
    <T extends DataSource> void register(String name, Class<T> type, DataSourceBuilder<T> builder);

    /**
     * Removes a {@link DataSource} from this factory by name.
     * 
     * @param name The name
     * @return If the factory contained the source
     */
    boolean remove(String name);

    /**
     * Clears all {@link DataSource}s from this factory.
     */
    void clear();

}
