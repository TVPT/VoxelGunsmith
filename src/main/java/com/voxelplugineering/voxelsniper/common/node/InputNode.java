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

public class InputNode<T> extends Node
{
    private static final long serialVersionUID = -1563189602513292697L;

    private boolean           isVariable       = true;

    private String            desription       = "";

    private Class<T>          type;

    @SuppressWarnings("unchecked")
    public InputNode(T value, Class<T> type)
    {
        this.type = type;
        outputs.put("value", new NodeOutput<T>("value", type));
        ((NodeOutput<T>) outputs.get("value")).setValue(value);
    }

    @SuppressWarnings("unchecked")
    public InputNode(String description, T value, Class<T> type)
    {
        this.desription = description;
        this.type = type;
        outputs.put("value", new NodeOutput<T>("value", type));
        ((NodeOutput<T>) outputs.get("value")).setValue(value);
    }

    public boolean isVariable()
    {
        return isVariable;
    }

    public void setVariable(boolean isVariable)
    {
        this.isVariable = isVariable;
    }

    public void setValue(Object value)
    {
        if (!this.isVariable || !value.getClass().equals(this.type)) return;
        ((NodeOutput<T>) outputs.get("value")).setValue((T) value);
        dirty = true;
    }

    public Class<T> getType()
    {
        return type;
    }

    public String getDesription()
    {
        return desription;
    }
}
