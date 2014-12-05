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
package com.voxelplugineering.voxelsniper.util;

import com.thevoxelbox.vsl.api.INodeGraph;
import com.thevoxelbox.vsl.node.ChainableNodeGraph;

/**
 * A {@link INodeGraph} for visual scripts representing brush parts.
 */
public class BrushPartNodeGraph extends ChainableNodeGraph
{
    private static final long serialVersionUID = 1L;

    /**
     * The names of special variables which are required by this brush part. TODO change to a {@link Pair}{@literal<String, Type>}.
     */
    public String[] requiredVars = null;

    /**
     * Creates a new node graph.
     * 
     * @param name the graph name
     */
    public BrushPartNodeGraph(String name)
    {
        super(name);
    }

    /**
     * Returns variables required by this brush part.
     * 
     * @return the required variables
     */
    public String[] getRequiredVars()
    {
        return this.requiredVars;
    }

    /**
     * Sets the required variables.
     * 
     * @param vars the required variables
     */
    public void setRequiredVars(String... vars)
    {
        this.requiredVars = vars;
    }

}
