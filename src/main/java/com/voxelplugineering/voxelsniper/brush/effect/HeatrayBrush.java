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

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.brush.Brush;
import com.voxelplugineering.voxelsniper.brush.BrushInfo;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.brush.ExecutionResult;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.shape.ComplexMaterialShape;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.brush.BrushVarsHelper;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;
import com.voxelplugineering.voxelsniper.world.queue.ShapeChangeQueue;

import com.google.common.base.Optional;

import java.util.Random;

@BrushInfo(name = "heatray", type = BrushPartType.EFFECT, help = "Bringing destruction since 2010", permission = "voxelsniper.brush.heatray")
public class HeatrayBrush extends Brush
{

    private MaterialState air;
    private MaterialState fire;
    private MaterialState obsidian;
    private MaterialState cobble;

    @Override
    public void init(Context context)
    {
        MaterialRegistry<?> registry = context.getRequired(MaterialRegistry.class);
        this.air = registry.getAirMaterial().getDefaultState();
        this.fire = registry.getMaterial("fire").get().getDefaultState();
        this.obsidian = registry.getMaterial("obsidian").get().getDefaultState();
        this.cobble = registry.getMaterial("cobblestone").get().getDefaultState();
    }

    @Override
    public ExecutionResult run(Player player, BrushVars args)
    {
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingShape, "material");
            return ExecutionResult.abortExecution();
        }
        Optional<MaterialState> m = args.get(BrushKeys.MATERIAL, MaterialState.class);
        if (!m.isPresent())
        {
            player.sendMessage(VoxelSniperConfiguration.missingMaterial);
            return ExecutionResult.abortExecution();
        }
        Optional<Block> l = BrushVarsHelper.getTargetBlock(args);
        Location loc = l.get().getLocation();
        ComplexMaterialShape ms = new ComplexMaterialShape(s.get(), m.get());
        Random rand = new Random();

        for (int x = 0; x < ms.getWidth(); x++)
        {
            int x0 = loc.getFlooredX() + x - ms.getOrigin().getX();
            for (int y = 0; y < ms.getHeight(); y++)
            {
                int y0 = loc.getFlooredY() + y - ms.getOrigin().getY();
                for (int z = 0; z < ms.getLength(); z++)
                {
                    int z0 = loc.getFlooredZ() + z - ms.getOrigin().getZ();
                    if (!ms.get(x, y, z, false))
                    {
                        continue;
                    }
                    Optional<Block> oblock = player.getWorld().getBlock(x0, y0, z0);
                    if (!oblock.isPresent())
                    {
                        GunsmithLogger.getLogger().info("Failed to get block, unsetting");
                        ms.unset(x, y, z, false);
                        continue;
                    }
                    MaterialState mat = oblock.get().getMaterial();
                    if (mat.getType().isLiquid())
                    {
                        ms.setMaterial(x, y, z, false, this.air);
                        continue;
                    }
                    if (mat.getType().isFlamable())
                    {
                        ms.setMaterial(x, y, z, false, this.fire);
                        continue;
                    }
                    if (!mat.getType().equals(this.air.getType()))
                    {
                        if (rand.nextGaussian() >= VoxelSniperConfiguration.obsidianDensity)
                        {
                            if (!mat.getType().equals(this.obsidian.getType()))
                            {
                                ms.setMaterial(x, y, z, false, this.obsidian);
                                continue;
                            }
                        } else if (rand.nextGaussian() >= VoxelSniperConfiguration.cobbleDensity)
                        {
                            if (!mat.getType().equals(this.cobble.getType()))
                            {
                                ms.setMaterial(x, y, z, false, this.cobble);
                                continue;
                            }
                        } else if (rand.nextGaussian() >= VoxelSniperConfiguration.fireDensity)
                        {
                            if (!mat.getType().equals(this.fire.getType()))
                            {
                                ms.setMaterial(x, y, z, false, this.fire);
                                continue;
                            }
                        } else if (rand.nextGaussian() >= VoxelSniperConfiguration.airDensity)
                        {
                            if (!mat.getType().equals(this.air.getType()))
                            {
                                ms.setMaterial(x, y, z, false, this.air);
                                continue;
                            }
                        }
                    }
                    ms.unset(x, y, z, false);
                }
            }
        }
        new ShapeChangeQueue(player, l.get().getLocation(), ms).flush();
        return ExecutionResult.continueExecution();
    }

}
