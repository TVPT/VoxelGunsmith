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
package com.voxelplugineering.voxelsniper.nodes.vector;

import com.thevoxelbox.vsl.node.AbstractNode;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Returns the negative of the given vector.
 */
public class Vector3iNegationNode extends AbstractNode
{

    private Provider<Vector3i> vecIn;
    private Provider<Vector3i> vecOut;

    /**
     * Creates a new {@link Vector3iNegationNode}.
     * 
     * @param vec The vector to negate
     */
    public Vector3iNegationNode(Provider<Vector3i> vec)
    {
        this.vecIn = vec;
        this.vecOut = new Provider<Vector3i>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        this.vecOut.set(this.vecIn.get(state).multipy(-1), state.getUUID());
    }

    /**
     * Gets the negative vector provider.
     * 
     * @return The vector
     */
    public Provider<Vector3i> getNegativeVector()
    {
        return this.vecOut;
    }

}
