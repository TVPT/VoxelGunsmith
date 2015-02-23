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
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;
import com.voxelplugineering.voxelsniper.service.persistence.JsonDataSource;
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
        container.writeBoolean("a", true);
        container.writeByte("b", (byte) 0);
        container.writeShort("c", (short) 0);
        container.writeInt("d", 0);
        container.writeLong("e", (long) 0);
        container.writeFloat("f", (float) 0);
        container.writeDouble("g", (double) 0);
        container.writeChar("h", 'a');
        container.writeString("i", "HelloWorld");
        container.writeCustom("j", test);
        container.writeContainer("k", test.toContainer());

        assertEquals(Boolean.valueOf(true), container.readBoolean("a").get());
        assertEquals(Byte.valueOf((byte) 0), container.readByte("b").get());
        assertEquals(Short.valueOf((short) 0), container.readShort("c").get());
        assertEquals(Integer.valueOf(0), container.readInt("d").get());
        assertEquals(Long.valueOf((long) 0), container.readLong("e").get());
        assertEquals(Float.valueOf((float) 0), container.readFloat("f").get());
        assertEquals(Double.valueOf((double) 0), container.readDouble("g").get());
        assertEquals(Character.valueOf('a'), container.readChar("h").get());
        assertEquals("HelloWorld", container.readString("i").get());
        assertEquals(test, container.readCustom("j", TestCustomData.class).get());
        test.fromContainer(container.readContainer("k").get());
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
        container.writeBoolean("a", true);
        container.writeByte("b", (byte) 0);
        container.writeShort("c", (short) 0);
        container.writeInt("d", 0);
        container.writeLong("e", (long) 0);
        container.writeFloat("f", (float) 0);
        container.writeDouble("g", (double) 0);
        container.writeChar("h", 'a');
        container.writeString("i", "HelloWorld");
        container.writeCustom("j", test);
        container.writeContainer("k", test.toContainer());

        JsonDataSource json = new JsonDataSource(null);
        JsonElement element = json.fromContainer(container);
        String data = element.toString();

        JsonParser parser = new JsonParser();
        JsonElement rootelement = parser.parse(data);
        DataContainer newContainer = json.toContainer(rootelement);

        assertEquals(Boolean.valueOf(true), newContainer.readBoolean("a").get());
        assertEquals(Byte.valueOf((byte) 0), newContainer.readByte("b").get());
        assertEquals(Short.valueOf((short) 0), newContainer.readShort("c").get());
        assertEquals(Integer.valueOf(0), newContainer.readInt("d").get());
        assertEquals(Long.valueOf((long) 0), newContainer.readLong("e").get());
        assertEquals(Float.valueOf((float) 0), newContainer.readFloat("f").get());
        assertEquals(Double.valueOf((double) 0), newContainer.readDouble("g").get());
        assertEquals(Character.valueOf('a'), newContainer.readChar("h").get());
        assertEquals("HelloWorld", newContainer.readString("i").get());
        assertEquals(test, newContainer.readCustom("j", TestCustomData.class).get());
        test.fromContainer(newContainer.readContainer("k").get());
        test.check();
    }

}

class TestCustomData implements DataSerializable
{

    public static TestCustomData buildFromContainer(DataContainer container)
    {
        TestCustomData test = new TestCustomData();
        test.test = container.readInt("test").get();
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
        this.test = container.readInt("test").get();
    }

    @Override
    public DataContainer toContainer()
    {
        MemoryContainer container = new MemoryContainer("");
        container.writeInt("test", this.test);
        return container;
    }

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
