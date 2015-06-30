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

import java.io.IOException;

import com.google.common.base.Optional;

/**
 * A {@link StreamDataSource} which ouputs data to {@link System#out} as a UFT-8 encoded String. <p>
 * {@link #read()} is no supported. </p>
 */
public class StandardOutDataSource extends StreamDataSource
{

    /**
     * A builder for {@link StandardOutDataSource}s.
     */
    public static final DataSourceBuilder<StandardOutDataSource> BUILDER = new DataSourceBuilder<StandardOutDataSource>()
    {

        @Override
        public Optional<StandardOutDataSource> build(DataContainer args)
        {
            return Optional.of(new StandardOutDataSource());
        }
    };

    @Override
    public Optional<String> getName()
    {
        return Optional.of("System.out");
    }

    @Override
    public byte[] read() throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(byte[] data) throws IOException
    {
        String s = new String(data, "UTF-8");
        System.out.print(s);
        System.out.println();
    }

    @Override
    public boolean exists()
    {
        return true;
    }

}
