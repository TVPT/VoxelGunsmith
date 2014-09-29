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
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import com.voxelplugineering.voxelsniper.api.INode;

public abstract class Node implements INode, Serializable
{
    private static final long serialVersionUID = -979649184372246054L;
    
    protected Map<String, NodeInput> inputs = new HashMap<String, NodeInput>();
    protected Map<String, NodeOutput<?>> outputs = new HashMap<String, NodeOutput<?>>();
    
    protected boolean dirty = true;

    @Override
    public boolean mapInput(String input, INode holder, String holderOutput)
    {
        inputs.get(input).set(holder, holderOutput);
        return true;
    }

    @Override
    public boolean isValidInput(String input, Class<?> type)
    {
        return inputs.keySet().contains(input) && inputs.get(input).getType().test(type);
    }

    @Override
    public boolean isInputSet(String input)
    {
        return inputs.keySet().contains(input) && inputs.get(input).getValue() != null;
    }
    
    @Override
    public void validateInputs() throws MissingResourceException
    {
        for(String required: inputs.keySet())
        {
            NodeInput input = inputs.get(required);
            if(input.isRequired() && input.getValue() == null)
            {
                throw new MissingResourceException("Node was found to be missing required resource " + input.getName(), input.getType().name(), required);
            }
        }
        for(String inputName: inputs.keySet())
        {
            NodeInput input = inputs.get(inputName);
            if(input.getValue() != null)
            {
                input.getHolder().validateInputs();
            }
        }        
    }

    @Override
    public void walk(Set<INode> nodes)
    {
        nodes.add(this);
        for(String inputName: inputs.keySet())
        {
            NodeInput input = inputs.get(inputName);
            if(input.getHolder() != null)
            {
                input.getHolder().walk(nodes);
            }
        }
    }
    
    @Override
    public Object getInput(String name)
    {
        if(!this.inputs.keySet().contains(name)) return null;
        return this.inputs.get(name).getValue();
    }

    @Override
    public Object getOutput(String name)
    {
        if(!this.outputs.keySet().contains(name)) return null;
        if(dirty) calculate();
        return this.outputs.get(name).getValue();
    }
    
    @Override
    public void calculate()
    {
        dirty = false;
    }
    
    public String toString(int t)
    {
        String s = t == 0? "" : "\t";
        s += "Node type: " + this.getClass().getName() + "\n";
        for(int i = 0; i < t; i++) s+= "\t";
        s += "Inputs (" + this.inputs.size() + ")\n";
        for(int i = 0; i < t; i++) s+= "\t";
        s += "[\n";
        for(int i = 0; i < t; i++) s+= "\t";
        for(String n: this.inputs.keySet())
        {
            s += "\t";
            NodeInput i = inputs.get(n);
            s += "Name: " + i.getName() + " ; Type: " + i.getType().name() + "\n";
            for(int o = 0; o < t; o++) s+= "\t";
            s += i.getHolder().toString(t+1);
            s += "\n";
            for(int o = 0; o < t; o++) s+= "\t";
        }
        s += "]\n";
        for(int i = 0; i < t; i++) s+= "\t";
        s += "Outputs (" + this.outputs.size() + ")\n";
        for(int i = 0; i < t; i++) s+= "\t";
        s += "[\n";
        for(int i = 0; i < t; i++) s+= "\t";
        for(String n: this.outputs.keySet())
        {
            s += "\t";
            NodeOutput<?> i = outputs.get(n);
            s += i.getName() + " : " + i.getType().getName() + " : " + i.getValue() + "\n";
            for(int o = 0; o < t; o++) s+= "\t";
        }
        s += "]\n";
        return s;
    }
}
