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

import java.io.File;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;

/**
 * An abstract data source based on a {@link File}.
 */
public abstract class FileDataSource implements DataSource
{

    protected final File file;

    /**
     * Sets up this {@link FileDataSource}
     * 
     * @param file The file
     */
    public FileDataSource(File file)
    {
        this.file = file;
    }

    /**
     * Gets the file that this data source is based in.
     * 
     * @return The file
     */
    public File getFile()
    {
        return this.file;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getName()
    {
        return Optional.of(this.file.getName());
    }

    /**
     * A builder for {@link DataSource}s from a file.
     */
    public static interface Builder
    {

        /**
         * Creates a new {@link DataSource} for the given file.
         * 
         * @param file The file
         * @return The {@link DataSource}
         */
        DataSource build(File file);
    }

}
