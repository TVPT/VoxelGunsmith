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
package com.voxelplugineering.voxelsniper.util.defaults;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.google.common.collect.Maps;
import com.thevoxelbox.vsl.nodes.StaticValueNode;
import com.thevoxelbox.vsl.nodes.control.ForEachNode;
import com.thevoxelbox.vsl.nodes.control.IfStatement;
import com.thevoxelbox.vsl.nodes.math.compare.NumberEqualsNode;
import com.thevoxelbox.vsl.nodes.vars.ChainedInputNode;
import com.thevoxelbox.vsl.nodes.vars.ChainedOutputNode;
import com.thevoxelbox.vsl.nodes.vars.VariableGetNode;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.brushes.BrushPartType;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Chunk;
import com.voxelplugineering.voxelsniper.api.world.biome.Biome;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.brushes.BrushNodeGraph;
import com.voxelplugineering.voxelsniper.nodes.block.BlockBreakNode;
import com.voxelplugineering.voxelsniper.nodes.material.MaterialCompareNode;
import com.voxelplugineering.voxelsniper.nodes.shape.DiscShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.FlattenShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.ShapeForEachNode;
import com.voxelplugineering.voxelsniper.nodes.shape.ShapeGetOriginNode;
import com.voxelplugineering.voxelsniper.nodes.shape.ShapeSetNode;
import com.voxelplugineering.voxelsniper.nodes.shape.ShapeUnsetNode;
import com.voxelplugineering.voxelsniper.nodes.shape.SphereShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.ToComplexShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.VoxelDiscShapeNode;
import com.voxelplugineering.voxelsniper.nodes.shape.VoxelShapeNode;
import com.voxelplugineering.voxelsniper.nodes.vector.LocationOffsetNode;
import com.voxelplugineering.voxelsniper.nodes.vector.Vector3iNegationNode;
import com.voxelplugineering.voxelsniper.nodes.world.BlockNeighboursNode;
import com.voxelplugineering.voxelsniper.nodes.world.CountOccurrencesNode;
import com.voxelplugineering.voxelsniper.nodes.world.GetBlockFromLocationNode;
import com.voxelplugineering.voxelsniper.nodes.world.GetOverlappingChunksNode;
import com.voxelplugineering.voxelsniper.nodes.world.RefreshChunkNode;
import com.voxelplugineering.voxelsniper.nodes.world.ShapeMaterialSetNode;
import com.voxelplugineering.voxelsniper.nodes.world.biome.SetBiomeNode;
import com.voxelplugineering.voxelsniper.registry.vsl.ArgumentParsers;

/**
 * In lieu of having flat file brushes this will temporarily serve as a builder
 * for brushes at runtime for debugging during development.
 */
public class DefaultBrushBuilder
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

        { // ball
            VariableGetNode<Double> radius = new VariableGetNode<Double>("brushsize");
            SphereShapeNode shape = new SphereShapeNode(radius.getValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", shape.getShape());

            BrushNodeGraph brush = new BrushNodeGraph("ball", BrushPartType.SHAPE);
            brush.setHelp("Creates a ball shaped volume");
            brush.setNext(shapeOut);
            graphs.put("ball", brush);
        }

        { // biome {type}
            VariableGetNode<Biome> biome = new VariableGetNode<Biome>("biome");
            ChainedInputNode<Shape> shapeIn = new ChainedInputNode<Shape>("shape");
            VariableGetNode<Block> target = new VariableGetNode<Block>("targetBlock");
            FlattenShapeNode flatten = new FlattenShapeNode(shapeIn.getValue());
            BlockBreakNode blockBreak = new BlockBreakNode(target.getValue());
            ShapeForEachNode forEach = new ShapeForEachNode(flatten.getFlattenedShape(), true);
            LocationOffsetNode offset = new LocationOffsetNode(blockBreak.getLocation(), forEach.getNextValue());
            SetBiomeNode setBiome = new SetBiomeNode(offset.getOffsetLocation(), biome.getValue());
            GetOverlappingChunksNode overlap = new GetOverlappingChunksNode(shapeIn.getValue(), blockBreak.getLocation());
            ForEachNode<Chunk> chunkForEach = new ForEachNode<Chunk>(overlap.getChunks());
            RefreshChunkNode refresh = new RefreshChunkNode(chunkForEach.getNextValue());

            flatten.setNext(forEach);
            forEach.setBody(offset);
            offset.setNext(setBiome);
            forEach.setNext(overlap);
            overlap.setNext(chunkForEach);
            chunkForEach.setBody(refresh);

            BrushNodeGraph brush = new BrushNodeGraph("biome", BrushPartType.EFFECT);
            brush.addArgument("biome", ArgumentParsers.BIOME_PARSER, Gunsmith.getBiomeRegistry().getDefaultBiome().getName());
            brush.setArgumentAsPrimary("biome");
            String biomes = "";
            for (Biome b : Gunsmith.getBiomeRegistry().getBiomes())
            {
                if (!biomes.isEmpty())
                {
                    biomes += ", ";
                }
                biomes += b.getName();
            }
            String help = "Sets the biome for the world across the flattened area of a shape\nValid biomes are: " + biomes;
            brush.setHelp(help);
            brush.setNext(flatten);
            graphs.put("biome", brush);
        }

        { // TODO blend

        }

        { // TODO blob {growth, recursion}

        }

        { // TODO canyon {min, max}

        }

        { // TODO checker

        }

        { // TODO cleansnow

        }

        { // TODO comet

        }

        { // TODO cylinder {face}

        }

        { // disc {face}
          // TODO support face arg
            VariableGetNode<Double> radius = new VariableGetNode<Double>("brushsize");
            DiscShapeNode shape = new DiscShapeNode(radius.getValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", shape.getShape());

            BrushNodeGraph brush = new BrushNodeGraph("disc", BrushPartType.SHAPE);
            brush.setHelp("Creates a disc shaped area");
            brush.setNext(shapeOut);
            graphs.put("disc", brush);
        }

        { // TODO dome {face}

        }

        { // TODO drain

        }

        { // TODO ellipse {face}

        }

        { // TODO ellipsoid
            
        }

        { // TODO entity {type}

        }

        { // TODO entityremoval {type}

        }

        { // TODO erosion {erode, erodeRecursions, fill, fillRecursions}

        }

        { // TODO filldown {from-existing}

        }

        { // TODO heatray

        }

        { // TODO lightning

        }

        { // TODO line

        }

        { // TODO ocean {min, max}

        }

        { // TODO overlay {height, face}

        }

        { // TODO pull {height}

        }

        { // TODO random {weight}

        }

        { // TODO ring {inner, face}

        }

        { // TODO torus {inner, height, face}

        }

        { // TODO rotation {yaw, pitch, roll}

        }

        { // TODO set

        }

        { // shell
            // @formatter:off
            ChainedInputNode<Shape> shapeInput = new ChainedInputNode<Shape>("shape");
            ToComplexShapeNode complexify = new ToComplexShapeNode(shapeInput.getValue());
            ShapeGetOriginNode origin = new ShapeGetOriginNode(complexify.getComplexShape());
            Vector3iNegationNode negorigin = new Vector3iNegationNode(origin.getOrigin());
            VariableGetNode<Material> maskMaterial = new VariableGetNode<Material>("maskmaterial");
            VariableGetNode<Block> target = new VariableGetNode<Block>("targetblock");
            BlockBreakNode blockBreak = new BlockBreakNode(target.getValue());
            ShapeForEachNode forEach = new ShapeForEachNode(complexify.getComplexShape(), false);
            ShapeUnsetNode unset = new ShapeUnsetNode(complexify.getComplexShape(), forEach.getNextValue());
            LocationOffsetNode offset = new LocationOffsetNode(blockBreak.getLocation(), forEach.getNextValue());
            LocationOffsetNode offset2 = new LocationOffsetNode(offset.getOffsetLocation(), negorigin.getNegativeVector());
            GetBlockFromLocationNode getBlock = new GetBlockFromLocationNode(offset2.getOffsetLocation());
            BlockNeighboursNode neighboursNode = new BlockNeighboursNode(getBlock.getBlock());
            CountOccurrencesNode countNode = new CountOccurrencesNode(maskMaterial.getValue(), neighboursNode.getNeighbours());
            StaticValueNode<Integer> valueNode = new StaticValueNode<Integer>(0);
            NumberEqualsNode numberNode = new NumberEqualsNode(valueNode.getValue(), countNode.getCount(), false);
            IfStatement ifStatement = new IfStatement(numberNode.getComparisonResult());

            ShapeSetNode set = new ShapeSetNode(complexify.getComplexShape(), forEach.getNextValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", complexify.getComplexShape());
            // @formatter:on
            forEach.setBody(unset);
            unset.setNext(offset);
            offset.setNext(offset2);
            offset2.setNext(getBlock);
            getBlock.setNext(neighboursNode);
            neighboursNode.setNext(countNode);
            countNode.setNext(numberNode);
            numberNode.setNext(ifStatement);
            ifStatement.setBody(set);
            forEach.setNext(shapeOut);

            BrushNodeGraph brush = new BrushNodeGraph("shell", BrushPartType.MASK);
            brush.setNext(forEach);
            graphs.put("shell", brush);
        }

        { // material set
            ChainedInputNode<Shape> shapeIn = new ChainedInputNode<Shape>("shape");
            VariableGetNode<Material> getMaterial = new VariableGetNode<Material>("setmaterial");
            VariableGetNode<Block> target = new VariableGetNode<Block>("targetblock");

            BlockBreakNode blockBreak = new BlockBreakNode(target.getValue());

            ShapeMaterialSetNode setMaterial = new ShapeMaterialSetNode(shapeIn.getValue(), getMaterial.getValue(), blockBreak.getLocation());

            BrushNodeGraph brush = new BrushNodeGraph("material", BrushPartType.EFFECT);
            brush.setNext(setMaterial);
            graphs.put("material", brush);
        }

        { // material mask
            // @formatter:off
            ChainedInputNode<Shape> shapeInput = new ChainedInputNode<Shape>("shape");
            ToComplexShapeNode complexify = new ToComplexShapeNode(shapeInput.getValue());
            ShapeGetOriginNode origin = new ShapeGetOriginNode(complexify.getComplexShape());
            Vector3iNegationNode negorigin = new Vector3iNegationNode(origin.getOrigin());
            VariableGetNode<Material> maskMaterial = new VariableGetNode<Material>("maskmaterial");
            VariableGetNode<Block> target = new VariableGetNode<Block>("targetblock");
            BlockBreakNode blockBreak = new BlockBreakNode(target.getValue());
            ShapeForEachNode forEach = new ShapeForEachNode(complexify.getComplexShape(), false);
            ShapeUnsetNode unset = new ShapeUnsetNode(complexify.getComplexShape(), forEach.getNextValue());
            LocationOffsetNode offset = new LocationOffsetNode(blockBreak.getLocation(), forEach.getNextValue());
            LocationOffsetNode offset2 = new LocationOffsetNode(offset.getOffsetLocation(), negorigin.getNegativeVector());
            GetBlockFromLocationNode getBlock = new GetBlockFromLocationNode(offset2.getOffsetLocation());
            BlockBreakNode blockBreak2 = new BlockBreakNode(getBlock.getBlock());
            MaterialCompareNode compare = new MaterialCompareNode(maskMaterial.getValue(), blockBreak2.getMaterial());
            ShapeSetNode set = new ShapeSetNode(complexify.getComplexShape(), forEach.getNextValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", complexify.getComplexShape());
            // @formatter:on
            forEach.setBody(unset);
            unset.setNext(offset);
            offset.setNext(offset2);
            offset2.setNext(getBlock);
            getBlock.setNext(blockBreak2);
            blockBreak2.setNext(compare);
            compare.setBody(set);
            forEach.setNext(shapeOut);

            BrushNodeGraph brush = new BrushNodeGraph("materialmask", BrushPartType.MASK);
            brush.setNext(forEach);
            graphs.put("materialmask", brush);
        }

        { // Snipe brush
            StaticValueNode<Double> radius = new StaticValueNode<Double>(0.0);
            VoxelShapeNode shape = new VoxelShapeNode(radius.getValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", shape.getShape());

            BrushNodeGraph brush = new BrushNodeGraph("snipe", BrushPartType.SHAPE);
            brush.setNext(shapeOut);
            graphs.put("snipe", brush);
        }

        { // TODO snowcone

        }

        { // TODO splatter {seed, growth, recursion}

        }

        { // TODO spline

        }

        { // TODO stencil {name, rotate, flip}

        }

        { // TODO stencillist {name, rotate, flip}

        }

        { // TODO tree {type}

        }

        { // TODO triangle

        }

        { // voxel
            VariableGetNode<Double> radius = new VariableGetNode<Double>("brushsize");
            VoxelShapeNode shape = new VoxelShapeNode(radius.getValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", shape.getShape());

            BrushNodeGraph brush = new BrushNodeGraph("voxel", BrushPartType.SHAPE);
            brush.setNext(shapeOut);
            graphs.put("voxel", brush);
        }

        { // voxeldisc {face}
          // TODO support face arg
            VariableGetNode<Double> radius = new VariableGetNode<Double>("brushsize");
            VoxelDiscShapeNode shape = new VoxelDiscShapeNode(radius.getValue());
            ChainedOutputNode<Shape> shapeOut = new ChainedOutputNode<Shape>("shape", shape.getShape());

            BrushNodeGraph brush = new BrushNodeGraph("voxeldisc", BrushPartType.SHAPE);
            brush.setNext(shapeOut);
            graphs.put("voxeldisc", brush);
        }

        { // TODO 3-point circle

        }

        { // TODO populator {type}

        }
    }

}
