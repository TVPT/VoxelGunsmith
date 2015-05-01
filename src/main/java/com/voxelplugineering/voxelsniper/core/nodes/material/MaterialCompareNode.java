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
package com.voxelplugineering.voxelsniper.core.nodes.material;

import com.thevoxelbox.vsl.api.node.Node;
import com.thevoxelbox.vsl.node.AbstractNode;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.world.material.Material;

/**
 * A node for comparing two materials and executing another {@link Node} if the comparison is true.
 */
public class MaterialCompareNode extends AbstractNode
{

    private Node body = null;
    private final Provider<Material> a;
    private final Provider<Material> b;

    /**
     * Creates a new {@link MaterialCompareNode}.
     * 
     * @param a The first material
     * @param b The second material
     */
    public MaterialCompareNode(Provider<Material> a, Provider<Material> b)
    {
        this.a = a;
        this.b = b;
    }

    /**
     * Creates a new {@link MaterialCompareNode}.
     * 
     * @param a The first material
     * @param b The second material
     */
    public MaterialCompareNode(Material a, Provider<Material> b)
    {
        this.a = new Provider<Material>(this, a);
        this.b = b;
    }

    /**
     * Sets the body of the if-statement formed by this comparison.
     * 
     * @param body the node to execute if the comparison is true
     */
    public void setBody(Node body)
    {
        this.body = body;
    }

    @Override
    public void exec(RuntimeState state)
    {
        if (this.a.get(state).equals(this.b.get(state)))
        {
            Node next = this.body;
            while (next != null)
            {
                next.exec(state);
                next = next.getNext();
            }
        }
    }

}
