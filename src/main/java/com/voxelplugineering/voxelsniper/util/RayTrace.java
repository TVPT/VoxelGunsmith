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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.common.CommonBlock;
import com.voxelplugineering.voxelsniper.common.CommonLocation;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;
import com.voxelplugineering.voxelsniper.common.CommonWorld;

/**
 * A utility for performing a ray trace within a world.
 */
public class RayTrace
{

    /**
     * The starting point from which the ray is cast.
     */
    private CommonLocation origin;
    /**
     * The world within the trace is being performed.
     */
    private CommonWorld world;

    /**
     * A set of materials which are treated as non-solid to the ray (eg. the ray passes through them without stopping)
     */
    private Set<CommonMaterial<?>> traversalBlocks = new HashSet<CommonMaterial<?>>();

    /**
     * The block targeted by the last trace.
     */
    private CommonBlock targetBlock = null;
    /**
     * The last block targeted before the end of the trace.
     */
    private CommonBlock lastBlock = null;

    /*
     * Values used internally for the calculation of the ray trace.
     * 
     * length:        the length of the ray
     * range:         the maximum length of the ray before stopping
     * hLength:       the vertical length of the ray
     * minWorldY:     the minimum y value of the world
     * maxWorldY:     the maximum y value of the world
     * yaw:           the yaw of the direction of the ray
     * pitch:         the pitch of the direction of the ray
     * currentX|Y|Z:  the current position of the ray, relative to the origin
     * targetX|Y|Z:   the position of the target block
     * lastX|Y|Z:     the position of the last block
     * rotX|Y:        the yaw and pitch of the ray shifted to the same context as the world, as well as the sine and cosine of each
     * step:          the increment amount of the step of the ray trace
     * 
     */

    private double length;
    private double range;
    private double verticalLength;
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

    public RayTrace(CommonLocation origin, double yaw, double pitch)
    {
        this.origin = origin;
        this.yaw = yaw;
        this.pitch = pitch;
        this.range = (Double) Gunsmith.getConfiguration().get("RAY_TRACE_RANGE");
        this.minWorldY = (Integer) Gunsmith.getConfiguration().get("MINIMUM_WORLD_DEPTH");
        this.maxWorldY = (Integer) Gunsmith.getConfiguration().get("MAXIMUM_WORLD_HEIGHT");
        this.step = (Double) Gunsmith.getConfiguration().get("RAY_TRACE_STEP");
        this.traversalBlocks.add(Gunsmith.getMaterialFactory().getAirMaterial());
        this.world = this.origin.getWorld();
    }

    /**
     * Returns the origin of the ray trace.
     * 
     * @return the origin
     */
    public CommonLocation getOrigin()
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
     * @param range
     *            the new range
     */
    public void setRange(double range)
    {
        this.range = range;
    }

    /**
     * Sets the blocks that may be traversed by the ray.
     * 
     * @param blocks
     *            the traversal blocks
     */
    public void setTraversalBlocks(CommonMaterial<?>... blocks)
    {
        this.traversalBlocks.clear();
        for (CommonMaterial<?> b : blocks)
        {
            this.traversalBlocks.add(b);
        }
    }

    /**
     * Returns the blocks that will be traversed by this ray as if non-solid.
     * 
     * @return the blocks that may be traversed
     */
    public Set<CommonMaterial<?>> getTraversalBlocks()
    {
        return Collections.unmodifiableSet(this.traversalBlocks);
    }

    /**
     * Returns the target block of this ray.
     * 
     * @return the target block
     */
    public CommonBlock getTargetBlock()
    {
        if (this.length > this.range || targetBlock.getLocation().getY() < this.minWorldY || targetBlock.getLocation().getY() > this.maxWorldY)
        {
            return getLastBlock();
        }
        return this.targetBlock;
    }

    /**
     * Returns the last block targeted before the current target block.
     * 
     * @return the last block
     */
    public CommonBlock getLastBlock()
    {
        if (this.length > this.range || lastBlock.getLocation().getY() < this.minWorldY || lastBlock.getLocation().getY() > this.maxWorldY)
        {
            return null;
        }
        return this.lastBlock;
    }

    /**
     * Initializes the ray for a new trace.
     */
    private void init()
    {
        this.length = 0;
        this.currentX = this.origin.getX();
        this.currentY = this.origin.getY() + ((Double) Gunsmith.getConfiguration().get("PLAYER_EYE_HEIGHT"));
        this.currentZ = this.origin.getZ();
        this.targetX = (int) Math.floor(this.currentX);
        this.targetY = (int) Math.floor(this.currentY);
        this.targetZ = (int) Math.floor(this.currentZ);
        this.lastX = (int) Math.floor(this.currentX);
        this.lastY = (int) Math.floor(this.currentY);
        this.lastZ = (int) Math.floor(this.currentZ);

        // I think this is to do with the axis orientation of minecraft, not sure how to handle this in a context separated from the implementation.
        // Possibly something attached to the world to detail the different orientations of the axis and which directions they represent.
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
        init();
        checkOutOfWorld();
        if (this.length <= this.range)
        {
            step();
        }
    }

    /**
     * Perform a single step of the trace.
     */
    private void step()
    {
        this.lastX = this.targetX;
        this.lastY = this.targetY;
        this.lastZ = this.targetZ;

        do
        {
            this.length += this.step;

            this.verticalLength = (this.length * this.rotYCos);
            this.currentX = (this.length * this.rotYSin);
            this.currentY = (this.verticalLength * this.rotXCos);
            this.currentZ = (this.verticalLength * this.rotXSin);

            this.targetX = (int) Math.floor(this.currentX + this.origin.getX());
            this.targetY = (int) Math.floor(this.currentY + this.origin.getY());
            this.targetZ = (int) Math.floor(this.currentZ + this.origin.getZ());

        } while ((this.length <= this.range) && ((this.targetX == this.lastX) && (this.targetY == this.lastY) && (this.targetZ == this.lastZ)));

        if (!this.traversalBlocks.contains(world.getBlockAt(this.targetX, this.targetY, this.targetZ).getMaterial()))
        {
            return;
        }

        if (this.length > this.range || this.targetY > this.maxWorldY || this.targetY < this.minWorldY)
        {
            this.targetX = this.lastX;
            this.targetY = this.lastY;
            this.targetZ = this.lastZ;
            return;
        } else
        {
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

                    this.verticalLength = (this.length * this.rotYCos);
                    this.currentX = (this.length * this.rotYSin);
                    this.currentY = (this.verticalLength * this.rotXCos);
                    this.currentZ = (this.verticalLength * this.rotXSin);

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

                    this.verticalLength = (this.length * this.rotYCos);
                    this.currentX = (this.length * this.rotYSin);
                    this.currentY = (this.verticalLength * this.rotXCos);
                    this.currentZ = (this.verticalLength * this.rotXSin);

                    this.targetX = (int) Math.floor(this.currentX + this.origin.getX());
                    this.targetY = (int) Math.floor(this.currentY + this.origin.getY());
                    this.targetZ = (int) Math.floor(this.currentZ + this.origin.getZ());

                } while ((this.length <= this.range)
                        && ((this.targetX == this.lastX) && (this.targetY == this.lastY) && (this.targetZ == this.lastZ)));
            }
        }
    }
}
