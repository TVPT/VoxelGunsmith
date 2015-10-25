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

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.command.MessageReceiver;
import com.voxelplugineering.voxelsniper.util.Nameable;

import com.google.common.collect.Queues;

import java.util.Iterator;
import java.util.Queue;

/**
 * Represents a chain of brushes. Unlike {@link Brush} this should be a unique instance per player
 * and should not be shared between users.
 */
public class BrushChain implements Nameable
{

    private final String cmd;
    private final Queue<BrushWrapper> brushes;
    private BrushWrapper continuePoint = null;

    /**
     * Creates a new {@link BrushChain}.
     * 
     * @param cmd The command name
     */
    public BrushChain(String cmd)
    {
        this(cmd, new BrushWrapper[0]);
    }

    /**
     * Creates a new {@link BrushChain}.
     * 
     * @param cmd The command
     * @param brushes The brushes to see the chain with
     */
    public BrushChain(String cmd, BrushWrapper... brushes)
    {
        this.cmd = checkNotNull(cmd);
        this.brushes = Queues.newArrayDeque();
        if (brushes != null)
        {
            for (BrushWrapper b : brushes)
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
    public BrushWrapper[] getBrushes()
    {
        return this.brushes.toArray(new BrushWrapper[this.brushes.size()]);
    }

    /**
     * Executes this brush chain.
     * 
     * @param player The player
     * @param brushVariables The execution variables
     */
    public void run(Player player, BrushVars brushVariables)
    {
        checkNotNull(brushVariables);
        if (this.brushes.isEmpty())
        {
            return;
        }
        if (this.continuePoint == null)
        {
            brushVariables.clearFlags();
        }
        Iterator<BrushWrapper> it = this.brushes.iterator();
        BrushWrapper next = it.next();
        if (this.continuePoint != null)
        {
            while (it.hasNext() && next != this.continuePoint)
            {
                GunsmithLogger.getLogger().debug("Skipping " + next.getName());
                next = it.next();
            }
            this.continuePoint = null;
        }
        brushVariables.setContext(BrushContext.of(next));
        GunsmithLogger.getLogger().debug("Executing " + next.getName());
        ExecutionResult ex = next.getBrush().run(player, brushVariables);
        if (!ex.shouldContinue())
        {
            if (ex instanceof ExecutionResult.Delay)
            {
                this.continuePoint = ((ExecutionResult.Delay) ex).getContinuePoint();
            }
            return;
        }
        while (it.hasNext())
        {
            next = it.next();
            brushVariables.setContext(BrushContext.of(next));
            GunsmithLogger.getLogger().debug("Executing " + next.getName());
            ex = next.getBrush().run(player, brushVariables);
            if (!ex.shouldContinue())
            {
                if (ex instanceof ExecutionResult.Delay)
                {
                    this.continuePoint = ((ExecutionResult.Delay) ex).getContinuePoint();
                }
                break;
            }
        }
    }

    /**
     * Chains a brush onto the end of the existing chain.
     * 
     * @param brush The new brush
     */
    public void chain(BrushWrapper brush)
    {
        checkNotNull(brush);
        this.brushes.add(brush);
    }

    @Override
    public String getName()
    {
        return this.cmd;
    }

    /**
     * Prints out all parameters of this brush chain to the given message receiver.
     * 
     * @param target The message receiver
     */
    public void printParams(final MessageReceiver target)
    {
        this.brushes.stream().filter((brush) -> {
            return brush.getParameters().length != 0;
        }).forEach((brush) -> {
            target.sendMessage(VoxelSniperConfiguration.paramHeader, brush.getName());
            for (BrushParam param : brush.getParameters())
            {
                target.sendMessage(VoxelSniperConfiguration.paramInfo, param.name(), param.desc());
            }
        });
    }

}
