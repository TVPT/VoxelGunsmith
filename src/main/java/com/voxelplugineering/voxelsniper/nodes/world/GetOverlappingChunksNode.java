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

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.thevoxelbox.vsl.node.AbstractNode;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.Chunk;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.util.math.Vector3d;

/**
 * Gets all chunks which overlap with the given shape.
 */
public class GetOverlappingChunksNode extends AbstractNode
{

    private final Provider<Shape> shape;
    private final Provider<Location> target;
    private final Provider<Chunk[]> chunks;

    /**
     * Creates {@link GetOverlappingChunksNode}
     * 
     * @param shape The shape
     * @param target The target position
     */
    public GetOverlappingChunksNode(Provider<Shape> shape, Provider<Location> target)
    {
        this.shape = shape;
        this.target = target;
        this.chunks = new Provider<Chunk[]>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        Shape shape = this.shape.get(state);
        Vector3d offsetTarget = this.target.get(state).add(shape.getOrigin().multipy(-1)).toVector();
        List<Chunk> chunks = Lists.newArrayList();
        //TODO remove assumptions about chunk size and orientation
        for (int x = (int) offsetTarget.getX(); x < offsetTarget.getX() + shape.getWidth(); x++)
        {
            for (int z = (int) offsetTarget.getZ(); z < offsetTarget.getZ() + shape.getLength(); z++)
            {
                Optional<Chunk> c = this.target.get(state).getWorld().getChunk((x < 0 ? x - 16 : x) / 16, 0, (z < 0 ? z - 16 : z) / 16);
                if (c.isPresent())
                {
                    if (!chunks.contains(c.get()))
                    {
                        chunks.add(c.get());
                    }
                }
            }
        }
        this.chunks.set(chunks.toArray(new Chunk[chunks.size()]), state.getUUID());
    }

    /**
     * Gets the overlapping chunks
     * 
     * @return The chunks
     */
    public Provider<Chunk[]> getChunks()
    {
        return this.chunks;
    }

}
