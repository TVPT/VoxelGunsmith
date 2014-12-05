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

/**
 * An enum of directions.
 */
public enum CommonDirection
{
    /*
     * These are the minecraft directions, I'm not sure at this time what the best approach is to make this more platform independent. The easiest
     * course I can see is to remove the labels and refer to them by their axes. So 'NORTH' would become 'NEGATIVE_Z'. Makes it more difficult to
     * label the diagonals though. For now however it doesn't matter as programmatically it is the getModX(), getModY(), and getModZ() methods which
     * are actually used, not the names.
     */
    NORTH(0, 0, -1),
    EAST(1, 0, 0),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH_EAST(NORTH, EAST),
    NORTH_WEST(NORTH, WEST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),
    WEST_NORTH_WEST(WEST, NORTH_WEST),
    NORTH_NORTH_WEST(NORTH, NORTH_WEST),
    NORTH_NORTH_EAST(NORTH, NORTH_EAST),
    EAST_NORTH_EAST(EAST, NORTH_EAST),
    EAST_SOUTH_EAST(EAST, SOUTH_EAST),
    SOUTH_SOUTH_EAST(SOUTH, SOUTH_EAST),
    SOUTH_SOUTH_WEST(SOUTH, SOUTH_WEST),
    WEST_SOUTH_WEST(WEST, SOUTH_WEST),
    SELF(0, 0, 0);

    private final int modX;
    private final int modY;
    private final int modZ;

    private CommonDirection(final int modX, final int modY, final int modZ)
    {
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
    }

    private CommonDirection(final CommonDirection face1, final CommonDirection face2)
    {
        this.modX = face1.getModX() + face2.getModX();
        this.modY = face1.getModY() + face2.getModY();
        this.modZ = face1.getModZ() + face2.getModZ();
    }

    /**
     * Returns the x axis value of this direction.
     * 
     * @return the x axis value
     */
    public int getModX()
    {
        return this.modX;
    }

    /**
     * Returns the y axis value of this direction.
     * 
     * @return the y axis value
     */
    public int getModY()
    {
        return this.modY;
    }

    /**
     * Returns the z axis value of this direction.
     * 
     * @return the z axis value
     */
    public int getModZ()
    {
        return this.modZ;
    }

    /**
     * Returns the inverse direction to this direction.
     * 
     * @return the inverse
     */
    public CommonDirection getOpposite()
    {
        for (CommonDirection d : CommonDirection.values())
        {
            if (d.getModX() == -getModX() && d.getModY() == -getModY() && d.getModZ() == -getModZ())
            {
                return d;
            }
        }
        return CommonDirection.SELF;
    }

}
