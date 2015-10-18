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
package com.voxelplugineering.voxelsniper.service.alias;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A standard implementation for {@link AliasHandler}.
 */
public class CommonAliasHandler implements AliasHandler
{

    private AliasHandler parent;
    private Map<String, AliasRegistry> aliasTargets;
    private AliasOwner owner;

    /**
     * Creates a new {@link AliasHandler}.
     * 
     * @param owner The owner
     */
    public CommonAliasHandler(AliasOwner owner)
    {
        this(owner, null);
    }

    /**
     * Sets the parent alias handler.
     * 
     * @param owner The owner
     * @param parent The new parent
     */
    public CommonAliasHandler(AliasOwner owner, AliasHandler parent)
    {
        this.owner = owner;
        this.parent = parent;
        this.aliasTargets = Maps.newHashMap();
        if (this.parent != null)
        {
            for (String target : this.parent.getValidTargets())
            {
                registerTarget(target);
            }
        }
    }

    @Override
    public AliasRegistry registerTarget(String target)
    {
        checkNotNull(target);
        checkArgument(!target.isEmpty());
        if (!this.aliasTargets.containsKey(target))
        {
            AliasRegistry parentRegistry = this.parent == null ? null : this.parent.getRegistry(target).orElse(null);
            this.aliasTargets.put(target, new CommonAliasRegistry(target, parentRegistry));
        }
        return this.aliasTargets.get(target);
    }

    @Override
    public Optional<AliasRegistry> getRegistry(String target)
    {
        checkNotNull(target);
        checkArgument(!target.isEmpty());
        if (this.aliasTargets.containsKey(target))
        {
            return Optional.of(this.aliasTargets.get(target));
        }
        return Optional.empty();
    }

    @Override
    public Set<String> getValidTargets()
    {
        return Collections.unmodifiableSet(this.aliasTargets.keySet());
    }

    @Override
    public boolean hasTarget(String target)
    {
        checkNotNull(target);
        checkArgument(!target.isEmpty());
        return this.aliasTargets.containsKey(target);
    }

    @Override
    public Optional<AliasOwner> getOwner()
    {
        return Optional.ofNullable(this.owner);
    }

    @Override
    public boolean removeTarget(String target)
    {
        return this.aliasTargets.remove(target) != null;
    }

    @Override
    public void clearTargets()
    {
        this.aliasTargets.clear();
    }

    @Override
    public void save() throws IOException
    {
        File dataFile = this.owner.getAliasSource();
        if (dataFile.exists())
        {
            dataFile.delete();
        }
        Writer writer = null;
        try
        {

            dataFile.createNewFile();

            writer = new FileWriter(dataFile);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject json = new JsonObject();
            for (String target : this.aliasTargets.keySet())
            {
                JsonObject array = new JsonObject();
                for (Map.Entry<String, String> e : this.aliasTargets.get(target).getEntries())
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

    @Override
    public void load() throws IOException
    {
        File dataFile = this.owner.getAliasSource();
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

}
