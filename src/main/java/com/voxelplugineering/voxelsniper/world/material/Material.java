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
package com.voxelplugineering.voxelsniper.world.material;

import com.voxelplugineering.voxelsniper.util.Nameable;

/**
 * Represents a material that may be applied to a block.
 */
public interface Material extends Nameable
{

    /**
     * Gets the name of this material.
     * 
     * @return The name
     */
    @Override
    String getName();

    /**
     * Gets whether the material is representing a voxel which mat be placed within the world.
     * 
     * @return Is a placeable block
     */
    boolean isBlock();

    /**
     * Gets whether the material is affected by gravity within the world.
     * 
     * @return Is affected by gravity
     */
    boolean isAffectedByGravity();

    /**
     * Gets if the material is solid (eg. whether a user could pass through the material
     * unhindered).
     * 
     * @return Is a solid block
     */
    boolean isSolid();

    /**
     * Gets if the material is a liquid.
     * 
     * @return Is a liquid
     */
    boolean isLiquid();

    /**
     * Gets if the material is reliant on its environment. (ex. a torch hanging from a wall is
     * reliant on the wall).
     * 
     * @return Is reliant on the environment
     */
    boolean isReliantOnEnvironment();

    /**
     * Gets if the material is flammable.
     * 
     * @return Is flammable
     */
    boolean isFlammable();

    /**
     * Gets the default {@link MaterialState} for this material.
     * 
     * @return The default state
     */
    MaterialState getDefaultState();

}
