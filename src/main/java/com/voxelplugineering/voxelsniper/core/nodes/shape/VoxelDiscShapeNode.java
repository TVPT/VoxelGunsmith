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

import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.core.shape.csg.CuboidShape;
import com.voxelplugineering.voxelsniper.core.util.math.Vector3i;

/**
 * Creates a square disc with with a side length of radius*2+1
 */
public class VoxelDiscShapeNode extends ShapeNode
{

    private final Provider<Double> radius;

    /**
     * Creates a new node.
     * 
     * @param radius The radius of the voxelDisc
     */
    public VoxelDiscShapeNode(Provider<Double> radius)
    {
        super();
        this.radius = radius;
    }

    @Override
    public void exec(RuntimeState state)
    {
        int rad = (int) Math.floor(this.radius.get(state));
        this.shape.set(new CuboidShape(rad * 2 + 1, 1, rad * 2 + 1, new Vector3i(rad, 0, rad)), state.getUUID());
    }
}
