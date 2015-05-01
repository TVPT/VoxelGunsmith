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

import java.io.IOException;

import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;

/**
 * A {@link DataSource} which is backed by a stream based interface.
 */
public abstract class StreamDataSource implements DataSource
{

    /**
     * Reads from this {@link DataSource} completely and returns the result of the read as a byte
     * array.
     * 
     * @return The data
     * @throws IOException If an error occurred while reading
     */
    public abstract byte[] read() throws IOException;

    /**
     * Writes the given byte array of data to this {@link DataSource}.
     * 
     * @param data The data
     * @throws IOException If an error occurred while writing
     */
    public abstract void write(byte[] data) throws IOException;

}
