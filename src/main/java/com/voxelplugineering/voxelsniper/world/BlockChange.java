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

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.common.CommonBlock;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;

/**
 * Represents a single block change to a world. A replace material is stored with this, it is not intended that this replace material be used to mask
 * a change but rather exists as a safeguard against overlapping changes. Therefore this replace material should be set to the current material of the
 * target at the time of the action which spawned this change, then if the world is modified while this change is in the queue and the target block is
 * changed to another material this change will be ignored as it would have moved out of relevance. This may however currently be the wrong action to
 * take due to overlapping changes which should be time ordered and the current distribution method of the world for processing changes.
 * <p>
 * TODO: remove the replace material, its original designed use may be the incorrect action for the current world queue processor.
 */
public class BlockChange extends Change
{

    /**
     * The material to replace.
     */
    private final CommonMaterial<?> from;
    /**
     * The new material.
     */
    private final CommonMaterial<?> to;

    /**
     * Creates a new BlockChange.
     * 
     * @param x the x position of the change
     * @param y the y position of the change
     * @param z the z position of the change
     * @param from the material to replace
     * @param to the new material
     */
    public BlockChange(int x, int y, int z, CommonMaterial<?> from, CommonMaterial<?> to)
    {
        super(x, y, z);
        checkNotNull(from, "From material cannot be null");
        checkNotNull(to, "To material cannot be null");
        this.from = from;
        this.to = to;
    }

    /**
     * Creates a new BlockChange at the position of the given block, the replace material will be set to whatever block material is currently at that
     * block position.
     * 
     * @param target the target block
     * @param mat the new material
     */
    public BlockChange(CommonBlock target, CommonMaterial<?> mat)
    {
        super(target.getLocation().getFlooredX(), target.getLocation().getFlooredY(), target.getLocation().getFlooredZ());
        checkNotNull(target, "Target block cannot be null");
        checkNotNull(mat, "To material cannot be null");
        this.to = mat;
        this.from = target.getLocation().getWorld().getBlockAt(target.getLocation()).getMaterial();
    }

    /**
     * Returns the replace material.
     * 
     * @return the replace material
     */
    public CommonMaterial<?> getFrom()
    {
        return this.from;
    }

    /**
     * Returns the new material being placed by this change.
     * 
     * @return the new material
     */
    public CommonMaterial<?> getTo()
    {
        return this.to;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Change getInverse()
    {
        return new BlockChange(getX(), getY(), getZ(), getTo(), getFrom());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Change clone()
    {
        return new BlockChange(getX(), getY(), getZ(), getFrom(), getTo());
    }

    /**
     * Returns a string representation of this change.
     * 
     * @return the string
     */
    public String toString()
    {
        return "BlockChange(" + getX() + ", " + getY() + ", " + getZ() + " " + getTo().toString() + ")";
    }

}
