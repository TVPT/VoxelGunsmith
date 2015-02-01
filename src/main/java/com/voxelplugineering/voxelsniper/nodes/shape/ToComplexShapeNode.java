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

import com.thevoxelbox.vsl.annotation.Input;
import com.thevoxelbox.vsl.annotation.Output;
import com.thevoxelbox.vsl.node.AbstractNode;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.shape.ComplexShape;

/**
 * A node to convert a shape which does not support changes to a
 * {@link ComplexShape}.
 */
public class ToComplexShapeNode extends AbstractNode
{

    @Input
    private final Provider<Shape> input;
    @Output
    private final Provider<Shape> output;

    /**
     * Creates a new {@link ToComplexShapeNode}.
     * 
     * @param in The input shape
     */
    public ToComplexShapeNode(Provider<Shape> in)
    {
        this.input = in;
        this.output = new Provider<Shape>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        Shape s = this.input.get(state);
        if (s.supportsChanges())
        {
            this.output.set(s, state.getUUID());
        } else
        {
            this.output.set(new ComplexShape(s), state.getUUID());
        }
    }

    /**
     * Gets the complex converted shape.
     * 
     * @return The complex shape
     */
    public Provider<Shape> getComplexShape()
    {
        return this.output;
    }
}
