package com.voxelplugineering.voxelsniper.common.event;

import com.voxelplugineering.voxelsniper.api.ISniper;

public class SniperCreateEvent
{

    private ISniper sniper;

    public SniperCreateEvent(ISniper s)
    {
        this.sniper = s;
    }

    public ISniper getSniper()
    {
        return this.sniper;
    }

}
