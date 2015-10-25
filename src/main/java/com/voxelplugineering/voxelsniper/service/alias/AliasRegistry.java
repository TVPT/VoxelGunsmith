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

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a registry for aliases.
 */
public interface AliasRegistry
{

    /**
     * Gets the name of this registry.
     * 
     * @return The registry name
     */
    String getRegistryName();

    /**
     * Recursively expands all aliases found within the given string.
     * 
     * @param string The string to expand
     * @return The result expansion
     */
    String expand(String string);

    /**
     * Gets the alias value for the given alias key.
     * 
     * @param alias The key
     * @return The value
     */
    Optional<String> getAlias(String alias);

    /**
     * Registers a new alias with this registry.
     * 
     * @param alias The new alias, cannot be null or empty
     * @param value The new value for this alias, cannot be null or empty
     */
    void register(String alias, String value);

    /**
     * Removes the given alias from the registry.
     * 
     * @param alias The alias to remove
     * @return Whether the registry contained the alias
     */
    boolean remove(String alias);

    /**
     * Gets all keys of this collection. Including, if the deep flag is set, all keys of its parent
     * registries as well.
     * 
     * @param deep Whether to include keys from parent registries as well
     * @return The keys
     */
    Collection<? extends String> getKeys(boolean deep);

    /**
     * Gets a Set of all entries in this registry. Intended for use in serialization only, nor for
     * fetching aliases, use {@link #getAlias(String)} instead.
     * 
     * @return The set of entries
     */
    Set<Entry<String, String>> getEntries();

    /**
     * Clears all aliases in this registry.
     */
    void clear();

    /**
     * Gets the parent registry.
     * 
     * @return The parent, may be null
     */
    AliasRegistry getParent();

}
