package com.voxelplugineering.voxelsniper.common.event;

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.api.ISniper;

public class SniperDestroyEvent extends SniperEvent
{
    /**
     * Creates a new SniperCreateEvent
     *
     * @param sniper the sniper newly created
     */
    public SniperDestroyEvent(ISniper sniper)
    {
        checkNotNull(sniper, "Sniper cannot be null!");
        setSniper(sniper);
    }
}
