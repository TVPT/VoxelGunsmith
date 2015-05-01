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
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceBuilder;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.core.Gunsmith;

/**
 * A data source which serializes data to a file as Json.
 */
public class JsonDataSourceReader implements DataSourceReader
{

    /**
     * A {@link DataSourceBuilder} for json data sources.
     */
    public static final DataSourceBuilder<JsonDataSourceReader> BUILDER = new DataSourceBuilder<JsonDataSourceReader>()
    {

        @Override
        public Optional<JsonDataSourceReader> build(DataContainer args)
        {
            if (!args.containsKey("source") || !args.containsKey("sourceArgs"))
            {
                Gunsmith.getLogger().warn("Failed to build JsonDataSourceReader, invalid args");
                return Optional.absent();
            }
            String sourceName = args.readString("source").get();
            Optional<DataSource> source = Gunsmith.getPersistence().build(sourceName, args.readContainer("sourceArgs").get());
            if (!source.isPresent())
            {
                Gunsmith.getLogger().warn("Failed to build data source for JsonDataSourceReader");
                return Optional.absent();
            }
            if (!StreamDataSource.class.isAssignableFrom(source.get().getClass()))
            {
                Gunsmith.getLogger().warn("Failed to build JsonDataSourceReader: Source was not a StreamDataSource");
                return Optional.absent();
            }
            StreamDataSource stream = (StreamDataSource) source.get();
            return Optional.of(new JsonDataSourceReader(stream));
        }

    };

    private final StreamDataSource source;
    private boolean pretty = false;

    /**
     * Creates a new {@link JsonDataSourceReader} based on the given stream source.
     * 
     * @param source The source
     */
    public JsonDataSourceReader(StreamDataSource source)
    {
        this.source = source;
    }

    /**
     * Sets whether this data source reader should output pretty json.
     * 
     * @param pretty Pretty json
     */
    public void setPrettyOutput(boolean pretty)
    {
        this.pretty = pretty;
    }

    @Override
    public void write(DataContainer container) throws IOException
    {
        Gson gson;
        if (this.pretty)
        {
            gson = new GsonBuilder().setPrettyPrinting().create();
        } else
        {
            gson = new GsonBuilder().create();
        }
        JsonObject json = fromContainer(container);

        String data = gson.toJson(json);
        this.source.write(data.getBytes("UTF-8"));
    }

    /**
     * Recursively converts the given {@link DataContainer} to a {@link JsonObject}.
     * 
     * @param container The container to convert
     * @return The json object representing the container
     */
    public JsonObject fromContainer(DataContainer container)
    {
        JsonObject json = new JsonObject();

        for (Map.Entry<String, Object> entry : container.entrySet())
        {
            Class<?> type = entry.getValue().getClass();
            if (type == Byte.class || type == byte.class || type == Short.class || type == short.class || type == Integer.class || type == int.class
                    || type == Long.class || type == long.class || type == Float.class || type == float.class || type == Double.class
                    || type == double.class)
            {
                json.addProperty(entry.getKey(), (Number) entry.getValue());
            } else if (type == Boolean.class || type == boolean.class)
            {
                json.addProperty(entry.getKey(), (Boolean) entry.getValue());
            } else if (type == Character.class || type == char.class)
            {
                json.addProperty(entry.getKey(), (Character) entry.getValue());
            } else if (type == String.class)
            {
                json.addProperty(entry.getKey(), (String) entry.getValue());
            } else if (DataContainer.class.isAssignableFrom(type))
            {
                json.add(entry.getKey(), fromContainer((DataContainer) entry.getValue()));
            } else if (DataSerializable.class.isAssignableFrom(type))
            {
                json.add(entry.getKey(), fromContainer(((DataSerializable) entry.getValue()).toContainer()));
            }
        }
        return json;
    }

    @Override
    public DataContainer read() throws IOException
    {
        DataContainer container = null;
        String data = new String(this.source.read(), "UTF-8");
        JsonParser parser = new JsonParser();
        JsonElement rootelement = parser.parse(data);
        container = toContainer(rootelement);
        return container;
    }

    /**
     * Recursively converts the given {@link JsonElement} to a {@link DataContainer}.
     * 
     * @param rootelement The element to convert
     * @return The new {@link DataContainer}
     */
    public DataContainer toContainer(JsonElement rootelement)
    {
        DataContainer container = new MemoryContainer("");

        if (rootelement.isJsonObject())
        {
            JsonObject root = rootelement.getAsJsonObject();
            for (Entry<String, JsonElement> entry : root.entrySet())
            {
                String key = entry.getKey();
                JsonElement element = entry.getValue();
                if (element.isJsonPrimitive())
                {
                    JsonPrimitive primitive = element.getAsJsonPrimitive();
                    if (primitive.isBoolean())
                    {
                        container.writeBoolean(key, primitive.getAsBoolean());
                    } else if (primitive.isString())
                    {
                        container.writeString(key, primitive.getAsString());
                    } else if (primitive.isNumber())
                    {
                        container.writeNumber(key, primitive.getAsNumber());
                    }
                } else if (element.isJsonObject())
                {
                    container.writeContainer(key, toContainer(element));
                }
            }
        }

        return container;

    }

    @Override
    public void write(DataSerializable serial) throws IOException
    {
        write(serial.toContainer());
    }

    @Override
    public <T extends DataSerializable> T read(T object) throws IOException
    {
        object.fromContainer(read());
        return object;
    }

    @Override
    public Optional<String> getName()
    {
        return this.source.getName();
    }

    @Override
    public boolean exists()
    {
        return this.source.exists();
    }

}
