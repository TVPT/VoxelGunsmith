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
import com.voxelplugineering.voxelsniper.api.world.material.Material;

/**
 * A node that counts the occurrences of a material in a block array.
 */
public class CountOccurrencesNode extends Node
{
    private final Provider<Block[]> blockArrayProvider;
    private final Provider<Material> materialProvider;
    private final Provider<Integer> integerProvider;

    /**
     * Creates a new CountOccurrences node.
     *
     * @param materialProvider The material provider
     * @param blockArrayProvider The block array provider
     */
    public CountOccurrencesNode(Provider<Material> materialProvider, Provider<Block[]> blockArrayProvider)
    {
        this.materialProvider = materialProvider;
        this.blockArrayProvider = blockArrayProvider;
        this.integerProvider = new Provider<Integer>(this);
    }

    @Override
    public void exec(RuntimeState state)
    {
        Block[] blocks = this.blockArrayProvider.get(state);
        Material material = this.materialProvider.get(state);
        int count = 0;
        for (Block block : blocks)
        {
            if (block != null && block.getMaterial().equals(material)) {
                count++;
            }
        }
        this.integerProvider.set(count, state.getUUID());
    }
}
