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

import java.util.Map;

import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.shape.csg.CuboidShape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.util.nbt.CompoundTag;

/**
 * A named {@link ComplexMaterialShape}.
 */
public class NamedWorldSection extends ComplexMaterialShape
{

    private String name;

    /**
     * Creates a new {@link NamedWorldSection}.
     * 
     * @param shape The shape
     * @param defaultMaterial The default material
     */
    public NamedWorldSection(Shape shape, Material defaultMaterial)
    {
        super(shape, defaultMaterial);
        this.name = "";
    }

    /**
     * Creates a new {@link NamedWorldSection}. This shape is initialized as a
     * shallow copy of the given shape.
     * 
     * @param shape The {@link ComplexMaterialShape} to replicate.
     */
    public NamedWorldSection(ComplexMaterialShape shape)
    {
        super(shape.getShape(), shape.getDefaultMaterial());
    }

    /**
     * Creates a new {@link NamedWorldSection} with the given material
     * dictionary.
     * 
     * @param shape The shape
     * @param materialDict The material dictionary to use
     */
    public NamedWorldSection(CuboidShape shape, Map<Short, Material> materialDict)
    {
        super(shape, materialDict.get(0));
        for (short key : materialDict.keySet())
        {
            this.registerMaterial(key, materialDict.get(key));
        }
    }

    /**
     * Gets the name of this shape.
     * 
     * @return The name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name for this shape.
     * 
     * @param name The new name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets if this world section has any tile entity data.
     * 
     * @return Has tile entities
     */
    public boolean hasTileEntities()
    {
        // TODO hasTileEntities
        return false;
    }

    /**
     * Gets the tile entities within this world section. The vector keys are the
     * tiles position relative to the origin of this world section.
     * 
     * @return The tiles
     */
    public Map<Vector3i, CompoundTag> getTileEntities()
    {
        // TODO getTileEntities
        return null;
    }

    /**
     * Sets what tile entities are stored within this world section. The vector
     * keys should the locations of the tiles relative to the origin of the
     * world section.
     * 
     * @param tileEntitiesMap The new tile entites
     */
    public void setTileEntityMap(Map<Vector3i, CompoundTag> tileEntitiesMap)
    {
        // TODO setTileEntityMap

    }

    /**
     * Gets if this world section has any entity data.
     * 
     * @return Has entities
     */
    public boolean hasEntities()
    {
        // TODO hasEntities
        return false;
    }

    /**
     * Gets the entities within this world section. The vector keys are the
     * entities position relative to the origin of this world section.
     * 
     * @return The entities
     */
    public Map<Vector3i, CompoundTag> getEntities()
    {
        // TODO getEntities
        return null;
    }

    /**
     * Sets what entities are stored within this world section. The vector keys
     * should the locations of the entities relative to the origin of the world
     * section.
     * 
     * @param entitiesMap The new entities
     */
    public void setEntitiesMap(Map<Vector3i, CompoundTag> entitiesMap)
    {
        // TODO setEntitiesMap

    }

}
