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
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Gets the material of a {@link MaterialShape} at the given vector target.
 */
public class MaterialShapeGetNode extends MaterialShapeNode
{
    private final Provider<Vector3i> target;
    private final Provider<Material> material;

    /**
     * Creates a {@link MaterialShapeGetNode}.
     * 
     * @param shape The shape
     * @param target The position to fetch
     */
    public MaterialShapeGetNode(Provider<MaterialShape> shape, Provider<Vector3i> target)
    {
        super(shape);
        this.target = target;
        this.material = new Provider<Material>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        Vector3i target = this.target.get(state);
        this.material.set(this.shape.get(state).get(target.getX(), target.getY(), target.getZ(), false).get(), state.getUUID());
    }

    /**
     * Gets the material provider.
     * 
     * @return The material
     */
    public Provider<Material> getMaterial()
    {
        return this.material;
    }

}
