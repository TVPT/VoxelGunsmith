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

import com.thevoxelbox.vsl.api.variables.VariableHolder;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.brushes.BrushPartType;
import com.voxelplugineering.voxelsniper.api.entity.Player;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.core.brushes.NativeBrush;
import com.voxelplugineering.voxelsniper.core.shape.ComplexShape;

/**
 * An implementation of the overlay brush part. <p> This brush part is a mask which replaces the
 * selected area with only blocks on the top layer, eg. blocks visible from the top of the shape.
 * </p>
 */
public class OverlayBrush extends NativeBrush
{

    /**
     * default ctor
     */
    public OverlayBrush()
    {
        super("overlay", BrushPartType.EFFECT);
        setHelp("overlay");
    }

    @Override
    public void run(RuntimeState state)
    {
        checkNotNull(state);
        VariableHolder vars = state.getVars();
        Player player = vars.get("__PLAYER__", Player.class).get();
        World world = player.getWorld();
        Block target = vars.get("targetBlock", Block.class).get();
        Shape shape = vars.get("__CHAINED__shape", Shape.class).get();
        Location targetVec = target.getLocation().add(-shape.getOrigin().getX(), -shape.getOrigin().getY(), -shape.getOrigin().getZ());
        Material air = world.getMaterialRegistry().getAirMaterial();
        ComplexShape out = new ComplexShape(shape.getWidth(), shape.getHeight(), shape.getLength(), shape.getOrigin());
        int depth = vars.get("depth", Integer.class).or(1);
        for (int x = 0; x < shape.getWidth(); x++)
        {
            for (int z = 0; z < shape.getLength(); z++)
            {
                boolean found = false;
                int height = -1;
                for (int y = shape.getHeight() - 1; y >= 0 && (height == -1 || !found); y--)
                {
                    if (height == -1 && world.getBlock(targetVec.add(x, y, z)).get().getMaterial() != air)
                    {
                        height = y;
                    }
                    if (shape.get(x, y, z, false))
                    {
                        found = true;
                    }
                }
                /*
                 * The check of height+1 here is for the case of the first block
                 * being not air, but the block above it (the one outside of the
                 * shape) being not air, say you sniped into a wall. In these
                 * cases we don't want to apply the overlay.
                 */
                if (found && height != -1 && world.getBlock(targetVec.add(x, height + 1, z)).get().getMaterial() == air)
                {
                    if (height - depth < 0)
                    {
                        out.grow(0, height - depth, 0);
                    }
                    for (int y = height; y > height - depth; y--)
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
        return "overlay";
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
            try
            {
                double depth = Double.parseDouble(string);
                vars.set("depth", ((Double) depth).intValue());
            } catch (NumberFormatException i)
            {
                // couldn't parse an integer or a double from the input, ignore
            }
        }
    }

}
