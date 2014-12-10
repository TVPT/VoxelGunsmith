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
package com.voxelplugineering.voxelsniper.config;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.voxelplugineering.voxelsniper.api.IConfiguration;

/**
 * A configuration loader for loading and saving configuration containers to json flat files.
 */
public class JsonConfigurationLoader
{

    /**
     * Loads a configuration container from the given jsonFile. The configuration container must be registered with the configuration storage before
     * calling this.
     * 
     * @param jsonFile the jsonFile to load the container from, cannot be null
     * @param configuration the configuration object to load the values into, cannot be null
     * @param containerName the name of the container, cannot be null or empty
     * @throws IOException if there is an issue with loading values from the file
     * @throws IllegalAccessException if there was an issue loading the values from the fields
     */
    public static void load(File jsonFile, IConfiguration configuration, String containerName) throws IOException, IllegalAccessException
    {
        checkNotNull(jsonFile, "Json File cannot be null!");
        checkNotNull(configuration, "Configuration cannot be null!");
        checkNotNull(containerName, "Name cannot be null!");
        checkArgument(!containerName.isEmpty(), "Name cannot be empty");
        Object container = configuration.getContainer(containerName);
        Reader reader = new InputStreamReader(new FileInputStream(jsonFile));
        Gson gson = new GsonBuilder().create();
        Object obj = gson.fromJson(reader, container.getClass());
        reader.close();
        for (Field f : container.getClass().getFields())
        {
            String name = f.getName();
            Object value = f.get(obj);
            if (value != null)
            {
                configuration.set(name, value);
            }
        }
    }

    /**
     * Saves the values of a configuration container from the configuration object to the given file as a json object. The container just have been
     * registered to the configuration object prior to calling this.
     * 
     * @param output the file to output to
     * @param configuration the configuration to save the values from
     * @param containerName the name of the container to save the values from
     * @throws IOException if there is a problem saving to the file
     * @throws InstantiationException if there was a problem creating the writer to save the file
     * @throws IllegalAccessException if there was a problem getting the data from the container
     */
    public static void save(File output, IConfiguration configuration, String containerName) throws IOException, InstantiationException,
            IllegalAccessException
    {
        checkNotNull(output, "File output cannot be null!");
        checkNotNull(configuration, "Configuration cannot be null!");
        checkNotNull(containerName, "Name cannot be null!");
        checkArgument(!containerName.isEmpty(), "Name cannot be empty");
        Object container = configuration.getContainer(containerName);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = new FileWriter(output);
        for (Field f : container.getClass().getFields())
        {
            String name = f.getName();
            Object value = configuration.get(name).get();
            f.set(container, value);
        }
        gson.toJson(container, writer);
        writer.close();
    }

    /**
     * Saves all containers registered with the configuration object to files. The files are named according to the container names (with a .json file
     * extension). The files are stored in the directory specified.
     * 
     * @param outputFolder the directory to store the files to
     * @param configuration the configuration object to save the values from
     * @throws IOException if there is an issue saving the files.
     * @throws InstantiationException if there was a problem creating the writer to save the file
     * @throws IllegalAccessException if there was a problem getting the data from the container
     */
    public static void saveAllContainers(File outputFolder, IConfiguration configuration) throws IOException, InstantiationException,
            IllegalAccessException
    {
        checkNotNull(outputFolder, "Output folder cannot be null!");
        checkNotNull(configuration, "Configuration cannot be null!");
        Object[] containers = configuration.getContainers();

        Gson gson = new GsonBuilder().create();
        for (Object obj : containers)
        {
            Class<?> container = obj.getClass();
            Writer writer = new FileWriter(new File(outputFolder, container.getName() + ".json"));
            for (Field f : container.getFields())
            {
                String name = f.getName();
                Object value = configuration.get(name).get();
                f.set(obj, value);
            }
            gson.toJson(obj, writer);
            writer.close();
        }
    }
}
