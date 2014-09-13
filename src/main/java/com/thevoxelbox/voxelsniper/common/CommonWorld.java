package com.thevoxelbox.voxelsniper.common;

public abstract class CommonWorld
{

    public abstract CommonChunk getChunkAt(int x, int z);

    public CommonBlock getBlockAt(CommonLocation location)
    {
        return getBlockAt(location.getX(), location.getY(), location.getZ());
    }

    public abstract CommonBlock getBlockAt(int x, int y, int z);
}
