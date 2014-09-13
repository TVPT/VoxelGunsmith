package com.thevoxelbox.voxelsniper.common;

public abstract class CommonMaterial<T> {

    private final T value;

    protected CommonMaterial(T value) {
        this.value = value;
    }

    public final T getValue() {
        return this.value;
    }

    @Override
    public abstract String toString();
}
