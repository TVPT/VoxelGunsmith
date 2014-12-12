package com.voxelplugineering.voxelsniper.common.event;

import com.voxelplugineering.voxelsniper.api.ISniper;

public abstract class SniperEvent extends CommonEvent
{
    /**
     * The sniper.
     */
    private ISniper sniper;

    /**
     * Sets the sniper.
     * 
     * @param sniper the sniper
     */
    protected void setSniper(ISniper sniper)
    {
        this.sniper = sniper;
    }

    /**
     * Returns the sniper.
     * 
     * @return the sniper
     */
    public ISniper getSniper()
    {
        return this.sniper;
    }
}
