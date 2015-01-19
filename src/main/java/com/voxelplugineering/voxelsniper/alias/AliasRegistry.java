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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.voxelplugineering.voxelsniper.api.brushes.BrushParser;
import com.voxelplugineering.voxelsniper.api.brushes.BrushParser.BrushPart;
import com.voxelplugineering.voxelsniper.util.StringUtilities;

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
        this.aliases = Maps.newTreeMap(new Comparator<String>()
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
        if (!this.aliases.containsKey(alias))
        {
            if (this.parent != null)
            {
                return this.parent.getAlias(alias);
            }
            return Optional.absent();
        } else
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
        if (this.parent != null && deep)
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
        return expand_(string);
    }

    /**
     * Recursively expands all aliases found within the given string.
     * 
     * @param brush the brush to expand
     * @return the result expansion
     */
    public BrushPart[] expand(String brush, BrushParser parser)
    {
        if (parser.validate(brush))
        {
            return expand(parser.parse(brush).get(), parser);
        } else
        {
            return new BrushPart[0];
        }
    }

    /**
     * Recursively expands all aliases found within the given string.
     * 
     * @param brush the brush to expand
     * @return the result expansion
     */
    public BrushPart[] expand(BrushPart[] brush, BrushParser parser)
    {
        List<BrushPart> ret = Lists.newArrayList();
        Queue<BrushPart> working = Queues.newArrayDeque();
        for (BrushPart p : brush)
        {
            working.add(p);
        }

        while (!working.isEmpty())
        {
            BrushPart next = working.poll();
            Optional<BrushPart[]> p = parser.parse(expand_(next.getBrushName() + " {" + next.getBrushArgument() + "}"));
            if (!p.isPresent())
            {
                continue;
            }
            for (BrushPart n : p.get())
            {
                ret.add(n);
            }
        }

        return ret.toArray(new BrushPart[ret.size()]);
    }

    private String expand_(String string)
    {
        String[] split = string.split(" ");
        int i = 0;
        List<String> alreadyUsedAliases = new ArrayList<String>();
        boolean inside = false;
        while (i < split.length)
        {
            boolean found = false;
            outer: for (int j = i; j < split.length; j++)
            {
                String section = StringUtilities.getSection(split, i, j);
                if (section.contains("{") && section.contains("}"))
                {
                    found = false;
                    break outer;
                } else if (section.contains("{"))
                {
                    inside = true;
                    found = false;
                    break outer;
                } else if (section.contains("}") && inside)
                {
                    inside = false;
                    found = false;
                    break outer;
                } else if (inside)
                {
                    found = false;
                    break outer;
                }
                for (String alias : this.getKeys(true))
                {
                    if (alias.equalsIgnoreCase(section) && !alreadyUsedAliases.contains(alias))
                    {
                        alreadyUsedAliases.add(alias);
                        found = true;
                        split = StringUtilities.replaceSection(split, this.getAlias(alias).get().split(" "), i, j);
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
        return StringUtilities.getSection(split, 0, split.length - 1);
    }

    /**
     * Returns a Set of all entries in this registry. Intended for use in serialization only, nor for fetching aliases, use {@link #getAlias(String)}
     * instead.
     * 
     * @return the set of entries
     */
    public Set<Entry<String, String>> getEntries()
    {
        return Collections.unmodifiableSet(this.aliases.entrySet());
    }

}
