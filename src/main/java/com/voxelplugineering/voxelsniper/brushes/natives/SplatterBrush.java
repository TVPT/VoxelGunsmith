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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
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
import com.voxelplugineering.voxelsniper.shape.ComplexShape;
import com.voxelplugineering.voxelsniper.world.queue.ShapeChangeQueue;

public class SplatterBrush extends NativeBrush
{

    private static final int GROW_PERCENT_MIN = 1;
    private static final int GROW_PERCENT_DEFAULT = 1000;
    private static final int GROW_PERCENT_MAX = 9999;
    private static final int SEED_PERCENT_MIN = 1;
    private static final int SEED_PERCENT_DEFAULT = 1000;
    private static final int SEED_PERCENT_MAX = 9999;
    private static final int SPLATREC_PERCENT_MIN = 1;
    private static final int SPLATREC_PERCENT_DEFAULT = 3;
    private static final int SPLATREC_PERCENT_MAX = 10;

    private int seedPercent = SEED_PERCENT_DEFAULT;
    private int growPercent = GROW_PERCENT_DEFAULT;
    private int splatterRecursions = SPLATREC_PERCENT_DEFAULT;
    private Random generator = new Random();

    public SplatterBrush()
    {
        super("splatter", BrushPartType.EFFECT);
        setHelp("splatter");
    }

    @Override
    public void run(RuntimeState state)
    {
        VariableHolder vars = state.getVars();
        Player player = vars.get("__PLAYER__", Player.class).get();
        World world = player.getWorld();
        Block target = vars.get("targetBlock", Block.class).get();
        Shape shape = vars.get("__CHAINED__shape", Shape.class).get();
        Location targetVec = target.getLocation().add(-shape.getOrigin().getX(), -shape.getOrigin().getY(), -shape.getOrigin().getZ());
        Material air = world.getMaterialRegistry().getAirMaterial();
        ComplexShape out = new ComplexShape(shape.getWidth(), shape.getHeight(), shape.getLength(), shape.getOrigin());

        for (int x = 0; x < out.getWidth(); x++)
        {
            for (int y = 0; y < out.getHeight(); y++)
            {
                for (int z = 0; z < out.getLength(); z++)
                {
                    if (shape.get(x, y, z, false) && this.generator.nextInt(SEED_PERCENT_MAX + 1) <= this.seedPercent)
                    {
                        out.set(x, y, z, false);
                    }
                }
            }
        }
        final int gref = this.growPercent;
        final boolean[][][] tempSplat = new boolean[out.getWidth()][out.getHeight()][out.getLength()];
        int growcheck;
        for (int r = 0; r < this.splatterRecursions; r++)
        {
            this.growPercent = gref - ((gref / this.splatterRecursions) * (r));
            for (int x = out.getWidth() - 1; x >= 0; x--)
            {
                for (int y = out.getHeight() - 1; y >= 0; y--)
                {
                    for (int z = out.getLength() - 1; z >= 0; z--)
                    {
                        tempSplat[x][y][z] = out.get(x, y, z, false); // prime tempsplat

                        growcheck = 0;
                        if (!out.get(x, y, z, false))
                        {
                            if (x != 0 && out.get(x - 1, y, z, false))
                            {
                                growcheck++;
                            }
                            if (y != 0 && out.get(x, y - 1, z, false))
                            {
                                growcheck++;
                            }
                            if (z != 0 && out.get(x, y, z - 1, false))
                            {
                                growcheck++;
                            }
                            if (x != out.getWidth() - 1 && out.get(x + 1, y, z, false))
                            {
                                growcheck++;
                            }
                            if (y != out.getHeight() - 1 && out.get(x, y + 1, z, false))
                            {
                                growcheck++;
                            }
                            if (z != out.getLength() - 1 && out.get(x, y, z + 1, false))
                            {
                                growcheck++;
                            }
                        }

                        if (growcheck >= GROW_PERCENT_MIN && this.generator.nextInt(GROW_PERCENT_MAX + 1) <= this.growPercent)
                        {
                            tempSplat[x][y][z] = true;
                        }

                    }
                }
            }
            // integrate tempsplat back into splat at end of iteration
            for (int x = out.getWidth() - 1; x >= 0; x--)
            {
                for (int y = out.getHeight() - 1; y >= 0; y--)
                {
                    for (int z = out.getLength() - 1; z >= 0; z--)
                    {
                        if (shape.get(x, y, z, false) && tempSplat[x][y][z])
                        {
                            out.set(x, y, z, false);
                        } else
                        {
                            out.unset(x, y, z, false);
                        }
                    }
                }
            }
        }
        this.growPercent = gref;
        // Fill 1x1x1 holes
        for (int x = out.getWidth() - 1; x >= 0; x--)
        {
            for (int y = out.getHeight() - 1; y >= 0; y--)
            {
                for (int z = out.getLength() - 1; z >= 0; z--)
                {
                    if (shape.get(x, y, z, false) && out.get(Math.max(x - 1, 0), y, z, false) && out.get(Math.min(x + 1, out.getWidth() - 1), y, z, false)
                            && out.get(x, Math.max(y - 1, 0), z, false) && out.get(x, Math.min(y + 1, out.getHeight() - 1), z, false)
                            && out.get(x, y, Math.max(z - 1, 0), false) && out.get(x, y, Math.min(z + 1, out.getLength() - 1), false))
                    {
                        out.set(x, y, z, false);
                    }
                }
            }
        }

        vars.set("__CHAINED__shape", out);
    }

    @Override
    public String getName()
    {
        return "splatter";
    }

    @Override
    public void parseArguments(String string, VariableHolder vars)
    {
        try
        {
            int depth = Integer.parseInt(string);
            vars.set("depth", depth);
        } catch (NumberFormatException e)
        {

        }
    }

}
