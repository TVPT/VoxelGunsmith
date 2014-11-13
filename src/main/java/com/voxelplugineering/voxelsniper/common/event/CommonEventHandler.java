package com.voxelplugineering.voxelsniper.common.event;

import com.google.common.eventbus.Subscribe;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.CommonLocation;
import com.voxelplugineering.voxelsniper.common.CommonVector;

public class CommonEventHandler
{

    public CommonEventHandler()
    {

    }

    @Subscribe
    public void onSnipe(SnipeEvent event)
    {
        ISniper sniper = event.getSniper();
        CommonVector direction = event.getDirection();
        CommonLocation location = sniper.getLocation();
    }
}
