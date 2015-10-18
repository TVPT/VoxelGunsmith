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

import com.voxelplugineering.voxelsniper.service.AbstractService;
import com.voxelplugineering.voxelsniper.service.scheduler.Scheduler;
import com.voxelplugineering.voxelsniper.util.Context;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * A service wrapping an {@link AliasHandler}.
 */
public class GlobalAliasService extends AbstractService implements GlobalAliasHandler
{

    private final AliasHandler wrapped;
    private final AliasSaveTask saveTask;

    /**
     * Creates a new {@link GlobalAliasService}.
     * 
     * @param context The context
     * @param wrapped The wrapped AliasHandler
     */
    public GlobalAliasService(Context context, AliasHandler wrapped)
    {
        super(context);
        this.wrapped = wrapped;
        this.saveTask = new AliasSaveTask();
        context.getRequired(Scheduler.class).startAsynchronousTask(this.saveTask, 300000);
    }

    @Override
    public Optional<AliasOwner> getOwner()
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
        this.saveTask.flush();
    }

    @Override
    public void save() throws IOException
    {
        this.wrapped.save();
    }

    @Override
    public void load() throws IOException
    {
        this.wrapped.load();
    }

    @Override
    public AliasSaveTask getAliasSaveTask()
    {
        return this.saveTask;
    }

}
