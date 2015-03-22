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
package com.voxelplugineering.voxelsniper.brushes.natives;

import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.thevoxelbox.vsl.api.variables.VariableHolder;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.brushes.BrushPartType;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.brushes.NativeBrush;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.world.queue.ShapeChangeQueue;

/**
 * A native implementation of the Erode brush.
 * <p>
 * This brush part is an effect which, depending on the settings, loops through
 * the area and for each erode iteration removes all blocks with exposed faces
 * below a threshold, and for each fill iteration adds all blocks above a
 * threshold.
 * </p>
 */
public class ErodeBrush extends NativeBrush
{

    private static Vector3i[] offsets = new Vector3i[] { new Vector3i(1, 0, 0), new Vector3i(-1, 0, 0), new Vector3i(0, 1, 0),
            new Vector3i(0, -1, 0), new Vector3i(0, 0, 1), new Vector3i(0, 0, -1) };

    private final int erode;
    private final int fill;

    /**
     * TODO this whole brush needs to be re built probably split into two
     * 
     * @param erode The threshold of faces above which the block will be removed
     * @param fill Similar as erode but for the fill threshold
     */
    public ErodeBrush(int erode, int fill)
    {
        super("melt", BrushPartType.EFFECT);
        setHelp("melt");
        this.erode = erode;
        this.fill = fill;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(RuntimeState state)
    {
        VariableHolder vars = state.getVars();
        Player player = vars.get("__PLAYER__", Player.class).get();
        World world = player.getWorld();
        Block target = vars.get("targetBlock", Block.class).get();
        Location targetVec = target.getLocation();
        Shape shape = vars.get("__CHAINED__shape", Shape.class).get();
        MaterialShape output = world.getShapeFromWorld(targetVec, shape);
        Location origin = targetVec.add(-shape.getOrigin().getX(), -shape.getOrigin().getY(), -shape.getOrigin().getZ());

        erodeIteration(world, output, origin);
        fillIteration(world, output, origin);

        player.addPending(new ShapeChangeQueue(player, target.getLocation(), output));
    }

    private void fillIteration(World world, MaterialShape out, Location origin)
    {
        for (int x = 0; x < out.getWidth(); ++x)
        {
            for (int z = 0; z < out.getLength(); ++z)
            {
                for (int y = 0; y < out.getHeight(); ++y)
                {
                    if (out.get(x, y, z, false))
                    {
                        if (world.getBlock(origin.add(x, y, z)).get().getMaterial().isLiquid()
                                || world.getBlock(origin.add(x, y, z)).get().getMaterial().isSolid())
                        {
                            continue;
                        }

                        final Map<Material, Integer> blockCount = Maps.newHashMap();
                        int count = 0;

                        for (Vector3i vec : offsets)
                        {
                            Optional<Block> block = world.getBlock(origin.add(x + vec.getX(), y + vec.getY(), z + vec.getZ()));
                            if (block.isPresent())
                            {
                                Material mat = block.get().getMaterial();
                                if (mat.isSolid() || !mat.isLiquid())
                                {
                                    count++;
                                    if (blockCount.containsKey(mat))
                                    {
                                        blockCount.put(mat, blockCount.get(mat) + 1);
                                    } else
                                    {
                                        blockCount.put(mat, 1);
                                    }
                                }
                            }
                        }

                        Material currentMaterial = world.getMaterialRegistry().getAirMaterial();
                        int amount = 0;

                        for (final Material wrapper : blockCount.keySet())
                        {
                            final Integer currentCount = blockCount.get(wrapper);
                            if (amount <= currentCount)
                            {
                                currentMaterial = wrapper;
                                amount = currentCount;
                            }
                        }

                        if (count >= this.fill)
                        {
                            out.setMaterial(x, y, z, false, currentMaterial);
                        }
                    }
                }
            }
        }
    }

    private void erodeIteration(World world, MaterialShape out, Location origin)
    {
        for (int x = 0; x < out.getWidth(); ++x)
        {
            for (int z = 0; z < out.getLength(); ++z)
            {
                for (int y = 0; y < out.getHeight(); ++y)
                {
                    if (out.get(x, y, z, false))
                    {
                        if (world.getBlock(origin.add(x, y, z)).get().getMaterial().isLiquid()
                                || !world.getBlock(origin.add(x, y, z)).get().getMaterial().isSolid())
                        {
                            continue;
                        }

                        int count = 0;

                        for (Vector3i vec : offsets)
                        {
                            Optional<Block> block = world.getBlock(origin.add(x + vec.getX(), y + vec.getY(), z + vec.getZ()));
                            if (block.isPresent())
                            {
                                Material mat = block.get().getMaterial();
                                if (!mat.isSolid() || mat.isLiquid())
                                {
                                    count++;
                                }
                            }
                        }

                        if (count >= this.erode)
                        {
                            out.setMaterial(x, y, z, false, world.getMaterialRegistry().getAirMaterial());
                        }
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "melt";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseArguments(String string, VariableHolder vars)
    {

    }

}
