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
package com.voxelplugineering.voxelsniper.core.nodes.shape;

import com.thevoxelbox.vsl.api.node.Node;
import com.thevoxelbox.vsl.node.AbstractNode;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.core.util.math.Vector3i;

/**
 * Iterates over every point in a shape and executes a node pathway.
 */
public class ShapeForEachNode extends AbstractNode
{

    private final Provider<Shape> shape;
    private final Provider<Vector3i> nextValue;
    private Node next;
    private final boolean offset;

    /**
     * Creates a new {@link ShapeForEachNode}.
     * 
     * @param shape The shape to iterate over
     * @param offset Whether to offset by origin
     */
    public ShapeForEachNode(Provider<Shape> shape, boolean offset)
    {
        this.shape = shape;
        this.nextValue = new Provider<Vector3i>(this);
        this.offset = offset;
    }

    /**
     * Sets the body of the loop.
     * 
     * @param node The node to call
     */
    public void setBody(Node node)
    {
        this.next = node;
    }

    @Override
    public void exec(RuntimeState state)
    {
        Shape s = this.shape.get(state);
        Vector3i o = s.getOrigin();
        for (int x = 0; x < s.getWidth(); x++)
        {
            for (int y = 0; y < s.getHeight(); y++)
            {
                for (int z = 0; z < s.getLength(); z++)
                {
                    if (s.get(x, y, z, false))
                    {
                        if (this.offset)
                        {
                            this.nextValue.set(new Vector3i(x - o.getX(), y - o.getY(), z - o.getZ()), state.getUUID());
                        } else
                        {
                            this.nextValue.set(new Vector3i(x, y, z), state.getUUID());
                        }
                        Node n = this.next;
                        while (n != null)
                        {
                            n.exec(state);
                            n = n.getNext();
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the provider for the next position in the shape.
     * 
     * @return The next position
     */
    public Provider<Vector3i> getNextValue()
    {
        return this.nextValue;
    }
}
