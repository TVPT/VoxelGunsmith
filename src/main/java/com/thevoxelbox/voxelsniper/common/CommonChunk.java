package com.thevoxelbox.voxelsniper.common;

public abstract class CommonChunk {

    public abstract CommonWorld getCommonWorld();

    public abstract CommonBlock getRelativeBlockAt(int x, int y, int z);
}
