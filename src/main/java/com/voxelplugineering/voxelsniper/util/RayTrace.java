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

public class RayTrace
{
    
    private CommonLocation origin;
    private CommonWorld world;
    
    private Set<CommonMaterial<?>> traversalBlocks = new HashSet<CommonMaterial<?>>();
    
    private CommonBlock targetBlock = null;
    private CommonBlock lastBlock = null;
    
    private double length, range, hLength;
    private int minWorldY, maxWorldY;
    private double yaw, pitch;
    private double currentX, currentY, currentZ;
    private int targetX, targetY, targetZ;
    private int lastX, lastY, lastZ;
    private double rotX, rotY, rotXSin, rotXCos, rotYSin, rotYCos;
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
    
    public CommonLocation getOrigin()
    {
        return this.origin;
    }
    
    public double getYaw()
    {
        return this.yaw;
    }
    
    public double getPitch()
    {
        return this.pitch;
    }
    
    public double getRange()
    {
        return this.range;
    }
    
    public void setRange(double r)
    {
        this.range = r;
    }
    
    public void setTraversalBlocks(CommonMaterial<?>... blocks)
    {
        this.traversalBlocks.clear();
        for(CommonMaterial<?> b: blocks)
        {
            this.traversalBlocks.add(b);
        }
    }
    
    public Set<CommonMaterial<?>> getTraversalBlocks()
    {
        return Collections.unmodifiableSet(this.traversalBlocks);
    }
    
    public CommonBlock getTargetBlock()
    {
        if(this.length > this.range || targetBlock.getLocation().getY() < this.minWorldY || targetBlock.getLocation().getY() > this.maxWorldY)
        {
            return getLastBlock();
        }
        return this.targetBlock;
    }
    
    public CommonBlock getLastBlock()
    {
        if(this.length > this.range || lastBlock.getLocation().getY() < this.minWorldY || lastBlock.getLocation().getY() > this.maxWorldY)
        {
            return null;
        }
        return this.lastBlock;
    }
    
    private void init()
    {
        this.length = 0;
        this.currentX = this.origin.getX();
        this.currentY = this.origin.getY()+((Double) Gunsmith.getConfiguration().get("PLAYER_EYE_HEIGHT"));
        this.currentZ = this.origin.getZ();
        this.targetX = (int) Math.floor(this.currentX);
        this.targetY = (int) Math.floor(this.currentY);
        this.targetZ = (int) Math.floor(this.currentZ);
        this.lastX = (int) Math.floor(this.currentX);
        this.lastY = (int) Math.floor(this.currentY);
        this.lastZ = (int) Math.floor(this.currentZ);

        this.rotX = (this.yaw + 90) % 360;
        this.rotY = this.pitch * -1;
        this.rotYCos = Math.cos(Math.toRadians(this.rotY));
        this.rotYSin = Math.sin(Math.toRadians(this.rotY));
        this.rotXCos = Math.cos(Math.toRadians(this.rotX));
        this.rotXSin = Math.sin(Math.toRadians(this.rotX));
    }
    
    public void trace()
    {
        init();
        checkOutOfWorld();
        if(this.length <= this.range)
        {
            step();
        }
    }
    
    private void step()
    {
        this.lastX = this.targetX;
        this.lastY = this.targetY;
        this.lastZ = this.targetZ;
        
        do
        {
            this.length += this.step;

            this.hLength = (this.length * this.rotYCos);
            this.currentX = (this.length * this.rotYSin);
            this.currentY = (this.hLength * this.rotXCos);
            this.currentZ = (this.hLength * this.rotXSin);

            this.targetX = (int) Math.floor(this.currentX + this.origin.getX());
            this.targetY = (int) Math.floor(this.currentY + this.origin.getY());
            this.targetZ = (int) Math.floor(this.currentZ + this.origin.getZ());

        }
        while ((this.length <= this.range) && ((this.targetX == this.lastX) && (this.targetY == this.lastY) && (this.targetZ == this.lastZ)));
        
        if(!this.traversalBlocks.contains(world.getBlockAt(this.targetX, this.targetY, this.targetZ).getMaterial()))
        {
            return;
        }
        
        if (this.length > this.range || this.targetY > this.maxWorldY || this.targetY < this.minWorldY)
        {
            this.targetX = this.lastX;
            this.targetY = this.lastY;
            this.targetZ = this.lastZ;
            return;
        }
        else
        {
            step();
        }
    }
    
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

                    this.hLength = (this.length * this.rotYCos);
                    this.currentX = (this.length * this.rotYSin);
                    this.currentY = (this.hLength * this.rotXCos);
                    this.currentZ = (this.hLength * this.rotXSin);

                    this.targetX = (int) Math.floor(this.currentX + this.origin.getX());
                    this.targetY = (int) Math.floor(this.currentY + this.origin.getY());
                    this.targetZ = (int) Math.floor(this.currentZ + this.origin.getZ());

                }
                while ((this.length <= this.range) && ((this.targetX == this.lastX) && (this.targetY == this.lastY) && (this.targetZ == this.lastZ)));
            }
        }
        else if (this.targetY < 0)
        {
            while (this.targetY < 0 && this.length <= this.range)
            {
                this.lastX = this.targetX;
                this.lastY = this.targetY;
                this.lastZ = this.targetZ;

                do
                {
                    this.length += this.step;

                    this.hLength = (this.length * this.rotYCos);
                    this.currentX = (this.length * this.rotYSin);
                    this.currentY = (this.hLength * this.rotXCos);
                    this.currentZ = (this.hLength * this.rotXSin);

                    this.targetX = (int) Math.floor(this.currentX + this.origin.getX());
                    this.targetY = (int) Math.floor(this.currentY + this.origin.getY());
                    this.targetZ = (int) Math.floor(this.currentZ + this.origin.getZ());

                }
                while ((this.length <= this.range) && ((this.targetX == this.lastX) && (this.targetY == this.lastY) && (this.targetZ == this.lastZ)));
            }
        }
    }
}
