package com.voxelplugineering.voxelsniper.common.alias;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.voxelplugineering.voxelsniper.Gunsmith;

public class AliasHandler
{
    
    private AliasHandler parent;
    private Map<String, AliasRegistry> aliasTargets;
    
    public AliasHandler()
    {
        this(null);
    }
    
    public AliasHandler(AliasHandler parent)
    {
        this.parent = parent;
        this.aliasTargets = new HashMap<String, AliasRegistry>();
        if(parent != null)
        {
            for(String target: this.parent.getValidTargets())
            {
                registerTarget(target);
            }
        }
    }
    
    public void registerTarget(String target)
    {
        if(!this.aliasTargets.containsKey(target))
        {
            AliasRegistry parentRegistry = this.parent == null? null: this.parent.getRegistry(target).orNull();
            this.aliasTargets.put(target, new AliasRegistry(parentRegistry));
        }
    }
    
    public Optional<AliasRegistry> getRegistry(String target)
    {
        if(this.aliasTargets.containsKey(target))
        {
            return Optional.of(this.aliasTargets.get(target));
        }
        return Optional.absent();
    }

    public Set<String> getValidTargets()
    {
        return Collections.unmodifiableSet(this.aliasTargets.keySet());
    }

    public boolean hasTarget(String target)
    {
        return this.aliasTargets.containsKey(target);
    }
    
    public void load(File dataFile) throws IOException
    {
        if(!dataFile.exists())
        {
            return;
        }
        
        JsonReader reader = null;
        
        try
        {
            reader = new JsonReader(new FileReader(dataFile));
            
            reader.beginObject();
            
            while(reader.hasNext())
            {
                String target = reader.nextName();
                registerTarget(target);
                AliasRegistry targetReg = getRegistry(target).get();
                reader.beginObject();
                while(reader.hasNext())
                {
                    String key = reader.nextName();
                    String value = reader.nextString();
                    targetReg.register(key, value);
                }
                reader.endObject();
            }
            
            reader.endObject();
        }
        catch(IOException e)
        {
            Gunsmith.getLogger().error(e, "Error saving aliases");
        }
        finally
        {
            if(reader != null)
            {
                reader.close();
            }
        }
        
    }
    
    public void save(File dataFile) throws IOException
    {
        if(dataFile.exists())
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
            for(String target: this.aliasTargets.keySet())
            {
                JsonObject array = new JsonObject();
                for(Entry<String, String> e: this.aliasTargets.get(target).getEntries())
                {
                    array.add(e.getKey(), new JsonPrimitive(e.getValue()));
                }
                json.add(target, array);
            }
            gson.toJson(json, writer);
        }
        catch(IOException e)
        {
            Gunsmith.getLogger().error(e, "Error saving aliases");
        }
        finally
        {
            if(writer != null)
            {
                writer.close();
            }
        }
        
    }

}
