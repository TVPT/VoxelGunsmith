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
import com.thevoxelbox.vsl.node.variables.ChainedInputNode;
import com.thevoxelbox.vsl.node.variables.ChainedOutputNode;
import com.thevoxelbox.vsl.node.variables.FloatValueNode;
import com.thevoxelbox.vsl.node.variables.VariableGetNode;
import com.thevoxelbox.vsl.type.Type;
import com.thevoxelbox.vsl.type.TypeDepth;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.api.IBrushManager;
import com.voxelplugineering.voxelsniper.nodes.BlockBreakNode;
import com.voxelplugineering.voxelsniper.nodes.GetBlockFromLocationNode;
import com.voxelplugineering.voxelsniper.nodes.LocationOffsetNode;
import com.voxelplugineering.voxelsniper.nodes.MaterialCompareNode;
import com.voxelplugineering.voxelsniper.nodes.SetBiomeNode;
import com.voxelplugineering.voxelsniper.nodes.ShapeForEachNode;
import com.voxelplugineering.voxelsniper.nodes.ShapeMaterialSetNode;
import com.voxelplugineering.voxelsniper.nodes.TestNode;
import com.voxelplugineering.voxelsniper.nodes.shape.DiscShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.FlattenShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.ShapeSetNode;
import com.voxelplugineering.voxelsniper.nodes.shape.ShapeUnsetNode;
import com.voxelplugineering.voxelsniper.nodes.shape.SphereShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.VoxelShapeNode;

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
     * 
     * @param classloader the classloader to use to load the brushes
     * @param manager the brush manager to load the brushes into
     */
    public static void loadAll(ASMClassLoader classloader, IBrushManager manager)
    {
        for (String name : graphs.keySet())
        {
            try
            {
                IChainableNodeGraph brush = graphs.get(name);
                IGraphCompiler compiler = classloader.getCompiler(IBrush.class);
                @SuppressWarnings("unchecked")
                Class<? extends IBrush> compiled = (Class<? extends IBrush>) compiler.compile(classloader, brush);
                manager.loadBrush(name, compiled);
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
                output.writeInt(1);
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
        { //test
            TestNode test = new TestNode();

            IChainableNodeGraph brush = new BrushPartNodeGraph("test");
            brush.setStartNode(test);
            graphs.put("test", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create test brush part");
        }

        try
        { //ball
            VariableGetNode radius = new VariableGetNode("brushSize", Type.FLOAT);
            SphereShapeNode shape = new SphereShapeNode();
            shape.mapInput("radius", radius.getOutput("value"));
            ChainedOutputNode shapeOut = new ChainedOutputNode("shape", Type.getType("SHAPE", TypeDepth.SINGLE));
            shapeOut.mapInput("value", shape.getOutput("shape"));

            IChainableNodeGraph brush = new BrushPartNodeGraph("ball");
            brush.setStartNode(shapeOut);
            graphs.put("ball", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create ball brush part");
        }

        try
        { //TODO biome
            VariableGetNode biome = new VariableGetNode("biome", Type.STRING);
            ChainedInputNode shapeIn = new ChainedInputNode("shape", Type.getType("SHAPE", TypeDepth.SINGLE));
            VariableGetNode target = new VariableGetNode("targetBlock", Type.getType("COMMONBLOCK", TypeDepth.SINGLE));
            FlattenShapeNode flatten = new FlattenShapeNode();
            flatten.mapInput("shape", shapeIn.getOutput("value"));
            
            ShapeForEachNode forEach = new ShapeForEachNode();
            flatten.setNextNode(forEach);
            
            BlockBreakNode blockBreak = new BlockBreakNode();
            blockBreak.mapInput("block", target.getOutput("value"));
            
            LocationOffsetNode offset = new LocationOffsetNode();
            offset.mapInput("location", blockBreak.getOutput("location"));
            offset.mapInput("offset", forEach.getOutput("next"));
            
            SetBiomeNode setBiome = new SetBiomeNode();
            forEach.setBody(setBiome);
            setBiome.mapInput("location", offset.getOutput("result"));
            setBiome.mapInput("biome", biome.getOutput("value"));
            
            BrushPartNodeGraph brush = new BrushPartNodeGraph("biome");
            brush.setStartNode(flatten);
            brush.setRequiredVars("biome");
            graphs.put("biome", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create material brush part");
        }

        try
        { //disc
            VariableGetNode radius = new VariableGetNode("brushSize", Type.FLOAT);
            DiscShapeNode shape = new DiscShapeNode();
            shape.mapInput("radius", radius.getOutput("value"));
            ChainedOutputNode shapeOut = new ChainedOutputNode("shape", Type.getType("SHAPE", TypeDepth.SINGLE));
            shapeOut.mapInput("value", shape.getOutput("shape"));

            IChainableNodeGraph brush = new BrushPartNodeGraph("disc");
            brush.setStartNode(shapeOut);
            graphs.put("disc", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create disc brush part");
        }

        try
        { //Snipe brush
            FloatValueNode radius = new FloatValueNode(0.5);
            VoxelShapeNode shape = new VoxelShapeNode();
            shape.mapInput("radius", radius.getOutput("value"));
            ChainedOutputNode shapeOut = new ChainedOutputNode("shape", Type.getType("SHAPE", TypeDepth.SINGLE));
            shapeOut.mapInput("value", shape.getOutput("shape"));

            IChainableNodeGraph brush = new BrushPartNodeGraph("snipe");
            brush.setStartNode(shapeOut);
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
            ChainedOutputNode shapeOut = new ChainedOutputNode("shape", Type.getType("SHAPE", TypeDepth.SINGLE));
            shapeOut.mapInput("value", shape.getOutput("shape"));

            IChainableNodeGraph brush = new BrushPartNodeGraph("voxel");
            brush.setStartNode(shapeOut);
            graphs.put("voxel", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create voxel brush part");
        }

        try
        { //material set
            ChainedInputNode shapeIn = new ChainedInputNode("shape", Type.getType("SHAPE", TypeDepth.SINGLE));
            VariableGetNode getMaterial = new VariableGetNode("setMaterial", Type.getType("COMMONMATERIAL", TypeDepth.SINGLE));
            VariableGetNode target = new VariableGetNode("targetBlock", Type.getType("COMMONBLOCK", TypeDepth.SINGLE));

            BlockBreakNode blockBreak = new BlockBreakNode();
            blockBreak.mapInput("block", target.getOutput("value"));
            
            ShapeMaterialSetNode setMaterial = new ShapeMaterialSetNode();
            setMaterial.mapInput("target", blockBreak.getOutput("location"));
            setMaterial.mapInput("material", getMaterial.getOutput("value"));
            setMaterial.mapInput("shape", shapeIn.getOutput("value"));

            IChainableNodeGraph brush = new BrushPartNodeGraph("material");
            brush.setStartNode(setMaterial);
            graphs.put("material", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create material brush part");
        }
        
        try
        { //material mask
            ChainedInputNode shapeIn = new ChainedInputNode("shape", Type.getType("SHAPE", TypeDepth.SINGLE));
            VariableGetNode maskMaterial = new VariableGetNode("maskMaterial", Type.getType("COMMONMATERIAL", TypeDepth.SINGLE));
            VariableGetNode target = new VariableGetNode("targetBlock", Type.getType("COMMONBLOCK", TypeDepth.SINGLE));

            ShapeForEachNode foreach = new ShapeForEachNode();
            foreach.mapInput("shape", shapeIn.getOutput("value"));
            
            ShapeUnsetNode unset = new ShapeUnsetNode();
            foreach.setBody(unset);
            unset.mapInput("shape", shapeIn.getOutput("value"));
            unset.mapInput("target", foreach.getOutput("next"));

            BlockBreakNode blockBreak = new BlockBreakNode();
            blockBreak.mapInput("block", target.getOutput("value"));
            
            LocationOffsetNode offset = new LocationOffsetNode();
            offset.mapInput("location", blockBreak.getOutput("location"));
            offset.mapInput("offset", foreach.getOutput("next"));
            
            GetBlockFromLocationNode getBlock = new GetBlockFromLocationNode();
            getBlock.mapInput("location", offset.getOutput("result"));

            BlockBreakNode blockBreak2 = new BlockBreakNode();
            blockBreak2.mapInput("block", getBlock.getOutput("block"));
            
            MaterialCompareNode compare = new MaterialCompareNode();
            compare.mapInput("a", maskMaterial.getOutput("value"));
            compare.mapInput("b", blockBreak2.getOutput("material"));
            unset.setNextNode(compare);
            
            ShapeSetNode set = new ShapeSetNode();
            set.mapInput("shape", shapeIn.getOutput("value"));
            set.mapInput("target", foreach.getOutput("next"));
            compare.setBody(set);
            
            ChainedOutputNode shapeOut = new ChainedOutputNode("shape", Type.getType("SHAPE", TypeDepth.SINGLE));
            shapeOut.mapInput("value", shapeIn.getOutput("value"));
            foreach.setNextNode(shapeOut);

            IChainableNodeGraph brush = new BrushPartNodeGraph("materialmask");
            brush.setStartNode(foreach);
            graphs.put("materialmask", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create materialmask brush part");
        }
    }

}
