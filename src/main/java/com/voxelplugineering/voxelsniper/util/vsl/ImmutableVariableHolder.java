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
package com.voxelplugineering.voxelsniper.util.vsl;

import com.google.common.base.Optional;
import com.thevoxelbox.vsl.api.IVariableScope;

/**
 * An immutable variable holder.
 * <p>
 * <strong>WARNING:</strong> values returned from {@link #get(String)} or {@link #get(String, Class)} may be mutable!
 * </p>
 */
public final class ImmutableVariableHolder implements IVariableScope
{

    private final IVariableScope vars;

    /**
     * Creates a new {@link ImmutableVariableHolder} wrapping the given {@link IVariableScope}.
     * 
     * @param vars The scope to wrap
     */
    public ImmutableVariableHolder(IVariableScope vars)
    {
        this.vars = vars;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCaseSensitive(boolean cs)
    {
        throw new UnsupportedOperationException("immutable type.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Object> get(String name)
    {
        return this.vars.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> get(String name, Class<T> type)
    {
        return this.vars.get(name, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String name, Object value)
    {
        throw new UnsupportedOperationException("immutable type.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasValue(String name)
    {
        return this.vars.hasValue(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParent(IVariableScope scope)
    {
        throw new UnsupportedOperationException("immutable type.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IVariableScope getParent()
    {
        return new ImmutableVariableHolder(this.vars.getParent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IVariableScope> getHighestParent()
    {
        Optional<IVariableScope> high = this.vars.getHighestParent();
        if (high.isPresent())
        {
            return Optional.<IVariableScope>of(new ImmutableVariableHolder(high.get()));
        } else
        {
            return Optional.absent();
        }
    }

}
