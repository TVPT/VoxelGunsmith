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

import com.voxelplugineering.voxelsniper.common.factory.WeakWrapper;

/**
 * Represents a Material type
 *
 * @param <T> The type of material being wrapped
 */
public abstract class CommonMaterial<T> extends WeakWrapper<T>
{

    /**
     * Constructs a new CommonMaterial for the given value.
     *
     * @param value the material object to wrap
     */
    public CommonMaterial(T value)
    {
        super(value);
    }

    /**
     * Returns the name of the material.
     * 
     * @return the name
     */
    @Override
    public abstract String toString();

    /**
     * Returns whether the material is representing a voxel which mat be placed within the world.
     * 
     * @return is a placeable block
     */
    public abstract boolean isBlock();

    /**
     * Returns whether the material is affected by gravity within the world.
     * 
     * @return is affected by gravity
     */
    public abstract boolean hasGravity();

    /**
     * Returns if the material is solid (eg. whether a user could pass through the material unhindered).
     * 
     * @return is a solid block
     */
    public abstract boolean isSolid();

    /**
     * Returns if the material supports transparency.
     * 
     * @return does support transparency
     */
    public abstract boolean isTransparent();

    /**
     * Returns if the material is a liquid.
     * 
     * @return is a liquid
     */
    public abstract boolean isLiquid();

    /**
     * Returns if the material is reliant on its environment. (ex. a torch hanging from a wall is reliant on the wall).
     * 
     * @return is reliant on the environment
     */
    public abstract boolean isReliantOnEnvironment();
    
    @Override
    public boolean equals(Object other)
    {
        if(other instanceof CommonMaterial)
        {
            CommonMaterial<?> otherMat = (CommonMaterial<?>) other;
            if(this.getThis().equals(otherMat.getThis()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the name of this material.
     * 
     * @return the name
     */
    public abstract String getName();

}
