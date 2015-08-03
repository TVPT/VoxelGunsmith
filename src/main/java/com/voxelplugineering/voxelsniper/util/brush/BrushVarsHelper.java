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
package com.voxelplugineering.voxelsniper.util.brush;

import com.voxelplugineering.voxelsniper.brush.BrushAction;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.world.Block;

import com.google.common.base.Optional;

/**
 * A utility for getting common values from the {@link BrushVars}.
 */
public final class BrushVarsHelper
{

    /**
     * Gets the target block adjusting for whether the player used the primary or alternate action.
     * 
     * @param args The brush vars
     * @return The adjusted target block
     */
    public static Optional<Block> getTargetBlock(BrushVars args)
    {
        BrushAction action = args.get(BrushKeys.ACTION, BrushAction.class).get();
        if (action == BrushAction.PRIMARY)
        {
            return args.get(BrushKeys.TARGET_BLOCK, Block.class);
        }
        return args.get(BrushKeys.LAST_BLOCK, Block.class);
    }

    private BrushVarsHelper()
    {

    }
}
