package com.voxelplugineering.voxelsniper.service.persistence;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;

/**
 * A data source which serializes data to a file as Json.
 */
public class JsonDataSource extends FileDataSource
{

    /**
     * Creates a new {@link JsonDataSource} based on the given file.
     * 
     * @param file The file
     */
    public JsonDataSource(File file)
    {
        super(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(DataContainer container) throws IOException
    {
        if (this.file.exists())
        {
            this.file.delete();
        }
        Writer writer = null;
        try
        {
            this.file.createNewFile();

            writer = new FileWriter(this.file);

            Gson gson = new GsonBuilder().create();
            JsonObject json = fromContainer(container);

            gson.toJson(json, writer);
        } finally
        {
            if (writer != null)
            {
                writer.close();
            }
        }
    }

    /**
     * Recursively converts the given {@link DataContainer} to a
     * {@link JsonObject}.
     * 
     * @param container The container to convert
     * @return The json object representing the container
     */
    public JsonObject fromContainer(DataContainer container)
    {
        JsonObject json = new JsonObject();

        for (Map.Entry<String, Object> entry : container.extrySet())
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

    /**
     * {@inheritDoc}
     */
    @Override
    public DataContainer read() throws IOException
    {
        JsonReader reader = null;
        DataContainer container = null;
        try
        {
            reader = new JsonReader(new FileReader(this.file));
            JsonParser parser = new JsonParser();
            JsonElement rootelement = parser.parse(reader);
            container = toContainer(rootelement);

        } finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }
        return container;
    }

    /**
     * Recursively converts the given {@link JsonElement} to a
     * {@link DataContainer}.
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

}
