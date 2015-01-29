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
package com.voxelplugineering.voxelsniper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.thevoxelbox.vsl.api.node.Node;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.nodes.block.BlockBreakNode;

/**
 * Standard node test class. TODO Actually write some tests!
 */
public class NodeTest
{

    RuntimeState state;

    /**
     * Test setup.
     */
    @Before
    public void setupTests()
    {
        UUID uuid = UUID.randomUUID();
        this.state = mock(RuntimeState.class);
        when(this.state.getUUID()).thenReturn(uuid);
    }

    /**
     * Tests the {@link BlockBreakNode}.
     */
    @Test
    public void testBlockBreakNode()
    {
        Material material = mock(Material.class);
        Location location = mock(Location.class);
        Block block = mock(Block.class);
        when(block.getMaterial()).thenReturn(material);
        when(block.getLocation()).thenReturn(location);

        BlockBreakNode node = new BlockBreakNode(new Provider<Block>(Mockito.mock(Node.class), block));
        node.exec(this.state);

        assertEquals(node.getLocation().get(this.state), location);
        assertEquals(node.getMaterial().get(this.state), material);
    }

}
