package com.voxelplugineering.voxelsniper.common.event;

import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonVector;

public class SnipeEvent extends CommonEvent
{
    private ISniper sniper;
    private CommonVector direction;

    public SnipeEvent(ISniper s, CommonVector dir)
    {
        this.sniper = s;
        this.direction = dir;
    }

    public ISniper getSniper()
    {
        return this.sniper;
    }

    public CommonVector getDirection()
    {
        return this.direction;
    }
}
