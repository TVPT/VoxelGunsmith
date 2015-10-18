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
package com.voxelplugineering.voxelsniper.util.math;

/**
 * A utility with basic math operations.
 */
public final class Maths
{

    private Maths()
    {
    }

    /**
     * Clamps the given value to be between the minimum and maximum values (both inclusive) and
     * returns the result.
     * 
     * @param value The input value
     * @param min The minimum value
     * @param max The maximum value
     * @return The clamped value
     */
    public static int clamp(int value, int min, int max)
    {
        return value < min ? min : value > max ? max : value;
    }

    /**
     * Clamps the given value to be between the minimum and maximum values (both inclusive) and
     * returns the result.
     * 
     * @param value The input value
     * @param min The minimum value
     * @param max The maximum value
     * @return The clamped value
     */
    public static double clamp(double value, double min, double max)
    {
        return value < min ? min : value > max ? max : value;
    }

    /**
     * A helper for getting the ceil of a double as an integer, note that a double value may
     * represent values far outside the range of values representable by an integer, in these cases
     * the result is undefined.
     * 
     * @param d The double to ceil
     * @return The integer value
     * @see Math#ceil(double)
     */
    public static int ceil(double d)
    {
        return (int) Math.ceil(d);
    }

    /**
     * A helper for getting the floor of a double as an integer, note that a double value may
     * represent values far outside the range of values representable by an integer, in these cases
     * the result is undefined.
     * 
     * @param d The double to floor
     * @return The integer value
     * @see Math#floor(double)
     */
    public static int floor(double d)
    {
        return (int) Math.floor(d);
    }
}
