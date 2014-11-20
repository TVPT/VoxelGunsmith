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
package com.voxelplugineering.voxelsniper.util;

import com.thevoxelbox.vsl.api.IChainableNodeGraph;
import com.thevoxelbox.vsl.api.IGraphCompiler;
import com.thevoxelbox.vsl.classloader.ASMClassLoader;
import com.thevoxelbox.vsl.node.ChainableNodeGraph;
import com.thevoxelbox.vsl.node.variables.ChainedInputNode;
import com.thevoxelbox.vsl.node.variables.ChainedOutputNode;
import com.thevoxelbox.vsl.node.variables.VariableGetNode;
import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.common.CommonBlock;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;
import com.voxelplugineering.voxelsniper.nodes.BlockBreakNode;
import com.voxelplugineering.voxelsniper.nodes.GetBlockFromLocationNode;
import com.voxelplugineering.voxelsniper.nodes.LocationOffsetNode;
import com.voxelplugineering.voxelsniper.nodes.MaterialSetNode;
import com.voxelplugineering.voxelsniper.nodes.ShapeForEachNode;
import com.voxelplugineering.voxelsniper.nodes.VoxelShapeNode;
import com.voxelplugineering.voxelsniper.shape.Shape;

/**
 * In lieu of having flat file brushes this will temporarily serve as a builder for brushes at runtime for debugging during development.
 */
public class TemporaryBrushBuilder
{

    /**
     * Creates and loads all brushes into the blobal brush manager.
     */
    public static void buildBrushes()
    {
        ASMClassLoader classloader = Gunsmith.getGlobalBrushManager().getClassLoader();

        try
        { //Snipe brush
            VariableGetNode getTarget = new VariableGetNode("targetBlock", CommonBlock.class);
            VariableGetNode getMaterial = new VariableGetNode("setMaterial", CommonMaterial.class);
            MaterialSetNode set = new MaterialSetNode();
            set.mapInput("targetBlock", getTarget.getOutput("value"));
            set.mapInput("material", getMaterial.getOutput("value"));

            IChainableNodeGraph brush = new ChainableNodeGraph("snipe");
            brush.setStartNode(set);
            IGraphCompiler compiler = classloader.getCompiler(IBrush.class);
            @SuppressWarnings("unchecked")
            Class<? extends IBrush> compiled = (Class<? extends IBrush>) compiler.compile(classloader, brush);
            Gunsmith.getGlobalBrushManager().loadBrush("snipe", compiled);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create snipe brush");
        }

        try
        { //Voxel shape brush part
            VariableGetNode radius = new VariableGetNode("brushSize", Double.class);
            VoxelShapeNode shape = new VoxelShapeNode();
            shape.mapInput("radius", radius.getOutput("value"));
            ChainedOutputNode shapeOut = new ChainedOutputNode("shape", Shape.class);
            shapeOut.mapInput("value", shape.getOutput("shape"));

            IChainableNodeGraph brush = new ChainableNodeGraph("voxel");
            brush.setStartNode(shapeOut);
            IGraphCompiler compiler = classloader.getCompiler(IBrush.class);
            @SuppressWarnings("unchecked")
            Class<? extends IBrush> compiled = (Class<? extends IBrush>) compiler.compile(classloader, brush);
            Gunsmith.getGlobalBrushManager().loadBrush("voxel", compiled);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create voxel brush part");
        }

        try
        { //shape set material
            ChainedInputNode shapeIn = new ChainedInputNode("shape", Shape.class);
            ShapeForEachNode forEach = new ShapeForEachNode();
            MaterialSetNode set = new MaterialSetNode();
            VariableGetNode getMaterial = new VariableGetNode("setMaterial", CommonMaterial.class);
            VariableGetNode target = new VariableGetNode("targetBlock", CommonBlock.class);
            BlockBreakNode blockBreak = new BlockBreakNode();
            LocationOffsetNode location = new LocationOffsetNode();
            GetBlockFromLocationNode getBlock = new GetBlockFromLocationNode();

            forEach.mapInput("shape", shapeIn.getOutput("value"));
            forEach.setBode(set);
            location.mapInput("offset", forEach.getOutput("next"));
            blockBreak.mapInput("block", target.getOutput("value"));
            location.mapInput("location", blockBreak.getOutput("location"));
            getBlock.mapInput("location", location.getOutput("result"));
            set.mapInput("targetBlock", getBlock.getOutput("block"));
            set.mapInput("material", getMaterial.getOutput("value"));

            IChainableNodeGraph brush = new ChainableNodeGraph("voxel");
            brush.setStartNode(forEach);
            IGraphCompiler compiler = classloader.getCompiler(IBrush.class);
            @SuppressWarnings("unchecked")
            Class<? extends IBrush> compiled = (Class<? extends IBrush>) compiler.compile(classloader, brush);
            Gunsmith.getGlobalBrushManager().loadBrush("material", compiled);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create material brush part");
        }
    }

}
