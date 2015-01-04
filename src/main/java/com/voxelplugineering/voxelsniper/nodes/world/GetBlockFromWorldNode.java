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
package com.voxelplugineering.voxelsniper.nodes.world;

import com.thevoxelbox.vsl.node.Node;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Takes a world and a vector position and returns the block at that location. Equivalent to
 * {@code world.getBlockAt(vector.getX(), vector.getY(), vector.getZ());}
 */
public class GetBlockFromWorldNode extends Node
{

    private final Provider<World> world;
    private final Provider<Vector3i> vector;
    private final Provider<Block> block;

    /**
     * Creates a new node.
     * 
     * @param world The world
     * @param vector The position
     */
    public GetBlockFromWorldNode(Provider<World> world, Provider<Vector3i> vector)
    {
        this.world = world;
        this.vector = vector;
        this.block = new Provider<Block>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        this.block.set(this.world.get(state).getBlock(this.vector.get(state)).get(), state.getUUID());
    }

    /**
     * Gets the block provider
     * 
     * @return The block
     */
    public Provider<Block> getBlock()
    {
        return this.block;
    }

}
