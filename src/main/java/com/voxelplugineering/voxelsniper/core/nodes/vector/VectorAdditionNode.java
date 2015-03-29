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
package com.voxelplugineering.voxelsniper.core.nodes.vector;

import com.thevoxelbox.vsl.node.AbstractNode;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.core.util.math.Vector3i;

/**
 * Adds two {@link Vector3i}s together and returns the result.
 */
public class VectorAdditionNode extends AbstractNode
{

    private final Provider<Vector3i> a;
    private final Provider<Vector3i> b;
    private final Provider<Vector3i> r;

    /**
     * Create a new node.
     * 
     * @param a The first vector
     * @param b The second vector
     */
    public VectorAdditionNode(Provider<Vector3i> a, Provider<Vector3i> b)
    {
        this.a = a;
        this.b = b;
        this.r = new Provider<Vector3i>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        this.r.set(this.a.get(state).add(this.b.get(state)), state.getUUID());
    }

    /**
     * Gets the result of the operation.
     * 
     * @return The result
     */
    public Provider<Vector3i> getResult()
    {
        return this.r;
    }

}
