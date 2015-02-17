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

import java.io.IOException;

import com.google.common.base.Optional;

/**
 * Represents a source which {@link DataContainer}s may be written to/read from.
 */
public interface DataSource
{

    /**
     * Writes the given object which implements {@link DataSerializable} to this
     * source as if by passing {@link DataSerializable#toContainer()} into
     * {@link #write(DataContainer)} .
     * 
     * @param serial The object
     * @throws IOException If there is a problem writing to the source
     */
    void write(DataSerializable serial) throws IOException;

    /**
     * Writes the given {@link DataContainer} to this source.
     * 
     * @param container The container to write
     * @throws IOException If there is a problem writing to the source
     */
    void write(DataContainer container) throws IOException;

    /**
     * Reads this source entirely into a DataContainer. This will attempt to
     * read the entire source into a single {@link DataContainer}.
     * 
     * @return The new data container
     * @throws IOException If there is a problem writing to the source
     */
    DataContainer read() throws IOException;

    /**
     * Reads this source into a {@link DataContainer} and loads it into the
     * given object by calling
     * {@link DataSerializable#fromContainer(DataContainer)}.
     * 
     * @param object The object to load the data into
     * @return The same object
     * @throws IOException If there is a problem writing to the source
     */
    <T extends DataSerializable> T read(T object) throws IOException;

    /**
     * Gets the name of this data source, if available.
     * 
     * @return The name
     */
    Optional<String> getName();

}
