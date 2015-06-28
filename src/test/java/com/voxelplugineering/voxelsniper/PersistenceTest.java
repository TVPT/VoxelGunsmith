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
package com.voxelplugineering.voxelsniper;

import java.io.IOException;

import org.junit.Test;

import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceFactory;
import com.voxelplugineering.voxelsniper.core.service.DataSourceFactoryService;
import com.voxelplugineering.voxelsniper.core.service.persistence.FileDataSource;
import com.voxelplugineering.voxelsniper.core.service.persistence.JsonDataSourceReader;
import com.voxelplugineering.voxelsniper.core.service.persistence.MemoryContainer;
import com.voxelplugineering.voxelsniper.core.service.persistence.StandardOutDataSource;
import com.voxelplugineering.voxelsniper.util.ContextTestUtil;

/**
 * A test for various aspects of the persistence service.
 */
public class PersistenceTest
{

    /**
     * @throws IOException On error
     */
    @Test
    public void test() throws IOException
    {
        StandardOutDataSource source = new StandardOutDataSource();
        JsonDataSourceReader reader = new JsonDataSourceReader(source);

        DataContainer data = new MemoryContainer("");
        data.writeString("hello", "world");

        reader.write(data);
    }

    /**
     * @throws IOException On error
     */
    @Test
    public void testFactory() throws IOException
    {
        DataSourceFactory factory = new DataSourceFactoryService(ContextTestUtil.create());
        factory.start();
        factory.register("stdout", StandardOutDataSource.class, StandardOutDataSource.BUILDER);
        factory.register("file", FileDataSource.class, FileDataSource.BUILDER);
        factory.register("json", JsonDataSourceReader.class, JsonDataSourceReader.getBuilder(factory));

        DataContainer args = new MemoryContainer("");
        args.writeString("source", "stdout");
        args.writeContainer("sourceArgs", new MemoryContainer(""));

        JsonDataSourceReader output = (JsonDataSourceReader) factory.build("json", args).get();

        DataContainer data = new MemoryContainer("");
        data.writeString("hello", "world");

        output.write(data);
    }

}
