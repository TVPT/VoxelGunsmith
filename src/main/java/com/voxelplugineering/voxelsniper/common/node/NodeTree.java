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
package com.voxelplugineering.voxelsniper.common.node;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import com.voxelplugineering.voxelsniper.api.INode;

public class NodeTree implements Serializable
{
    private static final long serialVersionUID = 8341667164810408458L;

    Set<ProcessorNode> roots = new HashSet<ProcessorNode>();
    Map<String, InputNode<?>> inputValues = new HashMap<String, InputNode<?>>();

    public NodeTree()
    {

    }

    public boolean addRoot(ProcessorNode node)
    {
        this.roots.add(node);

        return true;
    }

    public boolean compile()
    {

        reduceStaticBranches();
        findVariableInputs();
        return true;
    }

    public void reduceStaticBranches()
    {

    }

    public void findVariableInputs()
    {
        this.inputValues.clear();
        Set<INode> nodes = new HashSet<INode>();
        for (INode root : this.roots)
        {
            root.walk(nodes);
        }
        for (INode node : nodes)
        {
            if (node instanceof InputNode)
            {
                InputNode<?> input = (InputNode<?>) node;
                if (input.isVariable())
                {
                    this.inputValues.put(input.getDesription(), input);
                }
            }
        }
    }

    public boolean process(TreeInput... args) throws MissingResourceException
    {
        if (!validateInputs(args)) return false;
        for (TreeInput t : args)
        {
            if (this.inputValues.get(t.name) == null) continue;
            this.inputValues.get(t.name).setValue(t.value);
        }
        for (ProcessorNode p : this.roots)
        {
            p.validateInputs();
            p.process();
        }
        return true;
    }

    public boolean validateInputs(TreeInput[] args)
    {

        return true;
    }

    public String toString()
    {
        String s = "";
        for (INode n : this.roots)
        {
            s += n.toString(0);
        }
        return s;
    }
}

class TreeInput
{
    public String name;
    public Object value;

    public TreeInput(String s, Object o)
    {
        this.name = s;
        this.value = o;
    }
}
