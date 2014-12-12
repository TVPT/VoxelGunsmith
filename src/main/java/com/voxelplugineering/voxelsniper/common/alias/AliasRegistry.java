package com.voxelplugineering.voxelsniper.common.alias;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.util.Utilities;

/**
 * A registry for aliases.  
 */
public class AliasRegistry
{

    private Map<String, String> aliases;
    private AliasRegistry parent;

    /**
     * Creates a new {@link AliasRegistry} with no parent.
     */
    public AliasRegistry()
    {
        this(null);
    }

    /**
     * Creates a new {@link AliasRegistry} with the given registry as its parent.
     * 
     * @param parent the parent registry
     */
    public AliasRegistry(AliasRegistry parent)
    {
        this.aliases = new TreeMap<String, String>(new Comparator<String>()
        {

            @Override
            public int compare(String o1, String o2)
            {
                int l = o1.length() - o2.length();
                if (l == 0)
                {
                    return o1.compareTo(o2);
                }
                return l;
            }
        });
        this.parent = parent;
    }

    /**
     * Returns the parent registry
     * 
     * @return the parent, may be null
     */
    public AliasRegistry getParent()
    {
        return this.parent;
    }

    /**
     * Registers a new alias with this registry.
     * 
     * @param alias the new alias, cannot be null or empty
     * @param value the new value for this alias, cannot be null or empty
     */
    public void register(String alias, String value)
    {
        checkNotNull(alias, "Alias cannot be null.");
        checkNotNull(value, "Value cannot be null.");
        checkArgument(!alias.isEmpty(), "Alias cannot be empty.");
        checkArgument(!value.isEmpty(), "Value cannot be empty.");

        this.aliases.put(alias, value);
    }

    /**
     * Clears all aliases in this registry.
     */
    public void clear()
    {
        this.aliases.clear();
    }
    
    /**
     * Returns the alias value for the given alias key.
     * 
     * @param alias the key
     * @return the value
     */
    public Optional<String> getAlias(String alias)
    {
        if(!this.aliases.containsKey(alias))
        {
            if(this.parent != null)
            {
                return this.parent.getAlias(alias);
            }
            return Optional.absent();
        }
        else
        {
            return Optional.of(this.aliases.get(alias));
        }
    }
    
    /**
     * Returns all keys of this collection. Including, if the deep flag is set, all keys of its parent registries as well.
     * 
     * @param deep whether to include keys from parent registries as well
     * @return the keys
     */
    public Collection<String> getKeys(boolean deep)
    {
        Set<String> keys = new HashSet<String>();
        keys.addAll(this.aliases.keySet());
        if(this.parent != null && deep)
        {
            keys.addAll(this.parent.getKeys(true));
        }
        return keys;
    }

    /**
     * Recursively expands all aliases found within the given string.
     * 
     * @param string the string to expand
     * @return the result expansion
     */
    public String expand(String string)
    {
        String[] split = string.split(" ");
        int i = 0;
        List<String> alreadyUsedAliases = new ArrayList<String>();
        while (i < split.length)
        {
            boolean found = false;
            outer: for (int j = i; j < split.length; j++)
            {
                String section = Utilities.getSection(split, i, j);
                for (String alias : this.getKeys(true))
                {
                    if (alias.equalsIgnoreCase(section) && !alreadyUsedAliases.contains(alias))
                    {
                        alreadyUsedAliases.add(alias);
                        found = true;
                        split = Utilities.replaceSection(split, this.getAlias(alias).get().split(" "), i, j);
                        break outer;
                    }
                }
            }
            if (!found)
            {
                i++;
                alreadyUsedAliases.clear();
            }
        }
        return Utilities.getSection(split, 0, split.length - 1);
    }
    
    public Set<Entry<String, String>> getEntries()
    {
        return this.aliases.entrySet();
    }

}
