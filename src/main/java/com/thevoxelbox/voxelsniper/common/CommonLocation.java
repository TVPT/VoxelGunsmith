package com.thevoxelbox.voxelsniper.common;

public final class CommonLocation implements Cloneable {
    protected final CommonWorld world;
    protected final int x, y, z;

    public CommonLocation(CommonWorld world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CommonLocation add(int x, int y, int z) {
        return new CommonLocation(world, this.x + x, this.y + y, this.z + z);
    }

    @Override
    public CommonLocation clone() {
        return new CommonLocation(this.world, this.x, this.y, this.z);
    }
}
