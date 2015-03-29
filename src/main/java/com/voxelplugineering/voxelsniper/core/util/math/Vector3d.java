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
package com.voxelplugineering.voxelsniper.core.util.math;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

/**
 * A 3-dimensional vector with integers.
 */
public class Vector3d implements Serializable, Cloneable
{

    private static final long serialVersionUID = 2185038435585259289L;

    /**
     * Returns an instance of the zero vector.
     */
    public static final Vector3d ZERO = new Vector3d(0, 0, 0);

    /**
     * The vector data.
     */
    private final double[] vec;

    /**
     * Constructs a new vector with the direction.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector3d(double x, double y, double z)
    {
        this.vec = new double[] { x, y, z };
    }

    /**
     * Returns the x-axis value
     * 
     * @return x value
     */
    public double getX()
    {
        return this.vec[0];
    }

    /**
     * Returns the y-axis value
     * 
     * @return y value
     */
    public double getY()
    {
        return this.vec[1];
    }

    /**
     * Returns the z-axis value
     * 
     * @return z value
     */
    public double getZ()
    {
        return this.vec[2];
    }

    /**
     * Returns a new vector representing this vector added to the given vector.
     * 
     * @param v the vector to add onto this vector
     * @return the new vector
     */
    public Vector3d add(Vector3d v)
    {
        checkNotNull(v, "Vector cannot be null");
        return new Vector3d(getX() + v.getX(), getY() + v.getY(), getZ() + v.getZ());
    }

    /**
     * Returns a new vector representing this vector added to the given values.
     * 
     * @param x The x value
     * @param y The y value
     * @param z The z value
     * @return the new vector
     */
    public Vector3d add(double x, double y, double z)
    {
        return new Vector3d(getX() + x, getY() + y, getZ() + z);
    }

    /**
     * Returns a new vector representing this vector scaled by the given scalar
     * quantity.
     * 
     * @param scalar the amount to scale this vector
     * @return the new vector
     */
    public Vector3d multipy(double scalar)
    {
        return new Vector3d(getX() * scalar, getY() * scalar, getZ() * scalar);
    }

    /**
     * Returns a new vector representing this vector projected onto the given
     * vector.
     * 
     * @param b the vector to project this vector onto
     * @return the new vector
     */
    public Vector3d project(Vector3d b)
    {
        checkNotNull(b, "Vector cannot be null");
        Vector3d a = clone();
        a.multipy(a.dot(b) / a.lengthSquared());
        return a;
    }

    /**
     * Returns the dot product of this vector with the given vector.
     * 
     * @param v the other vector
     * @return the dot product
     */
    public double dot(Vector3d v)
    {
        checkNotNull(v, "Vector cannot be null");
        return getX() * v.getX() + getY() * v.getY() + getZ() * v.getZ();
    }

    /**
     * Returns a new vector representing the cross product of this vector with
     * the given vector.
     * 
     * @param v the other vector
     * @return the cross product result
     */
    public Vector3d cross(Vector3d v)
    {
        checkNotNull(v, "Vector cannot be null");
        return new Vector3d(getY() * v.getZ() - getZ() * v.getY(), getZ() * v.getX() - getX() * v.getZ(), getX() * v.getY() - getY() * v.getX());
    }

    /**
     * Returns a new vector representing the normalized version of this vector.
     * 
     * @return the normalized vector
     */
    public Vector3d normalize()
    {
        return new Vector3d(getX() / length(), getY() / length(), getZ() / length());
    }

    /**
     * Returns the length of this vector.
     * 
     * @return the length
     */
    public double length()
    {
        return Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }

    /**
     * Returns the squared length of this vector (for computations requiring the
     * squared form of the length and wanting to save on the cost of Math.sqrt
     * in the normal length function).
     * 
     * @return the squared length
     */
    public double lengthSquared()
    {
        return getX() * getX() + getY() * getY() + getZ() * getZ();
    }

    /**
     * Returns an exact copy of this vector.
     * 
     * @return the clone
     */
    public Vector3d clone()
    {
        return new Vector3d(getX(), getY(), getZ());
    }

    /**
     * Returns a string representation of this vector.
     * 
     * @return The string
     */
    public String toString()
    {
        return "Vector3d: (" + this.vec[0] + ", " + this.vec[1] + ", " + this.vec[2] + ")";
    }
}
