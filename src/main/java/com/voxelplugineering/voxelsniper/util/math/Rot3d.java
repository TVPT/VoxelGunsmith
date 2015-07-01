/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 The VoxelBox <http://engine.thevoxelbox.com>
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
 * Implements a rotation matrix to assist with rotating points in 3d space with an arbitrary yaw,
 * pitch, and roll.
 * 
 */
public class Rot3d {
    private double[][] r = new double[3][3];

    public Rot3d(double yaw, double pitch, double roll) {
        double cos1 = Math.cos(yaw);
        double sin1 = Math.sin(yaw);

        double cos2 = Math.cos(pitch);
        double sin2 = Math.sin(pitch);

        double cos3 = Math.cos(roll);
        double sin3 = Math.sin(roll);

        this.r[0][0] = cos1 * cos2;
        this.r[0][1] = sin1 * sin3 - cos1 * cos3 * sin2;
        this.r[0][2] = cos3 * sin1 + cos1 * sin2 * sin3;
        this.r[1][0] = sin2;
        this.r[1][1] = cos2 * cos3;
        this.r[1][2] = -cos2 * sin3;
        this.r[2][0] = -cos2 * sin1;
        this.r[2][1] = cos1 * sin3 + cos3 * sin1 * sin2;
        this.r[2][2] = cos1 * cos3 - sin1 * sin2 * sin3;

    }

    public Rot3d(Vector3i axis) {
        this(Math.atan2(axis.getX(), axis.getZ()), Math.asin(axis.getY()/axis.length()), 0);
    }

    /**
     * Performs the rotation operation on the given x, y, and z coordinates relative to the origin.
     * 
     * @param x
     * @param y
     * @param z
     * @return the final position
     */
    public double[] doRotation(double x, double y, double z) {
        double[] p = new double[3];
        p[0] = this.r[0][0] * x + this.r[0][1] * y + this.r[0][2] * z;
        p[1] = this.r[1][0] * x + this.r[1][1] * y + this.r[1][2] * z;
        p[2] = this.r[2][0] * x + this.r[2][1] * y + this.r[2][2] * z;
        return p;
    }

    /**
     * Performs the rotation operation on the given x, y, and z coordinates relative to the origin.
     * 
     * @param xyz
     * @return the final position
     */
    public double[] doRotation(double[] xyz) {
        double[] p = new double[3];
        p[0] = this.r[0][0] * xyz[0] + this.r[0][1] * xyz[1] + this.r[0][2] * xyz[2];
        p[1] = this.r[1][0] * xyz[0] + this.r[1][1] * xyz[1] + this.r[1][2] * xyz[2];
        p[2] = this.r[2][0] * xyz[0] + this.r[2][1] * xyz[1] + this.r[2][2] * xyz[2];
        return p;
    }

    public float[] doRotation(float[] xyz) {
        float[] p = new float[3];
        p[0] = (float) (this.r[0][0] * xyz[0] + this.r[0][1] * xyz[1] + this.r[0][2] * xyz[2]);
        p[1] = (float) (this.r[1][0] * xyz[0] + this.r[1][1] * xyz[1] + this.r[1][2] * xyz[2]);
        p[2] = (float) (this.r[2][0] * xyz[0] + this.r[2][1] * xyz[1] + this.r[2][2] * xyz[2]);
        return p;
    }
}