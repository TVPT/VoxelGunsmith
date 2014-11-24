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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.thevoxelbox.vsl.api.IChainableNodeGraph;
import com.thevoxelbox.vsl.api.IGraphCompiler;
import com.thevoxelbox.vsl.classloader.ASMClassLoader;
import com.thevoxelbox.vsl.node.ChainableNodeGraph;
import com.thevoxelbox.vsl.node.variables.ChainedInputNode;
import com.thevoxelbox.vsl.node.variables.ChainedOutputNode;
import com.thevoxelbox.vsl.node.variables.VariableGetNode;
import com.thevoxelbox.vsl.type.Type;
import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.common.CommonBlock;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;
import com.voxelplugineering.voxelsniper.common.FileBrushLoader;
import com.voxelplugineering.voxelsniper.nodes.BlockBreakNode;
import com.voxelplugineering.voxelsniper.nodes.FlushPlayerQueueNode;
import com.voxelplugineering.voxelsniper.nodes.GetBlockFromLocationNode;
import com.voxelplugineering.voxelsniper.nodes.LocationOffsetNode;
import com.voxelplugineering.voxelsniper.nodes.MaterialSetNode;
import com.voxelplugineering.voxelsniper.nodes.ShapeForEachNode;
import com.voxelplugineering.voxelsniper.nodes.SphereShapeNode;
import com.voxelplugineering.voxelsniper.nodes.VoxelShapeNode;
import com.voxelplugineering.voxelsniper.shape.Shape;

/**
 * In lieu of having flat file brushes this will temporarily serve as a builder for brushes at runtime for debugging during development.
 */
public class TemporaryBrushBuilder
{

    /**
     * A map of all graphs created from this temporary utility.
     */
    private static final Map<String, IChainableNodeGraph> graphs = new HashMap<String, IChainableNodeGraph>();

    /**
     * Loads all the graphs from this utility into the global brush manager.
     */
    public static void loadAll()
    {
        ASMClassLoader classloader = Gunsmith.getGlobalBrushManager().getClassLoader();
        for (String name : graphs.keySet())
        {
            try
            {
                IChainableNodeGraph brush = graphs.get(name);
                IGraphCompiler compiler = classloader.getCompiler(IBrush.class);
                @SuppressWarnings("unchecked")
                Class<? extends IBrush> compiled = (Class<? extends IBrush>) compiler.compile(classloader, brush);
                Gunsmith.getGlobalBrushManager().loadBrush(name, compiled);
            } catch (Exception e)
            {
                Gunsmith.getLogger().error(e, "Error while compiling brush " + name + ": " + e.getClass().getName() + " " + e.getMessage());
                continue;
            }
        }
    }

    /**
     * Saves all the brushes from this utility into the given directory.
     * 
     * @param directory the directory to store the brush files in.
     */
    public static void saveAll(File directory)
    {
        if (!directory.exists())
        {
            directory.mkdirs();
        }
        for (String name : graphs.keySet())
        {
            DataOutputStream output = null;
            try
            {
                IChainableNodeGraph brush = graphs.get(name);
                File f = new File(directory, brush.getName() + ".br");
                if (f.exists())
                {
                    f.delete();
                }
                f.createNewFile();
                output = new DataOutputStream(new FileOutputStream(f));
                output.writeInt(FileBrushLoader.BRUSH_FILE_FORMAT_VERSION);
                output.writeInt(0);
                output.flush();
                ObjectOutputStream objOut = new ObjectOutputStream(output);
                objOut.writeObject(brush);
                objOut.flush();
                objOut.close();

            } catch (Exception e)
            {
                Gunsmith.getLogger().warn("Error saving brush " + name + ": " + e.getClass().getName() + " " + e.getMessage());
            } finally
            {
                if (output != null)
                {
                    try
                    {
                        output.close();
                    } catch (IOException ignored)
                    {
                        assert true;
                    }
                }
            }
        }
    }

    /**
     * Creates and loads all brushes into the blobal brush manager.
     */
    public static void buildBrushes()
    {

        try
        { //ball
            VariableGetNode radius = new VariableGetNode("brushSize", Type.FLOAT);
            SphereShapeNode shape = new SphereShapeNode();
            shape.mapInput("radius", radius.getOutput("value"));
            ChainedOutputNode shapeOut = new ChainedOutputNode("shape", Shape.SHAPE_TYPE);
            shapeOut.mapInput("value", shape.getOutput("shape"));

            IChainableNodeGraph brush = new ChainableNodeGraph("ball");
            brush.setStartNode(shapeOut);
            graphs.put("ball", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create ball brush part");
        }

        try
        { //TODO biome
            ChainedInputNode shapeIn = new ChainedInputNode("shape", Shape.SHAPE_TYPE);
            ShapeForEachNode forEach = new ShapeForEachNode();
            MaterialSetNode set = new MaterialSetNode();
            VariableGetNode getMaterial = new VariableGetNode("setMaterial", CommonMaterial.COMMONMATERIAL_TYPE);
            VariableGetNode target = new VariableGetNode("targetBlock", CommonBlock.COMMONBLOCK_TYPE);
            BlockBreakNode blockBreak = new BlockBreakNode();
            LocationOffsetNode location = new LocationOffsetNode();
            GetBlockFromLocationNode getBlock = new GetBlockFromLocationNode();

            forEach.mapInput("shape", shapeIn.getOutput("value"));
            forEach.setBody(set);
            location.mapInput("offset", forEach.getOutput("next"));
            blockBreak.mapInput("block", target.getOutput("value"));
            location.mapInput("location", blockBreak.getOutput("location"));
            getBlock.mapInput("location", location.getOutput("result"));
            set.mapInput("targetBlock", getBlock.getOutput("block"));
            set.mapInput("material", getMaterial.getOutput("value"));

            IChainableNodeGraph brush = new ChainableNodeGraph("biome");
            brush.setStartNode(forEach);
            //graphs.put("biome", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create material brush part");
        }

        try
        { //Snipe brush
            VariableGetNode getTarget = new VariableGetNode("targetBlock", CommonBlock.COMMONBLOCK_TYPE);
            VariableGetNode getMaterial = new VariableGetNode("setMaterial", CommonMaterial.COMMONMATERIAL_TYPE);
            MaterialSetNode set = new MaterialSetNode();
            set.mapInput("targetBlock", getTarget.getOutput("value"));
            set.mapInput("material", getMaterial.getOutput("value"));

            IChainableNodeGraph brush = new ChainableNodeGraph("snipe");
            brush.setStartNode(set);
            graphs.put("snipe", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create snipe brush: " + e.getMessage());
        }

        try
        { //Voxel shape brush part
            VariableGetNode radius = new VariableGetNode("brushSize", Type.FLOAT);
            VoxelShapeNode shape = new VoxelShapeNode();
            shape.mapInput("radius", radius.getOutput("value"));
            ChainedOutputNode shapeOut = new ChainedOutputNode("shape", Shape.SHAPE_TYPE);
            shapeOut.mapInput("value", shape.getOutput("shape"));

            IChainableNodeGraph brush = new ChainableNodeGraph("voxel");
            brush.setStartNode(shapeOut);
            graphs.put("voxel", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create voxel brush part");
        }

        try
        { //shape set material
            ChainedInputNode shapeIn = new ChainedInputNode("shape", Shape.SHAPE_TYPE);
            ShapeForEachNode forEach = new ShapeForEachNode();
            MaterialSetNode set = new MaterialSetNode();
            VariableGetNode getMaterial = new VariableGetNode("setMaterial", CommonMaterial.COMMONMATERIAL_TYPE);
            VariableGetNode target = new VariableGetNode("targetBlock", CommonBlock.COMMONBLOCK_TYPE);
            BlockBreakNode blockBreak = new BlockBreakNode();
            LocationOffsetNode location = new LocationOffsetNode();
            GetBlockFromLocationNode getBlock = new GetBlockFromLocationNode();
            FlushPlayerQueueNode flush = new FlushPlayerQueueNode();

            forEach.mapInput("shape", shapeIn.getOutput("value"));
            forEach.setBody(set);
            location.mapInput("offset", forEach.getOutput("next"));
            blockBreak.mapInput("block", target.getOutput("value"));
            location.mapInput("location", blockBreak.getOutput("location"));
            getBlock.mapInput("location", location.getOutput("result"));
            set.mapInput("targetBlock", getBlock.getOutput("block"));
            set.mapInput("material", getMaterial.getOutput("value"));
            forEach.setNextNode(flush);

            IChainableNodeGraph brush = new ChainableNodeGraph("material");
            brush.setStartNode(forEach);
            graphs.put("material", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create material brush part");
        }
    }

}
