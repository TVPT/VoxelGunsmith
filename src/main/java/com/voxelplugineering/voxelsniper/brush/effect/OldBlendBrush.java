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
package com.voxelplugineering.voxelsniper.brush.effect;

import com.voxelplugineering.voxelsniper.brush.Brush;
import com.voxelplugineering.voxelsniper.brush.BrushInfo;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushParam;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.brush.ExecutionResult;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.ComplexMaterialShape;
import com.voxelplugineering.voxelsniper.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.shape.csg.CuboidShape;
import com.voxelplugineering.voxelsniper.shape.csg.PrimativeShapeFactory;
import com.voxelplugineering.voxelsniper.util.math.Maths;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.World;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;
import com.voxelplugineering.voxelsniper.world.queue.ShapeChangeQueue;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * The blend brush. An effect brush which performs a 'blend' operation by setting blocks in the
 * defined region to the mode material of the surrounding materials.
 */
@BrushInfo(name = "blend", type = BrushPartType.EFFECT, help = OldBlendBrush.HELP, params = {
        @BrushParam(name = BrushKeys.EXCLUDE_FLUID, desc = "Whether to exclude fluids (true/false)") })
public class OldBlendBrush extends Brush
{

    protected static final String HELP = "An effect brush which performs a 'blend' operation by setting blocks in the defined region to the "
            + "most common material of the surrounding materials";

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        boolean excludeFluid = true;
        if (args.has(BrushKeys.EXCLUDE_FLUID))
        {
            excludeFluid = args.get(BrushKeys.EXCLUDE_FLUID, Boolean.class).get();
        }

        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingShape, "blend");
            return ExecutionResult.abortExecution();
        }
        Optional<MaterialState> m = args.get(BrushKeys.MATERIAL, MaterialState.class);
        if (!m.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingMaterial);
            return ExecutionResult.abortExecution();
        }

        Optional<String> kernalShape = args.get(BrushKeys.KERNEL, String.class);
        Optional<Double> kernalSize = args.get(BrushKeys.KERNEL_SIZE, Double.class);
        double size = kernalSize.or(VoxelSniperConfiguration.blendDefaultKernalSize);
        String kernelString = kernalShape.or(VoxelSniperConfiguration.blendDefaultKernalShape);
        Optional<Shape> se = PrimativeShapeFactory.createShape(kernelString, size);
        if (!se.isPresent())
        {
            se = Optional.<Shape>of(new CuboidShape(3, 3, 3, new Vector3i(1, 1, 1)));
        }

        Optional<Block> l = args.get(BrushKeys.TARGET_BLOCK, Block.class);
        MaterialShape ms = new ComplexMaterialShape(s.get(), m.get());

        World world = player.getWorld();
        Location loc = l.get().getLocation();
        Shape shape = s.get();
        Shape structElem = se.get();

        // Extract the location in the world to x0, y0 and z0.
        for (int x = 0; x < ms.getWidth(); x++)
        {
            int x0 = loc.getFlooredX() + x - shape.getOrigin().getX();
            for (int y = 0; y < ms.getHeight(); y++)
            {
                int y0 = loc.getFlooredY() + y - shape.getOrigin().getY();
                for (int z = 0; z < ms.getLength(); z++)
                {
                    int z0 = loc.getFlooredZ() + z - shape.getOrigin().getZ();
                    if (!shape.get(x, y, z, false))
                    {
                        continue;
                    }

                    // Represents a histogram of material occurrences hit by the
                    // structuring element.
                    Map<MaterialState, Integer> mats = Maps.newHashMapWithExpectedSize(10);

                    for (int a = 0; a < structElem.getWidth(); a++)
                    {
                        for (int b = 0; b < structElem.getHeight(); b++)
                        {
                            for (int c = 0; c < structElem.getLength(); c++)
                            {
                                if (!structElem.get(a, b, c, false))
                                {
                                    continue;
                                }
                                int a0 = a - structElem.getOrigin().getX();
                                int b0 = b - structElem.getOrigin().getY();
                                int c0 = c - structElem.getOrigin().getZ();
                                // Discludes the target block from the
                                // calculation.
                                if (!(a0 == 0 && b0 == 0 && c0 == 0))
                                {
                                    // TODO: Use world bounds instead of
                                    // hardcoded magical values from Minecraft.
                                    int clampedY = Maths.clamp(y0 + b0, 0, 255);
                                    MaterialState mat = world.getBlock(x0 + a0, clampedY, z0 + c0).get().getMaterial();
                                    if (mats.containsKey(mat))
                                    {
                                        mats.put(mat, mats.get(mat) + 1);
                                    } else
                                    {
                                        mats.put(mat, 1);
                                    }
                                }
                            }
                        }
                    }

                    // Select the material which occured the most.
                    int n = 0;
                    MaterialState winner = null;
                    for (Map.Entry<MaterialState, Integer> e : mats.entrySet())
                    {
                        if (e.getValue() > n && !(excludeFluid && e.getKey().getType().isLiquid()))
                        {
                            winner = e.getKey();
                            n = e.getValue();
                        }
                    }

                    // If multiple materials occurred the most, the tie check
                    // will become true.
                    boolean tie = false;
                    for (Map.Entry<MaterialState, Integer> e : mats.entrySet())
                    {
                        if (e.getValue() == n && !(excludeFluid && e.getKey().getType().isLiquid()) && !e.getKey().equals(winner))
                        {
                            tie = true;
                        }
                    }

                    // If a tie is found, no change is made.
                    if (!tie)
                    {
                        ms.setMaterial(x, y, z, false, winner);
                    }
                }
            }
        }
        new ShapeChangeQueue(player, loc, ms).flush();
        return ExecutionResult.continueExecution();
    }
}
