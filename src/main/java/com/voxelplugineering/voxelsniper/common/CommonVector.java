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

import java.io.Serializable;

public class CommonVector implements Serializable, Cloneable
{
    private static final long serialVersionUID = 2185038435585259289L;
    double[] vec;

    public CommonVector(double x, double y, double z)
    {
        this.vec = new double[]{x, y, z};
    }

    public double getX()
    {
        return vec[0];
    }

    public double getY()
    {
        return vec[1];
    }

    public double getZ()
    {
        return vec[2];
    }

    public CommonVector add(CommonVector v)
    {
        return new CommonVector(getX() + v.getX(), getY() + v.getY(), getZ() + v.getZ());
    }

    public CommonVector multipy(double scalar)
    {
        return new CommonVector(getX() * scalar, getY() * scalar, getZ() * scalar);
    }

    public CommonVector project(CommonVector b)
    {
        CommonVector a = clone();
        a.multipy(a.dot(b) / a.lengthSquared());
        return a;
    }

    public double dot(CommonVector v)
    {
        return getX() * v.getX() + getY() * v.getY() + getZ() * v.getZ();
    }

    public CommonVector cross(CommonVector v)
    {
        return new CommonVector(getY() * v.getZ() - getZ() * v.getY(), getZ() * v.getX() - getX() * v.getZ(), getX() * v.getY() - getY() * v.getX());
    }

    public CommonVector normalize()
    {
        return new CommonVector(getX() / length(), getY() / length(), getZ() / length());
    }

    public double length()
    {
        return Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }

    public double lengthSquared()
    {
        return getX() * getX() + getY() * getY() + getZ() * getZ();
    }

    public CommonVector clone()
    {
        return new CommonVector(getX(), getY(), getZ());
    }
}
