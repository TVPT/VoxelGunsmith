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
package com.voxelplugineering.voxelsniper.world.queue;

import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.material.MaterialState;

import java.util.Optional;



/**
 * A special change queue for setting all of a shape to a single material.
 */
public class ShapeChangeQueue extends ChangeQueue
{

    private final MaterialShape shape;
    private final Location originOffset;
    private final Location origin;
    private final boolean physics;
    private ExecutionState state;
    private long position = 0;
    private int ticks = 0;
    private boolean reported = false;

    /**
     * Creates a new {@link ShapeChangeQueue}.
     * 
     * @param sniper the owner
     * @param origin the origin of the shape in the world
     * @param shape the shape
     */
    public ShapeChangeQueue(ChangeQueueOwner sniper, Location origin, MaterialShape shape)
    {
        super(sniper, origin.getWorld());
        this.originOffset = origin.add(-shape.getOrigin().getX(), -shape.getOrigin().getY(), -shape.getOrigin().getZ());
        this.origin = origin;
        this.state = ExecutionState.UNSTARTED;
        this.shape = shape;
        if(sniper instanceof Player) {
            this.physics = ((Player) sniper).getBrushVars().get(BrushKeys.PHYSICS, Boolean.class).orElse(true);
        } else {
            this.physics = true;
        }
    }

    @Override
    public boolean isFinished()
    {
        if (this.position == this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength())
        {
            this.state = ExecutionState.DONE;
        }
        return this.state == ExecutionState.DONE;
    }

    @Override
    public void flush()
    {
        reset();
        if (this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength() > 2000000)
        {
            getOwner().sendMessage("Shape too large, skipping undo storage.");
        } else
        {
            this.owner.getUndoHistory().addHistory(this, new ShapeChangeQueue(getOwner(), this.origin,
                    this.originOffset.getWorld().getShapeFromWorld(this.origin, this.shape.getShape())));
        }
        this.getOwner().addPending(this);
    }

    @Override
    public int perform(int next)
    {
        int count = 0;
        if (this.state == ExecutionState.UNSTARTED)
        {
            this.position = this.shape.getHeight() - 1;
            this.state = ExecutionState.BREAKABLE;
            this.ticks = 0;
            this.reported = false;
        }
        if (this.state == ExecutionState.BREAKABLE)
        {
            for (; this.position >= 0 && count < next; this.position--)
            {
                int subcount = 0;
                int oy = (int) this.position + this.originOffset.getFlooredY();
                for (int x = 0; x < this.shape.getWidth(); x++)
                {
                    int ox = x + this.originOffset.getFlooredX();
                    for (int z = 0; z < this.shape.getLength(); z++)
                    {
                        int oz = z + this.originOffset.getFlooredZ();
                        Optional<Block> block = this.world.getBlock(ox, oy, oz);
                        if (!block.isPresent())
                        {
                            continue;
                        }
                        MaterialState existingMaterial = block.get().getMaterial();
                        Optional<MaterialState> newMaterial = this.shape.getMaterial(x, (int) this.position, z, false);
                        if (newMaterial.isPresent() && (existingMaterial.getType().isLiquid() || existingMaterial.getType().isReliantOnEnvironment()))
                        {
                            this.world.setBlock(newMaterial.get(), x + this.originOffset.getFlooredX(),
                                    (int) this.position + this.originOffset.getFlooredY(), z + this.originOffset.getFlooredZ(), this.physics);
                            subcount++;
                        }
                    }
                }
                count += Math.max(next / (100000 / (this.shape.getWidth() * this.shape.getLength())), subcount);

            }
            if (this.position < 0)
            {
                this.ticks = 0;
                this.position = 0;
                this.state = ExecutionState.INCREMENTAL;
            } else if (this.ticks > 10)
            {
                this.ticks = 0;
                this.owner.sendMessage(String.format("Operating on breakable blocks %d out of %d", this.shape.getHeight() - 1 - this.position,
                        this.shape.getHeight() - 1));
            }
        } else if (this.state == ExecutionState.INCREMENTAL)
        {
            this.ticks++;
            // Gunsmith.getLogger().info("Position at " + this.position);
            for (; this.position < this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength() && count < next; this.position++)
            {
                int z = (int) (this.position / (this.shape.getWidth() * this.shape.getHeight()));
                int y = (int) ((this.position % (this.shape.getWidth() * this.shape.getHeight())) / this.shape.getWidth());
                int x = (int) ((this.position % (this.shape.getWidth() * this.shape.getHeight())) % this.shape.getWidth());
                Optional<Block> block = this.world.getBlock(x + this.originOffset.getFlooredX(), y + this.originOffset.getFlooredY(),
                        z + this.originOffset.getFlooredZ());
                if (!block.isPresent())
                {
                    continue;
                }
                MaterialState existingMaterial = block.get().getMaterial();
                Optional<MaterialState> newMaterial = this.shape.getMaterial(x, y, z, false);
                if (newMaterial.isPresent() && !(existingMaterial.getType().isLiquid() || existingMaterial.getType().isReliantOnEnvironment()))
                {
                    count++;
                    this.world.setBlock(newMaterial.get(), x + this.originOffset.getFlooredX(), y + this.originOffset.getFlooredY(),
                            z + this.originOffset.getFlooredZ(), this.physics);
                }
            }
            if (this.position == this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength())
            {
                if (this.reported)
                {
                    this.owner.sendMessage("Finished %d changes.", this.position);
                }
                this.state = ExecutionState.DONE;
            } else if (this.ticks > 10)
            {
                this.reported = true;
                this.ticks = 0;
                this.owner.sendMessage("Performed %d out of %d changes.", this.position,
                        this.shape.getWidth() * this.shape.getHeight() * this.shape.getLength());
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

/**
 * The execution state.
 */
enum ExecutionState
{
    UNSTARTED,
    BREAKABLE,
    INCREMENTAL,
    DONE;
}
