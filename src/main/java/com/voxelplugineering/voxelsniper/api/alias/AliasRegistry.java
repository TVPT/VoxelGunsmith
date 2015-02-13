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
package com.voxelplugineering.voxelsniper.api.alias;

import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;

/**
 * Represents a registry for aliases.
 */
public interface AliasRegistry extends DataSerializable
{

    /**
     * Returns the alias value for the given alias key.
     * 
     * @param alias the key
     * @return the value
     */
    Optional<String> getAlias(String alias);

    /**
     * Returns all keys of this collection. Including, if the deep flag is set,
     * all keys of its parent registries as well.
     * 
     * @param deep whether to include keys from parent registries as well
     * @return the keys
     */
    Collection<? extends String> getKeys(boolean deep);

    /**
     * Registers a new alias with this registry.
     * 
     * @param alias the new alias, cannot be null or empty
     * @param value the new value for this alias, cannot be null or empty
     */
    void register(String alias, String value);

    /**
     * Returns a Set of all entries in this registry. Intended for use in
     * serialization only, nor for fetching aliases, use
     * {@link #getAlias(String)} instead.
     * 
     * @return the set of entries
     */
    Set<Entry<String, String>> getEntries();

    /**
     * Clears all aliases in this registry.
     */
    void clear();

    /**
     * Recursively expands all aliases found within the given string.
     * 
     * @param string the string to expand
     * @return the result expansion
     */
    String expand(String string);

    /**
     * Returns the parent registry
     * 
     * @return the parent, may be null
     */
    AliasRegistry getParent();

}
