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
package com.voxelplugineering.voxelsniper.world;

import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonLocation;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;
import com.voxelplugineering.voxelsniper.shape.Shape;

public class ShapeChangeQueue extends ChangeQueue
{

    private Shape shape;
    private CommonMaterial<?> material;
    private CommonLocation origin;
    private ExecutionState state;
    private long position = 0;

    public ShapeChangeQueue(ISniper sniper, CommonLocation origin, Shape shape, CommonMaterial<?> material)
    {
        super(sniper, origin.getWorld());
        this.origin = origin.add(-shape.getOrigin().getX(), -shape.getOrigin().getY(), -shape.getOrigin().getZ());
        this.state = ExecutionState.UNSTARTED;
        this.shape = shape;
        this.material = material;
    }

    @Override
    public boolean isFinished()
    {
        if (position == shape.getWidth() * shape.getHeight() * shape.getLength())
        {
            this.state = ExecutionState.DONE;
        }
        return this.state == ExecutionState.DONE;
    }

    @Override
    public void flush()
    {
        reset();
        this.getOwner().addPending(this);
        this.getOwner().addHistory(this.invert());
    }

    @Override
    public ChangeQueue invert()
    {
        return null;
    }

    @Override
    public int perform(int next)
    {
        int count = 0;
        if (state == ExecutionState.UNSTARTED)
        {
            for (int x = 0; x < shape.getWidth(); x++)
            {
                for (int y = 0; y < shape.getHeight(); y++)
                {
                    for (int z = 0; z < shape.getLength(); z++)
                    {
                        if (shape.get(x, y, z)
                                && (getWorld().getBlockAt(x + origin.getFlooredX(), y + origin.getFlooredY(), z + origin.getFlooredZ()).getMaterial()
                                        .isLiquid() || getWorld()
                                        .getBlockAt(x + origin.getFlooredX(), y + origin.getFlooredY(), z + origin.getFlooredZ()).getMaterial()
                                        .isReliantOnEnvironment()))
                        {
                            getWorld().setBlockAt(x + origin.getFlooredX(), y + origin.getFlooredY(), z + origin.getFlooredZ(), material);
                        }
                    }
                }
            }
            this.state = ExecutionState.INCREMENTAL;
        } else if (state == ExecutionState.INCREMENTAL)
        {
            Gunsmith.getLogger().info("Position at " + position);
            for (; position < shape.getWidth() * shape.getHeight() * shape.getLength() && count < next; position++)
            {
                int z = (int) (position / (shape.getWidth()*shape.getHeight()));
                int y = (int) ((position % (shape.getWidth()*shape.getHeight()))/shape.getWidth());
                int x = (int) ((position % (shape.getWidth()*shape.getHeight()))%shape.getWidth());
                if (shape.get(x, y, z)
                        && !getWorld().getBlockAt(x + origin.getFlooredX(), y + origin.getFlooredY(), z + origin.getFlooredZ()).getMaterial()
                                .isLiquid()
                        && !getWorld().getBlockAt(x + origin.getFlooredX(), y + origin.getFlooredY(), z + origin.getFlooredZ()).getMaterial()
                                .isReliantOnEnvironment())
                {
                    count++;
                    getWorld().setBlockAt(x + origin.getFlooredX(), y + origin.getFlooredY(), z + origin.getFlooredZ(), material);
                }
            }
            if (position == shape.getWidth() * shape.getHeight() * shape.getLength())
            {
                this.state = ExecutionState.DONE;
            }
        }
        return count;
    }

    @Override
    public void reset()
    {
        this.state = ExecutionState.UNSTARTED;
    }

}

enum ExecutionState
{
    UNSTARTED, INCREMENTAL, DONE;
}
