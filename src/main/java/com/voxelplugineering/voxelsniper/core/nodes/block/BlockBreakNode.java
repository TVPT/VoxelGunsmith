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
package com.voxelplugineering.voxelsniper.core.nodes.block;

import com.thevoxelbox.vsl.node.AbstractNode;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.material.Material;

/**
 * Node to get location and material from block
 */
public class BlockBreakNode extends AbstractNode
{

    private final Provider<Block> block;
    private final Provider<Location> location;
    private final Provider<Material> material;

    /**
     * Creates a new node.
     * 
     * @param block The block provider
     */
    public BlockBreakNode(Provider<Block> block)
    {
        this.block = block;
        this.location = new Provider<Location>(this);
        this.material = new Provider<Material>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        Block b = this.block.get(state);
        this.location.set(b.getLocation(), state.getUUID());
        this.material.set(b.getMaterial(), state.getUUID());
    }

    /**
     * Gets the location output;
     * 
     * @return The location
     */
    public Provider<Location> getLocation()
    {
        return this.location;
    }

    /**
     * Gets the material output.
     * 
     * @return The material
     */
    public Provider<Material> getMaterial()
    {
        return this.material;
    }

}
