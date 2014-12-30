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
package com.voxelplugineering.voxelsniper.nodes.shape;

import com.thevoxelbox.vsl.node.Node;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.shape.Shape;

/**
 * A node for flattening a shape into a 1 unit high disc.
 */
public class FlattenShapeNode extends Node
{
    private final Provider<Shape> shapeIn;
    private final Provider<Shape> shapeOut;

    /**
     * Creates a new node.
     * 
     * @param shape The shape provider
     */
    public FlattenShapeNode(Provider<Shape> shape)
    {
        this.shapeIn = shape;
        this.shapeOut = new Provider<Shape>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        Shape s = this.shapeIn.get(state);
        s.flatten();
        this.shapeOut.set(s, state.getUUID());
    }

    /**
     * Gets the flattened shape
     * 
     * @return The shape
     */
    public Provider<Shape> getFlattenedShape()
    {
        return this.shapeOut;
    }

}
