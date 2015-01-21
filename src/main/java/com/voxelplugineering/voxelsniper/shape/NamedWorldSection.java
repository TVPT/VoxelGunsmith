package com.voxelplugineering.voxelsniper.shape;

import java.util.Map;

import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.util.nbt.CompoundTag;
import com.voxelplugineering.voxelsniper.util.nbt.Tag;

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
     * Creates a new {@link NamedWorldSection}. This shape is
     * initialized as a shallow copy of the given shape.
     * 
     * @param shape The {@link ComplexMaterialShape} to replicate.
     */
    public NamedWorldSection(ComplexMaterialShape shape)
    {
        super(shape.getShape(), shape.getDefaultMaterial());
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

    public boolean hasTileEntities()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Map<Vector3i, CompoundTag> getTileEntities()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setTileEntityMap(Map<Vector3i, CompoundTag> tileEntitiesMap)
    {
        // TODO Auto-generated method stub
        
    }

    public void setEntitiesMap(Map<Vector3i, CompoundTag> entitiesMap)
    {
        // TODO Auto-generated method stub
        
    }

    public boolean hasEntities()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Map<Vector3i, CompoundTag> getEntities()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
