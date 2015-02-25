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

import java.util.Set;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.alias.AliasOwner;
import com.voxelplugineering.voxelsniper.api.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.api.service.AbstractService;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;

/**
 * A service which wraps an {@link AliasHandler}.
 */
public class AliasHandlerService extends AbstractService implements AliasHandler
{

    private final AliasHandler wrapped;

    /**
     * Creates a new {@link AliasHandlerService}.
     * 
     * @param wrapped The handler to wrap
     */
    public AliasHandlerService(AliasHandler wrapped)
    {
        super(3);
        this.wrapped = wrapped;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "aliasRegistry";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init()
    {
        this.wrapped.init();
        Gunsmith.getLogger().info("Initialized AliasRegistry service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy()
    {
        this.wrapped.destroy();
        Gunsmith.getLogger().info("Stopping AliasRegistry service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fromContainer(DataContainer container)
    {
        check();
        this.wrapped.fromContainer(container);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataContainer toContainer()
    {
        check();
        return this.wrapped.toContainer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists()
    {
        check();
        return this.wrapped.exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getValidTargets()
    {
        check();
        return this.wrapped.getValidTargets();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AliasOwner getOwner()
    {
        check();
        return this.wrapped.getOwner();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AliasRegistry> getRegistry(String target)
    {
        check();
        return this.wrapped.getRegistry(target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasTarget(String target)
    {
        check();
        return this.wrapped.hasTarget(target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AliasRegistry registerTarget(String string)
    {
        check();
        return this.wrapped.registerTarget(string);
    }

}
