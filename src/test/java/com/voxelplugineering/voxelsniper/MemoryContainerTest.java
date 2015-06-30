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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.voxelplugineering.voxelsniper.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.service.persistence.DataSerializable;
import com.voxelplugineering.voxelsniper.service.persistence.JsonDataSourceReader;
import com.voxelplugineering.voxelsniper.service.persistence.MemoryContainer;

/**
 * A set of tests for the {@link MemoryContainer}.
 */
public class MemoryContainerTest
{

    /**
     * 
     */
    @Test
    public void testReadWrite()
    {
        TestCustomData test = new TestCustomData();
        MemoryContainer container = new MemoryContainer("");
        container.setBoolean("a", true);
        container.setByte("b", (byte) 0);
        container.setShort("c", (short) 0);
        container.setInt("d", 0);
        container.setLong("e", 0);
        container.setFloat("f", 0);
        container.setDouble("g", 0);
        container.setChar("h", 'a');
        container.setString("i", "HelloWorld");
        container.setCustom("j", test);
        container.setContainer("k", test.toContainer());

        assertEquals(Boolean.valueOf(true), container.getBoolean("a").get());
        assertEquals(Byte.valueOf((byte) 0), container.getByte("b").get());
        assertEquals(Short.valueOf((short) 0), container.getShort("c").get());
        assertEquals(Integer.valueOf(0), container.getInt("d").get());
        assertEquals(Long.valueOf(0), container.getLong("e").get());
        assertEquals(Float.valueOf(0), container.getFloat("f").get());
        assertEquals(Double.valueOf(0), container.getDouble("g").get());
        assertEquals(Character.valueOf('a'), container.getChar("h").get());
        assertEquals("HelloWorld", container.getString("i").get());
        assertEquals(test, container.getCustom("j", TestCustomData.class).get());
        test.fromContainer(container.getContainer("k").get());
        test.check();
    }

    /**
     * 
     */
    @Test
    public void testJson()
    {
        TestCustomData test = new TestCustomData();
        MemoryContainer container = new MemoryContainer("");
        container.setBoolean("a", true);
        container.setByte("b", (byte) 0);
        container.setShort("c", (short) 0);
        container.setInt("d", 0);
        container.setLong("e", 0);
        container.setFloat("f", 0);
        container.setDouble("g", 0);
        container.setChar("h", 'a');
        container.setString("i", "HelloWorld");
        container.setCustom("j", test);
        container.setContainer("k", test.toContainer());

        JsonDataSourceReader json = new JsonDataSourceReader(null);
        JsonElement element = json.fromContainer(container);
        String data = element.toString();

        JsonParser parser = new JsonParser();
        JsonElement rootelement = parser.parse(data);
        DataContainer newContainer = json.toContainer(rootelement);

        assertEquals(Boolean.valueOf(true), newContainer.getBoolean("a").get());
        assertEquals(Byte.valueOf((byte) 0), newContainer.getByte("b").get());
        assertEquals(Short.valueOf((short) 0), newContainer.getShort("c").get());
        assertEquals(Integer.valueOf(0), newContainer.getInt("d").get());
        assertEquals(Long.valueOf(0), newContainer.getLong("e").get());
        assertEquals(Float.valueOf(0), newContainer.getFloat("f").get());
        assertEquals(Double.valueOf(0), newContainer.getDouble("g").get());
        assertEquals(Character.valueOf('a'), newContainer.getChar("h").get());
        assertEquals("HelloWorld", newContainer.getString("i").get());
        assertEquals(test, newContainer.getCustom("j", TestCustomData.class).get());
        test.fromContainer(newContainer.getContainer("k").get());
        test.check();
    }

}

class TestCustomData implements DataSerializable
{

    public static TestCustomData buildFromContainer(DataContainer container)
    {
        TestCustomData test = new TestCustomData();
        test.test = container.getInt("test").get();
        return test;
    }

    int test = 5;

    public void check()
    {
        assertEquals(5, this.test);
    }

    @Override
    public void fromContainer(DataContainer container)
    {
        this.test = container.getInt("test").get();
    }

    @Override
    public DataContainer toContainer()
    {
        MemoryContainer container = new MemoryContainer("");
        container.setInt("test", this.test);
        return container;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof TestCustomData)
        {
            if (this.test == ((TestCustomData) other).test)
            {
                return true;
            }
        }
        return false;
    }

}
