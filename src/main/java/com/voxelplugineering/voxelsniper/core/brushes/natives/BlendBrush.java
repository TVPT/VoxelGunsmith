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
package com.voxelplugineering.voxelsniper.core.brushes.natives;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.thevoxelbox.vsl.api.variables.VariableHolder;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.brushes.BrushPartType;
import com.voxelplugineering.voxelsniper.api.entity.Player;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.core.brushes.NativeBrush;
import com.voxelplugineering.voxelsniper.core.world.queue.ShapeChangeQueue;

/**
 * An implementation of the blend brush as a native brush part. <p> This brush part is an effect
 * which performs a 'blending' effect by looping through the area and replacing each block with the
 * most common neighboring block, if a tie is found then no change is made. </p>
 */
public class BlendBrush extends NativeBrush
{

    /**
     * 
     */
    public BlendBrush()
    {
        super("blend", BrushPartType.EFFECT);
        setHelp("blend");
    }

    @Override
    public void run(RuntimeState state)
    {
        checkNotNull(state);
        VariableHolder vars = state.getVars();
        Player player = vars.get("__PLAYER__", Player.class).get();
        World world = player.getWorld();
        Block target = vars.get("targetBlock", Block.class).get();
        Location targetVec = target.getLocation();
        Shape shape = vars.get("__CHAINED__shape", Shape.class).get();
        MaterialShape output = world.getShapeFromWorld(targetVec, shape);
        Map<Material, Integer> freqs = Maps.newHashMap();
        //TODO Copied verbatim from old sniper, cleanup direly needed.
        Location origin = targetVec.add(-shape.getOrigin().getX(), -shape.getOrigin().getY(), -shape.getOrigin().getZ());
        for (int x = 0; x < shape.getWidth(); x++)
        {
            for (int y = 0; y < shape.getHeight(); y++)
            {
                for (int z = 0; z < shape.getLength(); z++)
                {
                    if (!shape.get(x, y, z, false))
                    {
                        continue;
                    }
                    freqs.clear();
                    int modeMatCount = 0;
                    Material modeMat = world.getMaterialRegistry().getAirMaterial();
                    boolean tiecheck = true;

                    for (int m = -1; m <= 1; m++)
                    {
                        for (int n = -1; n <= 1; n++)
                        {
                            for (int o = -1; o <= 1; o++)
                            {
                                if (!(m == 0 && n == 0 && o == 0))
                                {
                                    Optional<Block> block = world.getBlock(origin.add(x + m, y + n, z + o));
                                    if (block.isPresent())
                                    {
                                        Material mat = block.get().getMaterial();
                                        if (freqs.containsKey(mat))
                                        {
                                            freqs.put(mat, freqs.get(mat) + 1);
                                        } else
                                        {
                                            freqs.put(mat, 1);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Find most common neighboring material.
                    for (Map.Entry<Material, Integer> m : freqs.entrySet())
                    {
                        if (m.getValue() > modeMatCount)
                        {
                            modeMatCount = m.getValue();
                            modeMat = m.getKey();
                        }
                    }
                    // Make sure there'world not a tie for most common
                    for (Map.Entry<Material, Integer> m : freqs.entrySet())
                    {
                        if (m.getValue() == modeMatCount)
                        {
                            tiecheck = false;
                        }
                    }

                    // Record most common neighbor material for this block
                    if (tiecheck)
                    {
                        output.setMaterial(x, y, z, false, modeMat);
                    }
                }
            }
        }
        player.addPending(new ShapeChangeQueue(player, target.getLocation(), output));
    }

    @Override
    public String getName()
    {
        return "blend";
    }

    @Override
    public void parseArguments(String string, VariableHolder vars)
    {
        //TODO
    }

}
