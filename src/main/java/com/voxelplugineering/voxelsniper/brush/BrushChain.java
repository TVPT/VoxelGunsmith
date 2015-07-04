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
package com.voxelplugineering.voxelsniper.brush;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.Queue;

import com.google.common.collect.Queues;
import com.voxelplugineering.voxelsniper.entity.Player;

/**
 * Represents a chain of brushes. Unlike {@link Brush} this should be a unique instance per player
 * and should not be shared between users.
 */
public class BrushChain
{

    private final String cmd;
    private final Queue<Brush> brushes;

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
        this.cmd = checkNotNull(cmd);
        this.brushes = Queues.newArrayDeque();
        if (brushes != null)
        {
            for (Brush b : brushes)
            {
                this.brushes.add(b);
            }
        }
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
    public void run(Player player, BrushVars brushVariables)
    {
        checkNotNull(brushVariables);
        for (Iterator<Brush> it = this.brushes.iterator(); it.hasNext();)
        {
            Brush next = it.next();
            brushVariables.setContext(BrushContext.of(next));
            ExecutionResult ex = next.run(player, brushVariables);
            if (!ex.shouldContinue())
            {
                break;
            }
        }
    }

    /**
     * Chains a brush onto the end of the existing chain.
     * 
     * @param brush The new brush
     */
    public void chain(Brush brush)
    {
        checkNotNull(brush);
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
