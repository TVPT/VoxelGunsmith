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
package com.voxelplugineering.voxelsniper.core.shape.csg;

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.core.util.math.Vector3i;

/**
 * A Compound CSG shape made up of up to two other CSG shapes and an operation.
 */
public class CSGCompoundShape extends OffsetShape
{

    private final CSGShape a;
    private final CSGShape b;
    private final CSGOperation operation;
    private final int width;
    private final int height;
    private final int length;

    /**
     * Creates a new {@link CSGCompoundShape}.
     * 
     * @param base The base shape
     * @param offset The offset
     */
    public CSGCompoundShape(CSGShape base, Vector3i offset)
    {
        super(offset);
        base.offset(offset.multipy(-1));
        this.a = checkNotNull(base);
        this.b = null;
        this.operation = null;
        this.width = 0;
        this.height = 0;
        this.length = 0;
    }

    /**
     * Creates a new {@link CSGCompoundShape}.
     * 
     * @param a The first shape
     * @param b The second shape
     * @param o The operation
     */
    public CSGCompoundShape(CSGShape a, CSGShape b, CSGOperation o)
    {
        super(a.getOrigin());
        this.a = checkNotNull(a);
        this.b = checkNotNull(b);
        this.operation = checkNotNull(o);
        b.offset(a.getOrigin().multipy(-1));
        this.width = 0;
        this.height = 0;
        this.length = 0;
    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    /**
     * Creates a new {@link CSGCompoundShape} representing the other shape added to this one.
     * 
     * @param other The other shape
     * @return The new CSGShape
     */
    public CSGCompoundShape add(CSGShape other)
    {
        return new CSGCompoundShape(this, other, CSGOperation.ADD);
    }

    /**
     * Creates a new {@link CSGCompoundShape} representing the other shape subtracted from this one.
     * 
     * @param other The other shape
     * @return The new CSGShape
     */
    public CSGCompoundShape subtract(CSGShape other)
    {
        return new CSGCompoundShape(this, other, CSGOperation.SUBTRACT);
    }

    /**
     * Creates a new {@link CSGCompoundShape} representing the other shape xor'ed with this one.
     * 
     * @param other The other shape
     * @return The new CSGShape
     */
    public CSGCompoundShape xor(CSGShape other)
    {
        return new CSGCompoundShape(this, other, CSGOperation.XOR);
    }

    @Override
    public boolean get(int x, int y, int z, boolean relative)
    {
        if (this.b == null)
        {
            return this.a.get(x, y, z, relative);
        }
        switch (this.operation)
        {
        case ADD:
            return this.a.get(x, y, z, relative) || this.b.get(x, y, z, relative);
        case SUBTRACT:
            return this.a.get(x, y, z, relative) && !this.b.get(x, y, z, relative);
        case XOR:
            return this.a.get(x, y, z, relative) ^ this.b.get(x, y, z, relative);
        default:
            return false;
        }
    }

    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getHeight()
    {
        return this.height;
    }

    @Override
    public int getLength()
    {
        return this.length;
    }

    @Override
    public void set(int x, int y, int z, boolean relative)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unset(int x, int y, int z, boolean relative)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public CSGCompoundShape clone()
    {
        if (this.b == null)
        {
            return new CSGCompoundShape(this.a.clone(), this.getOrigin());
        }
        return new CSGCompoundShape(this.a.clone(), this.b.clone(), this.operation);
    }

}
