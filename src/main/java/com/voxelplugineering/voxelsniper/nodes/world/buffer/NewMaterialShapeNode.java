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

import com.thevoxelbox.vsl.node.AbstractNode;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.shape.SingleMaterialShape;

/**
 * Creates a new MaterialShape wrapped around the given shape.
 */
public class NewMaterialShapeNode extends AbstractNode
{

    private final Provider<MaterialShape> matshape;
    private final Provider<Shape> shape;
    private final Provider<Material> defaultMaterial;

    /**
     * Creates a {@link NewMaterialShapeNode}.
     * 
     * @param shape The shape
     * @param defaultMaterial The default material
     */
    public NewMaterialShapeNode(Provider<Shape> shape, Provider<Material> defaultMaterial)
    {
        this.matshape = new Provider<MaterialShape>(this);
        this.shape = shape;
        this.defaultMaterial = defaultMaterial;
    }

    /**
     * Creates a {@link NewMaterialShapeNode}.
     * 
     * @param shape The shape
     * @param defaultMaterial The default material
     */
    public NewMaterialShapeNode(Provider<Shape> shape, Material defaultMaterial)
    {
        this.matshape = new Provider<MaterialShape>(this);
        this.shape = shape;
        this.defaultMaterial = new Provider<Material>(this, defaultMaterial);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        MaterialShape ms = new SingleMaterialShape(this.shape.get(state), this.defaultMaterial.get(state));
        this.matshape.set(ms, state.getUUID());
    }

    /**
     * Gets the new material shape provider.
     * 
     * @return The material shape
     */
    public Provider<MaterialShape> getMaterialShape()
    {
        return this.matshape;
    }

}
