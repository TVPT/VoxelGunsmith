/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 The Voxel Plugin Team
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
package com.voxelplugineering.voxelsniper.common.node;

import java.io.Serializable;

import com.voxelplugineering.voxelsniper.api.INode;

public class NodeInput implements Serializable
{
    private static final long serialVersionUID = -3149768229377635513L;

    private INode     valueHolder  = null;

    private String    value;

    private boolean   required     = true;

    private InputType type;

    private Object    defaultValue = null;

    public NodeInput(InputType type)
    {
        this.type = type;
    }

    public NodeInput(InputType type, boolean required, Object def)
    {
        this.type = type;
        this.required = required;
        this.defaultValue = def;
    }

    public boolean set(INode holder, String value)
    {
        this.valueHolder = holder;
        this.value = value;
        return true;
    }

    public boolean isRequired()
    {
        return this.required;
    }

    public InputType getType()
    {
        return this.type;
    }

    public Object getValue()
    {
        if (this.valueHolder == null) return this.defaultValue;
        return this.valueHolder.getOutput(this.value);
    }

    public INode getHolder()
    {
        return this.valueHolder;
    }

    public String getName()
    {
        return this.value;
    }

}
