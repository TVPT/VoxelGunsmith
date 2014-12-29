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

import com.thevoxelbox.vsl.node.Node;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.shape.Shape;

/**
 * Creates a new MaterialShape wrapped around the given shape.
 */
public class NewMaterialShapeNode extends Node
{
    private final Provider<MaterialShape> matshape;
    private final Provider<Shape> shape;
    private final Provider<Material> defaultMaterial;

    /**
     * Creates a {@link NewMaterialShapeNode}.
     */
    public NewMaterialShapeNode(Provider<Shape> shape, Provider<Material> defaultMaterial)
    {
        this.matshape = new Provider<MaterialShape>(this);
        this.shape = shape;
        this.defaultMaterial = defaultMaterial;
    }

    @Override
    public void exec(RuntimeState state)
    {
        MaterialShape ms = new MaterialShape(this.shape.get(state), this.defaultMaterial.get(state));
        this.matshape.set(ms, state.getUUID());
    }

    public Provider<MaterialShape> getMaterialShape()
    {
        return this.matshape;
    }

}
