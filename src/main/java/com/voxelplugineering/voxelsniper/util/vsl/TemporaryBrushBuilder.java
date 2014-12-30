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
package com.voxelplugineering.voxelsniper.util.vsl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.google.common.collect.Maps;
import com.thevoxelbox.vsl.nodes.StaticValueNode;
import com.thevoxelbox.vsl.nodes.vars.ChainedInputNode;
import com.thevoxelbox.vsl.nodes.vars.ChainedOutputNode;
import com.thevoxelbox.vsl.nodes.vars.VariableGetNode;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.brushes.BrushNodeGraph;
import com.voxelplugineering.voxelsniper.nodes.block.BlockBreakNode;
import com.voxelplugineering.voxelsniper.nodes.material.MaterialCompareNode;
import com.voxelplugineering.voxelsniper.nodes.shape.DiscShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.FlattenShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.ShapeForEachNode;
import com.voxelplugineering.voxelsniper.nodes.shape.ShapeSetNode;
import com.voxelplugineering.voxelsniper.nodes.shape.ShapeUnsetNode;
import com.voxelplugineering.voxelsniper.nodes.shape.SphereShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.VoxelShapeNode;
import com.voxelplugineering.voxelsniper.nodes.vector.LocationOffsetNode;
import com.voxelplugineering.voxelsniper.nodes.world.GetBlockFromLocationNode;
import com.voxelplugineering.voxelsniper.nodes.world.ShapeMaterialSetNode;
import com.voxelplugineering.voxelsniper.nodes.world.biome.GetBiomeNode;
import com.voxelplugineering.voxelsniper.nodes.world.biome.SetBiomeNode;
import com.voxelplugineering.voxelsniper.shape.Shape;

/**
 * In lieu of having flat file brushes this will temporarily serve as a builder for brushes at runtime for debugging during development.
 */
public class TemporaryBrushBuilder
{

    /**
     * A map of all graphs created from this temporary utility.
     */
    private static final Map<String, BrushNodeGraph> graphs = Maps.newHashMap();

    /**
     * Loads all the graphs from this utility into the global brush manager.
     * 
     * @param manager the brush manager to load the brushes into
     */
    public static void loadAll(BrushManager manager)
    {
        for (String name : graphs.keySet())
        {
            BrushNodeGraph brush = graphs.get(name);
            manager.loadBrush(name, brush);
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
                BrushNodeGraph brush = graphs.get(name);
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
        { //ball
            VariableGetNode<Double> radius = new VariableGetNode<Double>("brushsize");
            SphereShapeNode shape = new SphereShapeNode(radius.getValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", shape.getShape());

            BrushNodeGraph brush = new BrushNodeGraph("ball");
            brush.setNext(shapeOut);
            graphs.put("ball", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create ball brush part");
        }

        try
        { //biome
            VariableGetNode<String> biomeName = new VariableGetNode<String>("biome");
            GetBiomeNode biome = new GetBiomeNode(biomeName.getValue());
            ChainedInputNode<Shape> shapeIn = new ChainedInputNode<Shape>("shape");
            VariableGetNode<Block> target = new VariableGetNode<Block>("targetBlock");
            FlattenShapeNode flatten = new FlattenShapeNode(shapeIn.getValue());
            BlockBreakNode blockBreak = new BlockBreakNode(target.getValue());
            ShapeForEachNode forEach = new ShapeForEachNode(flatten.getFlattenedShape());
            LocationOffsetNode offset = new LocationOffsetNode(blockBreak.getLocation(), forEach.getNextValue());
            SetBiomeNode setBiome = new SetBiomeNode(offset.getOffsetLocation(), biome.getBiome());

            flatten.setNext(forEach);
            forEach.setBody(offset);
            offset.setNext(setBiome);

            BrushNodeGraph brush = new BrushNodeGraph("biome");
            brush.setNext(flatten);
            graphs.put("biome", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create material brush part");
        }

        try
        { //disc
            VariableGetNode<Double> radius = new VariableGetNode<Double>("brushsize");
            DiscShapeNode shape = new DiscShapeNode(radius.getValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", shape.getShape());

            BrushNodeGraph brush = new BrushNodeGraph("disc");
            brush.setNext(shapeOut);
            graphs.put("disc", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create disc brush part");
        }

        try
        { //Snipe brush
            StaticValueNode<Double> radius = new StaticValueNode<Double>(0.0);
            VoxelShapeNode shape = new VoxelShapeNode(radius.getValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", shape.getShape());

            BrushNodeGraph brush = new BrushNodeGraph("snipe");
            brush.setNext(shapeOut);
            graphs.put("snipe", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create snipe brush: " + e.getMessage());
        }

        try
        { //Voxel shape brush part
            VariableGetNode<Double> radius = new VariableGetNode<Double>("brushsize");
            VoxelShapeNode shape = new VoxelShapeNode(radius.getValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", shape.getShape());

            BrushNodeGraph brush = new BrushNodeGraph("voxel");
            brush.setNext(shapeOut);
            graphs.put("voxel", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create voxel brush part");
        }

        try
        { //material set
            ChainedInputNode<Shape> shapeIn = new ChainedInputNode<Shape>("shape");
            VariableGetNode<Material> getMaterial = new VariableGetNode<Material>("setmaterial");
            VariableGetNode<Block> target = new VariableGetNode<Block>("targetblock");

            BlockBreakNode blockBreak = new BlockBreakNode(target.getValue());

            ShapeMaterialSetNode setMaterial = new ShapeMaterialSetNode(shapeIn.getValue(), getMaterial.getValue(), blockBreak.getLocation());

            BrushNodeGraph brush = new BrushNodeGraph("material");
            brush.setNext(setMaterial);
            graphs.put("material", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create material brush part");
        }

        try
        { //material mask
            ChainedInputNode<Shape> shapeIn = new ChainedInputNode<Shape>("shape");
            VariableGetNode<Material> maskMaterial = new VariableGetNode<Material>("maskmaterial");
            VariableGetNode<Block> target = new VariableGetNode<Block>("targetblock");
            BlockBreakNode blockBreak = new BlockBreakNode(target.getValue());
            ShapeForEachNode forEach = new ShapeForEachNode(shapeIn.getValue());
            ShapeUnsetNode unset = new ShapeUnsetNode(shapeIn.getValue(), forEach.getNextValue());
            LocationOffsetNode offset = new LocationOffsetNode(blockBreak.getLocation(), forEach.getNextValue());
            GetBlockFromLocationNode getBlock = new GetBlockFromLocationNode(offset.getOffsetLocation());
            BlockBreakNode blockBreak2 = new BlockBreakNode(getBlock.getBlock());
            MaterialCompareNode compare = new MaterialCompareNode(maskMaterial.getValue(), blockBreak2.getMaterial());
            ShapeSetNode set = new ShapeSetNode(shapeIn.getValue(), forEach.getNextValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", shapeIn.getValue());

            forEach.setBody(unset);
            unset.setNext(offset);
            offset.setNext(getBlock);
            getBlock.setNext(blockBreak2);
            blockBreak2.setNext(compare);
            compare.setBody(set);
            forEach.setNext(shapeOut);

            BrushNodeGraph brush = new BrushNodeGraph("materialmask");
            brush.setNext(forEach);
            graphs.put("materialmask", brush);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create materialmask brush part");
        }
    }

}
