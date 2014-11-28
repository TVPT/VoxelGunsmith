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
package com.voxelplugineering.voxelsniper.common;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A combination location and material representation of a single voxel. The location is immutable.
 */
public class CommonBlock
{

    /**
     * The location of this voxel.
     */
    private final CommonLocation location;
    /**
     * The material of the voxel.
     */
    private CommonMaterial<?> material;

    /**
     * Creates a new CommonBlock with the given location and material
     *
     * @param location the location, cannot be null
     * @param material the material, cannot be null
     */
    public CommonBlock(CommonLocation location, CommonMaterial<?> material)
    {
        checkNotNull(location, "Location cannot be null");
        checkNotNull(material, "Material cannot be null");
        this.location = location;
        this.material = material;
    }

    /**
     * Returns the location of this voxel.
     * 
     * @return the location
     */
    public final CommonLocation getLocation()
    {
        return this.location;
    }

    /**
     * Returns the material currently at the location of this voxel. Unless overridden by the specific implementation this material may be out of date
     * to the underlying world.
     * 
     * @return the material
     */
    public CommonMaterial<?> getMaterial()
    {
        return this.material;
    }

    /**
     * Sets the material of the voxel at this location.
     * 
     * @param material the new material, cannot be null
     */
    public void setMaterial(CommonMaterial<?> material)
    {
        checkNotNull(material, "Material cannot be null");
        this.material = material;
        this.location.getWorld().setBlockAt(this.location, material);
    }

    /**
     * Utility method for setting the material of the voxel without propagating the world changes.
     *
     * @param mat the new material, cannot be null
     */
    protected void localSetMaterial(CommonMaterial<?> mat)
    {
        checkNotNull(material, "Material cannot be null");
        this.material = mat;
    }

}
