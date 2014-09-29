package com.voxelplugineering.voxelsniper.common;

import java.lang.ref.WeakReference;

import com.voxelplugineering.voxelsniper.api.ISniper;

public abstract class CommonPlayer<T> implements ISniper
{
    WeakReference<T> playerReference;

    protected CommonPlayer(T player)
    {
        this.playerReference = new WeakReference<T>(player);
    }

    /**
     * Warning, this may return null.
     *
     * @return The player reference if not null
     */
    public final T getPlayerReference()
    {
        return this.playerReference.get();
    }
}
