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
package com.voxelplugineering.voxelsniper.brush.effect.morphological;

import com.voxelplugineering.voxelsniper.util.math.Maths;
import com.voxelplugineering.voxelsniper.world.World;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * A {@link FilterOperation} for the basic blend operation.
 */
public class BlendMaterialOperation implements FilterOperation
{

    private Map<MaterialState, Integer> mats;

    /**
     * Creates a new {@link BlendMaterialOperation}.
     */
    public BlendMaterialOperation()
    {
        this.mats = Maps.newHashMapWithExpectedSize(10);
    }

    @Override
    public String getName()
    {
        return "blend";
    }

    @Override
    public boolean checkPosition(int x, int y, int z, int dx, int dy, int dz, World w, MaterialState m)
    {
        if (!(dx == 0 && dy == 0 && dz == 0))
        {
            // TODO: Use world bounds instead of hardcoded magical values from
            // Minecraft.
            int clampedY = Maths.clamp(y + dy, 0, 255);
            MaterialState mat = w.getBlock(x + dx, clampedY, z + dz).get().getMaterial();
            if (this.mats.containsKey(mat))
            {
                this.mats.put(mat, this.mats.get(mat) + 1);
            } else
            {
                this.mats.put(mat, 1);
            }
        }
        return false;
    }

    @Override
    public Optional<MaterialState> getResult()
    {
        // Select the material which occurred the most.
        int n = 0;
        MaterialState winner = null;
        for (Map.Entry<MaterialState, Integer> e : this.mats.entrySet())
        {
            if (e.getValue() > n)
            {
                winner = e.getKey();
                n = e.getValue();
            }
        }

        // If multiple materials occurred the most, the tie check will become
        // true.
        boolean tie = false;
        for (Map.Entry<MaterialState, Integer> e : this.mats.entrySet())
        {
            if (e.getValue() == n && !e.getKey().equals(winner))
            {
                tie = true;
            }
        }

        // If a tie is found, no change is made.
        if (!tie)
        {
            return Optional.of(winner);
        }
        return Optional.of(null);
    }

    @Override
    public void reset()
    {
        this.mats = Maps.newHashMapWithExpectedSize(10);
    }

}
