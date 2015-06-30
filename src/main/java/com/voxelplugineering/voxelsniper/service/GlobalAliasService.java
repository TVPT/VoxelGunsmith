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
package com.voxelplugineering.voxelsniper.service;

import java.util.Set;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.service.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.service.alias.AliasOwner;
import com.voxelplugineering.voxelsniper.service.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.service.alias.GlobalAliasHandler;
import com.voxelplugineering.voxelsniper.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.util.Context;

/**
 * A service wrapping an {@link AliasHandler}.
 */
public class GlobalAliasService extends AbstractService implements GlobalAliasHandler
{

    private final AliasHandler wrapped;

    /**
     * Creates a new {@link GlobalAliasService}.
     */
    public GlobalAliasService(Context context, AliasHandler wrapped)
    {
        super(context);
        this.wrapped = wrapped;
    }

    @Override
    public void fromContainer(DataContainer container)
    {
        this.wrapped.fromContainer(container);
    }

    @Override
    public DataContainer toContainer()
    {
        return this.wrapped.toContainer();
    }

    @Override
    public AliasOwner getOwner()
    {
        return this.wrapped.getOwner();
    }

    @Override
    public Set<String> getValidTargets()
    {
        return this.wrapped.getValidTargets();
    }

    @Override
    public Optional<AliasRegistry> getRegistry(String target)
    {
        return this.wrapped.getRegistry(target);
    }

    @Override
    public AliasRegistry registerTarget(String target)
    {
        return this.wrapped.registerTarget(target);
    }

    @Override
    public boolean hasTarget(String target)
    {
        return this.wrapped.hasTarget(target);
    }

    @Override
    public boolean removeTarget(String target)
    {
        return this.wrapped.removeTarget(target);
    }

    @Override
    public void clearTargets()
    {
        this.wrapped.clearTargets();
    }

    @Override
    protected void _init()
    {
    }

    @Override
    protected void _shutdown()
    {
    }

}
