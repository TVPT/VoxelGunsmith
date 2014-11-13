package com.voxelplugineering.voxelsniper.api;

import com.voxelplugineering.voxelsniper.common.CommonWorld;

public interface IWorldFactory
{

    CommonWorld getWorld(String name);
    
}
