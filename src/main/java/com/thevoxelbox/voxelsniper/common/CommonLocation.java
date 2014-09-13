package com.thevoxelbox.voxelsniper.common;

public final class CommonLocation implements Cloneable
{
    private final CommonWorld world;
    private final int x, y, z;

    public CommonLocation(CommonWorld world, int x, int y, int z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CommonWorld getWorld()
    {
        return this.world;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }

    public CommonLocation add(int x, int y, int z)
    {
        return new CommonLocation(this.world, this.x + x, this.y + y, this.z + z);
    }

    @Override
    public CommonLocation clone()
    {
        return new CommonLocation(this.world, this.x, this.y, this.z);
    }
}
