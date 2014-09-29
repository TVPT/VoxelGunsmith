package com.voxelplugineering.voxelsniper;

import java.lang.ref.WeakReference;

import com.voxelplugineering.voxelsniper.api.ISniper;

public abstract class Sniper<T> implements ISniper
{

    WeakReference<T> player;
    
    public Sniper(T player)
    {
        this.player = new WeakReference<T>(player);
    }

    @Override
    public String getCurrentToolId()
    {
        return null;
    }

    @Override
    public String getToolId(Object object)
    {
        return null;
    }

    @Override
    public T getPlayer()
    {
        return player.get();
    }
    
}
