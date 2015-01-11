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
package com.voxelplugineering.voxelsniper.nodes.world.buffer;

import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.world.material.Material;

/**
 * A node for flooding a materialshape with a material.
 */
public class MaterialShapeFloodNode extends MaterialShapeNode
{

    private final Provider<Material> fill;

    /**
     * Creates a new {@link MaterialShapeFloodNode}
     * 
     * @param shape The shape to flood
     * @param fill The material to flood it with
     */
    public MaterialShapeFloodNode(Provider<MaterialShape> shape, Provider<Material> fill)
    {
        super(shape);
        this.fill = fill;
    }

    /**
     * Creates a new {@link MaterialShapeFloodNode}
     * 
     * @param shape The shape to flood
     * @param fill The material to flood it with
     */
    public MaterialShapeFloodNode(Provider<MaterialShape> shape, Material fill)
    {
        super(shape);
        this.fill = new Provider<Material>(fill);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        this.shape.get(state).flood(this.fill.get(state));
    }

}
