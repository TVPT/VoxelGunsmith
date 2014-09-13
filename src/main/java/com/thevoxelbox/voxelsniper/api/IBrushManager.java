package com.thevoxelbox.voxelsniper.api;

public interface IBrushManager extends IManager {

    public void registerBrush(String identifier, Class<? extends IBrush> clazz);

}
