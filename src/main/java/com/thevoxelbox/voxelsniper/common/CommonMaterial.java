package com.thevoxelbox.voxelsniper.common;

/**
 * Represents a Material type
 * @param <T> The type of material being wrapped
 */
public abstract class CommonMaterial<T>
{

    private final T value;

    protected CommonMaterial(T value)
    {
        this.value = value;
    }

    public final T getValue()
    {
        return this.value;
    }

    @Override
    public abstract String toString();
}
