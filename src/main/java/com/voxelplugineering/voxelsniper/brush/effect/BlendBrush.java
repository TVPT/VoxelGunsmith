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

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.brush.AbstractBrush;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.ComplexMaterialShape;
import com.voxelplugineering.voxelsniper.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.shape.Shape;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.World;
import com.voxelplugineering.voxelsniper.world.material.Material;
import com.voxelplugineering.voxelsniper.world.queue.ShapeChangeQueue;

public class BlendBrush extends AbstractBrush
{

    public BlendBrush()
    {
        super("blend", BrushPartType.EFFECT);
    }

    @Override
    public void run(Player player, BrushVars args)
    {
        boolean excludeFluid = true;
        if(args.has(BrushKeys.EXCLUDE_FLUID)) {
            excludeFluid = args.get(BrushKeys.EXCLUDE_FLUID, Boolean.class).get();
        }
        
        Optional<Shape> s = args.get(BrushKeys.SHAPE, Shape.class);
        if (!s.isPresent())
        {
            player.sendMessage("You must have at least one shape brush before your blend brush.");
            return;
        }
        Optional<Material> m = args.get(BrushKeys.MATERIAL, Material.class);
        if (!m.isPresent())
        {
            player.sendMessage("You must select a material.");
            return;
        }
        Optional<Block> l = args.get(BrushKeys.TARGET_BLOCK, Block.class);
        MaterialShape ms = new ComplexMaterialShape(s.get(), m.get());

        World world = player.getWorld();
        Location loc = l.get().getLocation();
        Shape shape = s.get();

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
                    Map<Material, Integer> mats = Maps.newHashMapWithExpectedSize(10);
                    for (int a = -1; a <= 1; a++)
                    {
                        for (int b = -1; b <= 1; b++)
                        {
                            for (int c = -1; c <= 1; c++)
                            {
                                if (!(a == 0 && b == 0 && c == 0))
                                {
                                    Material mat = world.getBlock(x0 + a, y0 + b, z0 + c).get().getMaterial();
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
                    int n = 0;
                    Material winner = null;
                    for (Map.Entry<Material, Integer> e : mats.entrySet())
                    {
                        if (e.getValue() > n && !(excludeFluid && e.getKey().isLiquid()))
                        {
                            winner = e.getKey();
                        }
                    }
                    
                    boolean tie = false;

                    for (Map.Entry<Material, Integer> e : mats.entrySet())
                    {
                        if(e.getValue() == n && !(excludeFluid && e.getKey().isLiquid())) {
                            tie = true;
                        }
                    }
                    
                    if(!tie) {
                        ms.setMaterial(x, y, z, false, winner);
                    }
                }
            }
        }

        new ShapeChangeQueue(player, loc, ms).flush();
    }
}
