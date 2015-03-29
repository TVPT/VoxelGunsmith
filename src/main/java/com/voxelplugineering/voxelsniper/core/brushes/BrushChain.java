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
package com.voxelplugineering.voxelsniper.core.brushes;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.thevoxelbox.vsl.api.variables.VariableScope;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.brushes.Brush;

/**
 * Represents a chain of brushes. Unlike {@link Brush} this should be a unique
 * instance per player and should not be shared between users.
 */
public class BrushChain
{

    private final String cmd;
    private final Queue<Brush> brushes;
    private final Map<String, String> args;

    /**
     * Creates a new {@link BrushChain}.
     * 
     * @param cmd The command name
     */
    public BrushChain(String cmd)
    {
        this(cmd, new Brush[0]);
    }

    /**
     * Creates a new {@link BrushChain}.
     * 
     * @param cmd The command
     * @param brushes The brushes to see the chain with
     */
    public BrushChain(String cmd, Brush... brushes)
    {
        this.cmd = cmd;
        this.brushes = Queues.newArrayDeque();
        for (Brush b : brushes)
        {
            this.brushes.add(b);
        }
        this.args = Maps.newHashMap();
    }

    /**
     * Gets all brushes within this chain.
     * 
     * @return The brushes
     */
    public Brush[] getBrushes()
    {
        return this.brushes.toArray(new Brush[this.brushes.size()]);
    }

    /**
     * Executes this brush chain.
     * 
     * @param brushVariables The execution variables
     */
    public void run(VariableScope brushVariables)
    {
        RuntimeState state = new RuntimeState(brushVariables);
        for (Iterator<Brush> it = this.brushes.iterator(); it.hasNext();)
        {
            Brush next = it.next();
            if (this.args.containsKey(next.getName()))
            {
                next.parseArguments(this.args.get(next.getName()), brushVariables);
            }
            next.run(state);
        }
    }

    /**
     * Sets the argument for a particular brush.
     * 
     * @param brush The target brush
     * @param arg The argument
     */
    public void setBrushArgument(String brush, String arg)
    {
        this.args.put(brush, arg);
    }

    /**
     * Chains a brush onto the end of the existing chain.
     * 
     * @param brush The new brush
     */
    public void chain(Brush brush)
    {
        this.brushes.add(brush);
    }

    /**
     * Gets the brush chain command.
     * 
     * @return The command
     */
    public String getName()
    {
        return this.cmd;
    }

}
