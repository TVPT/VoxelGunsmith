package com.thevoxelbox.voxelsniper.common;

public abstract class CommonBlock
{

    private final CommonLocation location;
    private final CommonMaterial material;

    protected CommonBlock(CommonLocation location, CommonMaterial material)
    {
        this.location = location;
        this.material = material;
    }

    public final CommonLocation getLocation()
    {
        return this.location;
    }

    public final CommonMaterial getMaterial()
    {
        return this.material;
    }

    public abstract void setMaterial(CommonMaterial material);

}
