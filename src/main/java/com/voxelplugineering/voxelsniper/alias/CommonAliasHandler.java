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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.alias.AliasOwner;
import com.voxelplugineering.voxelsniper.api.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.service.persistence.MemoryContainer;

/**
 * A standard implementation for {@link AliasHandler}.
 */
public class CommonAliasHandler implements AliasHandler
{

    private AliasHandler parent;
    private Map<String, AliasRegistry> aliasTargets;
    private AliasOwner owner;
    private boolean built;

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
     * @param parent The new parent
     * @param owner The owner
     */
    public CommonAliasHandler(AliasOwner owner, AliasHandler parent)
    {
        checkNotNull(owner);
        this.parent = parent;
        this.owner = owner;
        this.built = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists()
    {
        return this.built;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init()
    {
        this.aliasTargets = Maps.newHashMap();
        if (this.parent != null)
        {
            for (String target : this.parent.getValidTargets())
            {
                registerTarget(target);
            }
        }
        this.built = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy()
    {
        this.aliasTargets = null;
        this.built = false;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    public Set<String> getValidTargets()
    {
        return Collections.unmodifiableSet(this.aliasTargets.keySet());
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasTarget(String target)
    {
        checkNotNull(target);
        checkArgument(!target.isEmpty());
        return this.aliasTargets.containsKey(target);
    }

    /**
     * {@inheritDoc}
     */
    public AliasOwner getOwner()
    {
        return this.owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fromContainer(DataContainer container)
    {
        for (String key : container.keySet())
        {
            Optional<DataContainer> regContainer = container.readContainer(key);
            if (regContainer.isPresent())
            {
                AliasRegistry registry = this.registerTarget(key);
                registry.fromContainer(regContainer.get());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataContainer toContainer()
    {
        MemoryContainer container = new MemoryContainer("aliases");
        for (String s : this.aliasTargets.keySet())
        {
            container.writeContainer(s, this.aliasTargets.get(s).toContainer());
        }
        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeTarget(String target)
    {
        return this.aliasTargets.remove(target) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearTargets()
    {
        this.aliasTargets.clear();
    }

}
