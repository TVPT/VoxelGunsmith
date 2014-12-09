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
package com.voxelplugineering.voxelsniper.api;

import com.thevoxelbox.vsl.api.IChainedRunnableGraph;
import com.thevoxelbox.vsl.api.IVariableHolder;
import com.voxelplugineering.voxelsniper.util.BrushCompiler;

/**
 * The Gunsmith version of {@link com.thevoxelbox.vsl.api.IRunnableGraph}. This contains the actual logic for the brush compiled from the visual
 * scripting language graphs.
 * <p>
 * Changes to this interface MUST be replicated in {@link BrushCompiler}!!!
 */
public interface IBrush extends IChainedRunnableGraph
{

    /**
     * Specialized run method for Gunsmith, adds a reference to the player executing the brush.
     * 
     * @param vars the execution variables, cannot be null
     * @param player the player, cannot be null
     */
    void run(IVariableHolder vars, ISniper player);

    /**
     * Returns an array of variables that are required by this brush part.
     * 
     * @return the variables
     */
    String[] getRequiredVars();
}
