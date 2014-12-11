package com.voxelplugineering.voxelsniper.common.alias;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;

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

}
