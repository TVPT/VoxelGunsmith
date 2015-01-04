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

import com.google.common.base.Optional;
import com.thevoxelbox.vsl.node.Node;
import com.thevoxelbox.vsl.util.Input;
import com.thevoxelbox.vsl.util.Output;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.util.Direction;

/**
 * A node that fetches available neighbouring blocks.
 */
public class BlockNeighboursNode extends Node
{
    @Input
    private final Provider<Block> blockProvider;
    @Output
    private final Provider<Block[]> blockArrayProvider;

    /**
     * Creates a new BlockNeighboursNode.
     *
     * @param blockProvider The block provider
     */
    public BlockNeighboursNode(Provider<Block> blockProvider)
    {
        this.blockProvider = blockProvider;
        this.blockArrayProvider = new Provider<Block[]>(this);
    }

    /**
     * Gets the neighbors found by this node.
     *
     * @return The neighboring blocks
     */
    public Provider<Block[]> getNeighbours()
    {
        return this.blockArrayProvider;
    }

    @Override
    public void exec(RuntimeState state)
    {
        Block block = this.blockProvider.get(state);
        Block[] neighbours = new Block[6];
        int count = 0;
        for (Direction direction : Direction.getCardinals())
        {
            Location newLocation = block.getLocation()
                    .add(direction.getModX(), direction.getModY(), direction.getModZ());
            Optional<Block> newBlock = block.getWorld().getBlock(newLocation);
            if (newBlock.isPresent())
            {
                neighbours[count] = newBlock.get();
            }
            count++;
        }
        this.blockArrayProvider.set(neighbours, state.getUUID());
    }
}
