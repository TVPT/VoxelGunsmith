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
import com.voxelplugineering.voxelsniper.core.nodes.block.BlockBreakNode;
import com.voxelplugineering.voxelsniper.core.nodes.material.IsLiquidNode;
import com.voxelplugineering.voxelsniper.core.nodes.material.MaterialCompareNode;
import com.voxelplugineering.voxelsniper.core.nodes.shape.DiscShapeNode;
import com.voxelplugineering.voxelsniper.core.util.ShapeValidation;
import com.voxelplugineering.voxelsniper.util.CheckRunNode;

/**
 * Standard node test class.
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

        assertEquals(location, node.getLocation().get(this.state));
        assertEquals(material, node.getMaterial().get(this.state));
    }

    /**
     * 
     */
    @Test
    public void testIsLiquidNode()
    {
        Material material = mock(Material.class);
        when(material.isLiquid()).thenReturn(true);

        IsLiquidNode node = new IsLiquidNode(new Provider<Material>(mock(Node.class), material));
        node.exec(this.state);

        assertEquals(true, node.isLiquid().get(this.state));
    }

    /**
     * 
     */
    @Test
    public void testMaterialCompareNode()
    {
        Provider<Material> a = new Provider<Material>(mock(Node.class), mock(Material.class));
        Provider<Material> b = new Provider<Material>(mock(Node.class), mock(Material.class));

        MaterialCompareNode node = new MaterialCompareNode(a, b);
        CheckRunNode check = new CheckRunNode(0);
        node.setBody(check);
        node.exec(this.state);

        check.end();
    }

    /**
     * 
     */
    @Test
    public void testMaterialCompareNode2()
    {
        Material mat = mock(Material.class);
        Provider<Material> a = new Provider<Material>(mock(Node.class), mat);
        Provider<Material> b = new Provider<Material>(mock(Node.class), mat);

        MaterialCompareNode node = new MaterialCompareNode(a, b);
        CheckRunNode check = new CheckRunNode(1);
        node.setBody(check);
        node.exec(this.state);

        check.end();
    }

    /**
     * 
     */
    @Test
    public void testMaterialCompareNode3()
    {
        Material mat = mock(Material.class);
        Provider<Material> b = new Provider<Material>(mock(Node.class), mat);

        MaterialCompareNode node = new MaterialCompareNode(mat, b);
        CheckRunNode check = new CheckRunNode(1);
        node.setBody(check);
        node.exec(this.state);

        check.end();
    }

    /**
     * 
     */
    @Test
    public void testMaterialCompareNode4()
    {
        Material mat = mock(Material.class);
        Provider<Material> b = new Provider<Material>(mock(Node.class), mock(Material.class));

        MaterialCompareNode node = new MaterialCompareNode(mat, b);
        CheckRunNode check = new CheckRunNode(0);
        node.setBody(check);
        node.exec(this.state);

        check.end();
    }

    /**
     * 
     */
    @Test
    public void testDiscShapeNode()
    {
        Provider<Double> rad = new Provider<Double>(mock(Node.class), 5d);

        DiscShapeNode node = new DiscShapeNode(rad);
        node.exec(this.state);

        assertEquals(true, ShapeValidation.isDisc(node.getShape().get(this.state)));
    }

    // TODO remaining node tests

}
