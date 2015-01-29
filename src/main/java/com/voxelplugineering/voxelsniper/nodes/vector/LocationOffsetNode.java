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
package com.voxelplugineering.voxelsniper.nodes.vector;

import com.thevoxelbox.vsl.node.AbstractNode;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Offsets a location by a given vector offset. Equivalent to
 * {@code location.add(vector.getX(), vector.getY(), vector.getZ());}
 */
public class LocationOffsetNode extends AbstractNode
{

    private final Provider<Location> input;
    private final Provider<Vector3i> offset;
    private final Provider<Location> output;

    /**
     * Create a new node.
     * 
     * @param input The input location
     * @param offset The offset vector
     */
    public LocationOffsetNode(Provider<Location> input, Provider<Vector3i> offset)
    {
        this.input = input;
        this.output = new Provider<Location>(this);
        this.offset = offset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        Location loc = this.input.get(state);
        this.output.set(loc.add(this.offset.get(state)), state.getUUID());
    }

    /**
     * Gets the offset location provider.
     * 
     * @return The offset location
     */
    public Provider<Location> getOffsetLocation()
    {
        return this.output;
    }

}
