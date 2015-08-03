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

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;
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
        this.owner = checkNotNull(owner);
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
            AliasRegistry parentRegistry = this.parent == null ? null : this.parent.getRegistry(target).orNull();
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
        return Optional.absent();
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
    public AliasOwner getOwner()
    {
        return this.owner;
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

}
