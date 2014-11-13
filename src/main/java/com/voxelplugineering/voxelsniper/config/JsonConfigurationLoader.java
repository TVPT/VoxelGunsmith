package com.voxelplugineering.voxelsniper.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.voxelplugineering.voxelsniper.api.IConfiguration;

public class JsonConfigurationLoader
{
    
    public JsonConfigurationLoader()
    {
        
    }
    
    public void load(File jsonFile, IConfiguration configuration, String containerName) throws IllegalArgumentException, IllegalAccessException, IOException
    {
        Class<?> container = configuration.getContainer(containerName);
        Reader reader = new InputStreamReader(new FileInputStream(jsonFile));
        Gson gson = new GsonBuilder().create();
        Object obj = gson.fromJson(reader, container);
        reader.close();
        for(Field f: container.getFields())
        {
            String name = f.getName();
            Object value = f.get(obj);
            if(value != null)
            {
                configuration.set(name, value);
            }
        }
    }
    
    public void save(File output, IConfiguration configuration, String containerName) throws IOException, InstantiationException, IllegalAccessException
    {
        Class<?> container = configuration.getContainer(containerName);
        Gson gson = new GsonBuilder().create();
        Writer writer = new FileWriter(output);
        Object obj = container.newInstance();
        for(Field f: container.getFields())
        {
            String name = f.getName();
            Object value = configuration.get(name);
            f.set(obj, value);
        }
        gson.toJson(obj, writer);
        writer.close();
    }
    
    public void saveAllContainers(File outputFolder, IConfiguration configuration) throws IOException, InstantiationException, IllegalAccessException
    {
        Class<?>[] containers = configuration.getContainers();
        
        Gson gson = new GsonBuilder().create();
        for(Class<?> container: containers)
        {
            Writer writer = new FileWriter(new File(outputFolder, container.getName() + ".json"));
            Object obj = container.newInstance();
            for(Field f: container.getFields())
            {
                String name = f.getName();
                Object value = configuration.get(name);
                f.set(obj, value);
            }
            gson.toJson(obj, writer);
            writer.close();
        }
    }
}
