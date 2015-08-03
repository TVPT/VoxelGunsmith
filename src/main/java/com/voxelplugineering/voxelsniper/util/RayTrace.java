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
package com.voxelplugineering.voxelsniper.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.util.math.Vector3d;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.World;
import com.voxelplugineering.voxelsniper.world.material.Material;

/**
 * A utility for performing a ray trace within a world.
 */
public class RayTrace
{

    private static Direction dirFromDiff(int tx, int ty, int tz, int lx, int ly, int lz)
    {
        int modx = Integer.signum(tx - lx);
        int mody = Integer.signum(ty - ly);
        int modz = Integer.signum(tz - lz);
        return Direction.of(modx, mody, modz);
    }

    /**
     * The starting point from which the ray is cast.
     */
    private Location origin;
    /**
     * The world within the trace is being performed.
     */
    private World world;

    /**
     * A set of materials which are treated as non-solid to the ray (eg. the ray passes through them
     * without stopping)
     */
    private List<Material> traversalBlocks = Lists.newArrayList();

    /**
     * The block targeted by the last trace.
     */
    private Block targetBlock = null;
    private Direction targetDirection = null;
    /**
     * The last block targeted before the end of the trace.
     */
    private Block lastBlock = null;
    private Direction lastDirection = null;

    /*
     * Values used internally for the calculation of the ray trace. length: the length of the ray
     * range: the maximum length of the ray before stopping minWorldY: the minimum y value of the
     * world maxWorldY: the maximum y value of the world yaw: the yaw of the direction of the ray
     * pitch: the pitch of the direction of the ray currentX|Y|Z: the current position of the ray,
     * relative to the origin targetX|Y|Z: the position of the target block lastX|Y|Z: the position
     * of the last block rotX|Y: the yaw and pitch of the ray shifted to the same context as the
     * world, as well as the sine and cosine of each step: the increment amount of the step of the
     * ray trace
     */

    private double length;
    private double range;
    private int minWorldY;
    private int maxWorldY;
    private double yaw;
    private double pitch;
    private double currentX;
    private double currentY;
    private double currentZ;
    private int targetX;
    private int targetY;
    private int targetZ;
    private int lastX;
    private int lastY;
    private int lastZ;
    private double rotX;
    private double rotY;
    private double rotXSin;
    private double rotXCos;
    private double rotYSin;
    private double rotYCos;
    private double step;
    private Vector3d playerEyeOffset;

    /**
     * Creates a new raytrace to reference with the given location yaw and pitch.
     * 
     * @param origin the origin location
     * @param yaw the yaw
     * @param pitch the pitch
     */
    public RayTrace(Location origin, double yaw, double pitch, double range, int minY, int maxY, double step, Vector3d playerEyeOffset)
    {
        this.origin = checkNotNull(origin, "Origin cannot be null");
        this.yaw = yaw;
        this.pitch = pitch;
        this.range = range;
        this.minWorldY = minY;
        this.maxWorldY = maxY;
        this.step = step;
        this.world = this.origin.getWorld();
        this.traversalBlocks.add(this.world.getMaterialRegistry().getAirMaterial());
        this.playerEyeOffset = playerEyeOffset;
    }

    /**
     * Returns the origin of the ray trace.
     * 
     * @return the origin
     */
    public Location getOrigin()
    {
        return this.origin;
    }

    /**
     * The yaw of the direction of the ray in degrees.
     * 
     * @return the yaw
     */
    public double getYaw()
    {
        return this.yaw;
    }

    /**
     * The pitch of the direction of the ray, in degrees.
     * 
     * @return the pitch
     */
    public double getPitch()
    {
        return this.pitch;
    }

    /**
     * the maximum range of the ray.
     * 
     * @return the range
     */
    public double getRange()
    {
        return this.range;
    }

    /**
     * Sets the maximum range of the ray.
     * 
     * @param range the new range
     */
    public void setRange(double range)
    {
        this.range = range;
    }

    /**
     * Sets the blocks that may be traversed by the ray.
     * 
     * @param blocks the traversal blocks
     */
    public void setTraversalBlocks(Material... blocks)
    {
        checkNotNull(blocks, "Traversal blocks cannot be null");
        this.traversalBlocks.clear();
        Collections.addAll(this.traversalBlocks, blocks);
    }

    /**
     * Returns the blocks that will be traversed by this ray as if non-solid.
     * 
     * @return the blocks that may be traversed
     */
    public List<Material> getTraversalBlocks()
    {
        return Collections.unmodifiableList(this.traversalBlocks);
    }

    /**
     * Returns the target block of this ray.
     * 
     * @return the target block
     */
    public Block getTargetBlock()
    {
        if (this.length > this.range || this.targetBlock.getLocation().getY() < this.minWorldY
                || this.targetBlock.getLocation().getY() > this.maxWorldY)
        {
            return getLastBlock();
        }
        return this.targetBlock;
    }

    /**
     * Returns the block face traversed while the ray was entering the target block.
     * 
     * @return The target block face
     */
    public Direction getTargetFace()
    {
        return this.targetDirection;
    }

    /**
     * Returns the last block targeted before the current target block.
     * 
     * @return the last block
     */
    public Block getLastBlock()
    {
        if (this.length > this.range || this.lastBlock.getLocation().getY() < this.minWorldY || this.lastBlock.getLocation().getY() > this.maxWorldY)
        {
            return null;
        }
        return this.lastBlock;
    }

    /**
     * Gets the last block face traversed before the last block.
     * 
     * @return The last face
     */
    public Direction getLastFace()
    {
        return this.lastDirection;
    }

    /**
     * Returns the length of the last ray (the distance from the origin to the target block).
     * 
     * @return the length
     */
    public double getLength()
    {
        return this.length;
    }

    /**
     * Initializes the ray for a new trace.
     */
    private void init()
    {
        this.length = 0;
        this.origin = this.origin.add(this.playerEyeOffset);
        this.currentX = this.origin.getX();
        this.currentY = this.origin.getY();
        this.currentZ = this.origin.getZ();
        this.targetX = (int) Math.floor(this.currentX);
        this.targetY = (int) Math.floor(this.currentY);
        this.targetZ = (int) Math.floor(this.currentZ);
        this.lastX = (int) Math.floor(this.currentX);
        this.lastY = (int) Math.floor(this.currentY);
        this.lastZ = (int) Math.floor(this.currentZ);

        // I think this is to do with the axis orientation of minecraft,
        // not sure how to handle this in a context separated from the
        // implementation.
        // Possibly something attached to the world to detail the different
        // orientations
        // of the axis and which directions they represent.
        //
        // Currently:
        // 0 degrees of yaw is the direction of the positive x axis going
        // clockwise towards the positive z axis first.
        this.rotX = (this.yaw + 90) % 360;
        this.rotY = this.pitch * -1;
        this.rotYCos = Math.cos(Math.toRadians(this.rotY));
        this.rotYSin = Math.sin(Math.toRadians(this.rotY));
        this.rotXCos = Math.cos(Math.toRadians(this.rotX));
        this.rotXSin = Math.sin(Math.toRadians(this.rotX));
    }

    /**
     * Perform a new ray trace.
     */
    public void trace()
    {
        GunsmithLogger.getLogger().debug("Traversing: ");
        for (Material m : this.traversalBlocks)
        {
            GunsmithLogger.getLogger().debug("\t" + m.getName() + " " + m.toString());
        }
        init();
        checkOutOfWorld();
        if (this.length <= this.range)
        {
            step();
        }
        this.lastBlock = this.world.getBlock(this.lastX, this.lastY, this.lastZ).orNull();
        this.targetBlock = this.world.getBlock(this.targetX, this.targetY, this.targetZ).or(this.lastBlock);
    }

    /**
     * Perform a single step of the trace.
     */
    private void step()
    {

        this.lastX = this.targetX;
        this.lastY = this.targetY;
        this.lastZ = this.targetZ;
        this.lastDirection = this.targetDirection;
        // step until we find a new block in the line
        do
        {
            this.length += this.step;

            this.currentX = (this.length * this.rotYCos) * this.rotXCos;
            this.currentY = this.length * this.rotYSin;
            this.currentZ = (this.length * this.rotYCos) * this.rotXSin;

            this.targetX = (int) Math.floor(this.currentX + this.origin.getX());
            this.targetY = (int) Math.floor(this.currentY + this.origin.getY());
            this.targetZ = (int) Math.floor(this.currentZ + this.origin.getZ());
        } while ((this.length <= this.range) && ((this.targetX == this.lastX) && (this.targetY == this.lastY) && (this.targetZ == this.lastZ)));

        this.targetDirection = dirFromDiff(this.targetX, this.targetY, this.targetZ, this.lastX, this.lastY, this.lastZ);

        Optional<Block> next = this.world.getBlock(this.targetX, this.targetY, this.targetZ);
        if (!next.isPresent())
        {
            // Abort out of bounds, or something wrong with area of world
            this.targetX = this.lastX;
            this.targetY = this.lastY;
            this.targetZ = this.lastZ;
            this.targetDirection = this.lastDirection;
            return;
        }
        if (!this.traversalBlocks.contains(next.get().getMaterial().getType()))
        {
            // Abort - found non-traversal block
            GunsmithLogger.getLogger().debug("Found non-traversal block: " + next.get().getMaterial().getType().getName() + " breaking. "
                    + next.get().getMaterial().getType().toString());
            return;
        }

        if (this.length > this.range || this.targetY > this.maxWorldY || this.targetY < this.minWorldY)
        {
            // Abort - Out of bounds
            this.targetX = this.lastX;
            this.targetY = this.lastY;
            this.targetZ = this.lastZ;
            this.targetDirection = this.lastDirection;
            this.length = this.range;
        } else
        {
            // continue
            step();
        }
    }

    /**
     * Step forward until the ray is within the bounds of the world.
     */
    private void checkOutOfWorld()
    {
        if (this.targetY > this.maxWorldY)
        {
            while (this.targetY > this.maxWorldY && this.length <= this.range)
            {
                this.lastX = this.targetX;
                this.lastY = this.targetY;
                this.lastZ = this.targetZ;

                do
                {
                    this.length += this.step;

                    this.currentX = (this.length * this.rotYCos) * this.rotXCos;
                    this.currentY = this.length * this.rotYSin;
                    this.currentZ = (this.length * this.rotYCos) * this.rotXSin;

                    this.targetX = (int) Math.floor(this.currentX + this.origin.getX());
                    this.targetY = (int) Math.floor(this.currentY + this.origin.getY());
                    this.targetZ = (int) Math.floor(this.currentZ + this.origin.getZ());

                } while ((this.length <= this.range)
                        && ((this.targetX == this.lastX) && (this.targetY == this.lastY) && (this.targetZ == this.lastZ)));
            }
        } else if (this.targetY < 0)
        {
            while (this.targetY < 0 && this.length <= this.range)
            {
                this.lastX = this.targetX;
                this.lastY = this.targetY;
                this.lastZ = this.targetZ;

                do
                {
                    this.length += this.step;

                    this.currentX = (this.length * this.rotYCos) * this.rotXCos;
                    this.currentY = this.length * this.rotYSin;
                    this.currentZ = (this.length * this.rotYCos) * this.rotXSin;

                    this.targetX = (int) Math.floor(this.currentX + this.origin.getX());
                    this.targetY = (int) Math.floor(this.currentY + this.origin.getY());
                    this.targetZ = (int) Math.floor(this.currentZ + this.origin.getZ());

                } while ((this.length <= this.range)
                        && ((this.targetX == this.lastX) && (this.targetY == this.lastY) && (this.targetZ == this.lastZ)));
            }
        }
    }
}
