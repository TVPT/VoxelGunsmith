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
package com.voxelplugineering.voxelsniper.alias;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

/**
 * A handler for targeted {@link AliasRegistry}s.
 */
public class AliasHandler
{

    private AliasHandler parent;
    private Map<String, AliasRegistry> aliasTargets;

    /**
     * Creates a new {@link AliasHandler}.
     */
    public AliasHandler()
    {
        this(null);
    }

    /**
     * Sets the parent alias handler.
     * 
     * @param parent the new parent
     */
    public AliasHandler(AliasHandler parent)
    {
        this.parent = parent;
        this.aliasTargets = Maps.newHashMap();
        if (parent != null)
        {
            for (String target : this.parent.getValidTargets())
            {
                registerTarget(target);
            }
        }
    }

    /**
     * Registers a new target.
     * 
     * @param target the new target name
     */
    public void registerTarget(String target)
    {
        if (!this.aliasTargets.containsKey(target))
        {
            AliasRegistry parentRegistry = this.parent == null ? null : this.parent.getRegistry(target).orNull();
            this.aliasTargets.put(target, new AliasRegistry(parentRegistry));
        }
    }

    /**
     * Returns a the {@link AliasRegistry} by the given name.
     * 
     * @param target the name
     * @return the registry for that name
     */
    public Optional<AliasRegistry> getRegistry(String target)
    {
        if (this.aliasTargets.containsKey(target))
        {
            return Optional.of(this.aliasTargets.get(target));
        }
        return Optional.absent();
    }

    /**
     * Returns a Set of all valid targets in this handler.
     * 
     * @return all valid targets
     */
    public Set<String> getValidTargets()
    {
        return Collections.unmodifiableSet(this.aliasTargets.keySet());
    }

    /**
     * Returns whether this handler has the given target.
     * 
     * @param target the target to check
     * @return whether the target exists
     */
    public boolean hasTarget(String target)
    {
        return this.aliasTargets.containsKey(target);
    }

    /**
     * Loads this {@link AliasHandler}s data from the given file. The file is assumed to be a json file.
     * <p>
     * TODO support better persistence options
     * 
     * @param dataFile the file to load from
     * @throws IOException if there is an issue loading the file
     */
    public void load(File dataFile) throws IOException
    {
        if (!dataFile.exists())
        {
            return;
        }

        JsonReader reader = null;

        try
        {
            reader = new JsonReader(new FileReader(dataFile));

            reader.beginObject();

            while (reader.hasNext())
            {
                String target = reader.nextName();
                registerTarget(target);
                AliasRegistry targetReg = getRegistry(target).get();
                reader.beginObject();
                while (reader.hasNext())
                {
                    String key = reader.nextName();
                    String value = reader.nextString();
                    targetReg.register(key, value);
                }
                reader.endObject();
            }

            reader.endObject();
        } finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }

    }

    /**
     * Saves this {@link AliasHandler}s data from to given file. The data is outputted as json.
     * <p>
     * TODO support better persistence options
     * 
     * @param dataFile the file to save to
     * @throws IOException if there is an issue saving the file
     */
    public void save(File dataFile) throws IOException
    {
        if (dataFile.exists())
        {
            dataFile.delete();
        }
        Writer writer = null;
        try
        {

            dataFile.createNewFile();

            writer = new FileWriter(dataFile);

            Gson gson = new GsonBuilder().create();
            JsonObject json = new JsonObject();
            for (String target : this.aliasTargets.keySet())
            {
                JsonObject array = new JsonObject();
                for (Entry<String, String> e : this.aliasTargets.get(target).getEntries())
                {
                    array.add(e.getKey(), new JsonPrimitive(e.getValue()));
                }
                json.add(target, array);
            }
            gson.toJson(json, writer);
        } finally
        {
            if (writer != null)
            {
                writer.close();
            }
        }

    }

}
