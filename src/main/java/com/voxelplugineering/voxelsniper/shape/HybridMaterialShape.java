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
package com.voxelplugineering.voxelsniper.shape;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * A {@link MaterialShape} which a material for each point in the shape.
 */
public class HybridMaterialShape implements MaterialShape
{
    private short nextId = 1;
    private BiMap<Short, Material> materialDictionary;
    private BiMap<Material, Short> inverseDictionary;
    private short[] materials;
    private Shape shape;
    private Material defaultMaterial;
    private short simpleMaterial;
    private boolean isComplex = false;

    /**
     * Creates a new {@link MaterialShape}.
     * 
     * @param shape the shape
     * @param defaultMaterial the default material for the shape
     */
    public HybridMaterialShape(Shape shape, Material defaultMaterial)
    {
        checkNotNull(defaultMaterial, "Default material cannot be null!");
        this.shape = new ComplexShape(shape);
        this.materialDictionary = HashBiMap.create();
        this.inverseDictionary = this.materialDictionary.inverse();
        this.materials = null;
        this.materialDictionary.put((short) 0, defaultMaterial);
        this.flood(defaultMaterial);
        this.defaultMaterial = defaultMaterial;
    }

    /**
     * Returns the wrapped shape.
     * 
     * @return the shape
     */
    public Shape getShape()
    {
        return this.shape;
    }

    /**
     * Gets the material at the given location.
     * 
     * @param x the x position to get
     * @param y the y position to get
     * @param z the z position to get
     * @param relative whether to offset the given position to the origin
     * @return the material, or {@link Optional#absent()} if the point in the shape is not set
     */
    public Optional<Material> getMaterial(int x, int y, int z, boolean relative)
    {
        if (this.getShape().get(x, y, z, relative))
        {
            if (!this.isComplex || this.materials == null)
            {
                return Optional.<Material>of(this.materialDictionary.get(this.simpleMaterial));
            }
            if (this.materials[getIndex(x, y, z)] == -1)
            {
                this.materials[getIndex(x, y, z)] = 0; // the default material
            }
            return Optional.<Material>of(this.materialDictionary.get(this.materials[getIndex(x, y, z)]));
        } else
        {
            return Optional.absent();
        }
    }

    /**
     * Sets the given point in the shape and applies the given material to that point.
     * 
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @param relative whether to offset the given position to the origin
     * @param material the material to set the given point to
     */
    public void setMaterial(int x, int y, int z, boolean relative, Material material)
    {
        checkNotNull(material);
        if (relative)
        {
            x += getShape().getOrigin().getX();
            y += getShape().getOrigin().getY();
            z += getShape().getOrigin().getZ();
        }
        if (x >= getShape().getWidth() || x < 0 || y >= getShape().getHeight() || y < 0 || z >= getShape().getLength() || z < 0)
        {
            throw new ArrayIndexOutOfBoundsException("Tried to set material outside of the shape. (" + x + ", " + y + ", " + z + ")");
        }
        complexify();
        short id = this.getOrRegisterMaterial(material);
        this.materials[getIndex(x, y, z)] = id;
        getShape().set(x, y, z, false);
    }

    /**
     * Unsets the given location in the shape and clears any material data for that point.
     * 
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @param relative whether to offset the given position to the origin
     */
    public void unsetMaterial(int x, int y, int z, boolean relative)
    {
        if (relative)
        {
            x += getShape().getOrigin().getX();
            y += getShape().getOrigin().getY();
            z += getShape().getOrigin().getZ();
        }
        if (x >= getShape().getWidth() || x < 0 || y >= getShape().getHeight() || y < 0 || z >= getShape().getLength() || z < 0)
        {
            throw new ArrayIndexOutOfBoundsException("Tried to unset material outside of the shape. (" + x + ", " + y + ", " + z + ")");
        }
        complexify();
        this.materials[getIndex(x, y, z)] = -1;
        getShape().unset(x, y, z, false);
    }

    /**
     * Sets every point of the shape which is set to the given material.
     * 
     * @param material the material
     */
    public void flood(Material material)
    {
        short id = this.getOrRegisterMaterial(material);
        this.isComplex = false;
        this.materials = null;
        this.simpleMaterial = id;
    }

    /**
     * Fills the entire horizontal layers between y and height with the given material.
     * 
     * @param material The material
     * @param y The starting y layer
     * @param height The height of the area to fill
     */
    public void setHorizontalLayer(Material material, int y, int height)
    {
        complexify();
        short id = this.getOrRegisterMaterial(material);
        int startIndex = getIndex(0, y, 0);
        int endIndex = getIndex(this.shape.getWidth() - 1, y + height - 1, this.shape.getLength() - 1) + 1;
        Arrays.fill(this.materials, startIndex, endIndex, id);
    }

    /**
     * Returns the id for the given material within this {@link MaterialShape}'s dictionary. If the material is not found in the dictionary then the
     * next id is allocated to it.
     * 
     * @param material the material to fetch
     * @return the id for the material
     */
    private short getOrRegisterMaterial(Material material)
    {
        if (this.inverseDictionary.containsKey(material))
        {
            return this.inverseDictionary.get(material);
        } else
        {
            short id = this.nextId++;
            this.materialDictionary.put(id, material);
            return id;
        }
    }

    /**
     * Returns the width of the underlying shape (x-axis size). Note that this is not the width of the region set, but rather the width of the total
     * possible volume.
     * 
     * @return the width
     */
    public int getWidth()
    {
        return this.shape.getWidth();
    }

    /**
     * Returns the height of this shape (y-axis size). Note that this is not the height of the region set, but rather the height of the total possible
     * volume.
     * 
     * @return the height
     */
    public int getHeight()
    {
        return this.shape.getHeight();
    }

    /**
     * Returns the length of this shape (z-axis size). Note that this is not the length of the region set, but rather the length of the total possible
     * volume.
     * 
     * @return the length
     */
    public int getLength()
    {
        return this.shape.getLength();
    }

    /**
     * Returns the origin of this shape.
     * 
     * @return the origin
     */
    public Vector3i getOrigin()
    {
        return this.shape.getOrigin();
    }

    /**
     * Gets the index for the given location.
     * 
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     * @return The index
     */
    protected int getIndex(int x, int y, int z)
    {
        return y * (this.shape.getWidth() * this.shape.getLength()) + z * (this.shape.getWidth()) + x;
    }

    /**
     * Floods the shape with the default material, effectively resetting it to initial conditions.
     */
    public void reset()
    {
        flood(this.defaultMaterial);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean get(int x, int y, int z, boolean relative)
    {
        return this.shape.get(x, y, z, relative);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(int x, int y, int z, boolean relative)
    {
        complexify();
        this.shape.set(x, y, z, relative);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MaterialShape clone()
    {
        //TODO
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unset(int x, int y, int z, boolean relative)
    {
        complexify();
        this.shape.unset(x, y, z, relative);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVolume()
    {
        return this.shape.getVolume();
    }

    private void complexify()
    {
        if (this.isComplex)
        {
            return;
        }
        this.isComplex = true;
        if (this.materials != null)
        {
            this.materials = new short[this.shape.getVolume()];
        }
        if (!(this.shape instanceof ComplexShape))
        {
            this.shape = new ComplexShape(this.shape);
        }
    }

}
