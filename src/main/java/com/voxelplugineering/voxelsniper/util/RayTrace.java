package com.voxelplugineering.voxelsniper.util;

import java.util.HashSet;
import java.util.Set;

import com.voxelplugineering.voxelsniper.common.CommonLocation;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;
import com.voxelplugineering.voxelsniper.common.CommonVector;

public class RayTrace
{
    
    private CommonLocation origin;
    private CommonVector direction;
    
    private Set<CommonMaterial<?>> traversalBlocks = new HashSet<CommonMaterial<?>>();
    
    private CommonLocation targetBlock = null;
    private CommonLocation lastBlock = null;
    
    public RayTrace(CommonLocation origin, CommonVector direction)
    {
        this.origin = origin;
        this.direction = direction;
    }
    
    public CommonLocation getOrigin()
    {
        return this.origin;
    }
    
    public CommonVector getDirection()
    {
        return this.direction;
    }
}
