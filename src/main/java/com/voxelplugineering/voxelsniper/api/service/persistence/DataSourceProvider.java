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
 * A provider for {@link DataSourceReader}s from a common root.
 */
public interface DataSourceProvider
{

    /**
     * Gets whether this provider has a {@link DataSourceReader} within it
     * matching the given identifier.
     * 
     * @param identifier The identifier
     * @return Whether the source exists
     */
    boolean has(String identifier);

    /**
     * Gets the {@link DataSourceReader} matching the given identifier if it is
     * available.
     * 
     * @param identifier The identifier
     * @return The source, if available
     */
    Optional<DataSource> get(String identifier);

    /**
     * Attempts to get another provider from within this providers scope. Allows
     * for {@link DataSourceProvider}s to be nested recursively as it is within
     * for example: a filesystem.
     * 
     * @param identifier The identifier
     * @return The provider, if available
     */
    Optional<? extends DataSourceProvider> getInternalProvider(String identifier);

    /**
     * Sets a default reader type for this provider to construct any
     * {@link DataSource}s fetched from it within.
     * 
     * @param reader The reader type
     * @param args The reader arguments
     */
    void setReaderType(Class<? extends DataSourceReader> reader, DataContainer args);

    /**
     * Gets a new {@link DataSource} from within this provider wrapped within
     * the default reader (See {@link #setReaderType(Class, DataContainer)}).
     * 
     * @param identifier The identifier
     * @return The data source, if available
     */
    Optional<DataSourceReader> getWithReader(String identifier);

    /**
     * Gets a new {@link DataSource} from within this provider wrapped within
     * the given reader.
     * 
     * @param identifier The identifier
     * @param reader The reader type
     * @param args The reader arguments
     * @param <T> The type
     * @return The data source, if available
     */
    <T extends DataSourceReader> Optional<T> getWithReader(String identifier, Class<T> reader, DataContainer args);

    /**
     * Gets a new {@link DataSource} from within this provider wrapped within
     * the given reader.
     * 
     * @param identifier The identifier
     * @param reader The reader type
     * @param <T> The type
     * @return The data source, if available
     */
    <T extends DataSourceReader> Optional<T>  getWithReader(String identifier, Class<T> reader);

}
