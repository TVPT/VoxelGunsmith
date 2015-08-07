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
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.util.brush.BrushVarsHelper;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Chunk;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.World;
import com.voxelplugineering.voxelsniper.world.biome.Biome;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * An effect brush which sets your shape area to your specified biome.
 */
@BrushInfo(name = "biome", type = BrushPartType.EFFECT, help = "An effect brush which sets your shape area to the specified biome", params = {
        @BrushParam(name = BrushKeys.BIOME, desc = "The biome") })
public class BiomeBrush implements Brush
{

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingShape, "biome");
            return ExecutionResult.abortExecution();
        }
        Shape shape = s.get();
        Optional<Block> l = BrushVarsHelper.getTargetBlock(args);
        Location loc = l.get().getLocation();
        World world = player.getWorld();
        Optional<Biome> b = args.get(BrushKeys.BIOME, Biome.class);
        if (!b.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingParam, BrushKeys.BIOME, "biome");
            return ExecutionResult.abortExecution();
        }
        List<Chunk> toUpdate = Lists.newArrayList();
        for (int x = 0; x < shape.getWidth(); x++)
        {
            int x0 = loc.getFlooredX() + x - shape.getOrigin().getX();
            for (int z = 0; z < shape.getLength(); z++)
            {
                int z0 = loc.getFlooredZ() + z - shape.getOrigin().getZ();
                for (int y = 0; y < shape.getHeight(); y++)
                {
                    int y0 = loc.getFlooredY() + y - shape.getOrigin().getY();
                    if (shape.get(x, y, z, false))
                    {
                        world.setBiome(b.get(), x0, y0, z0);
                        Chunk chunk = world.getChunk(x0 / 16, y0 / 16, z0 / 16).orNull();
                        if (chunk != null && !toUpdate.contains(chunk))
                        {
                            toUpdate.add(chunk);
                        }
                        break;
                    }
                }
            }
        }
        for (Chunk c : toUpdate)
        {
            c.refreshChunk();
        }
        return ExecutionResult.continueExecution();
    }

}
