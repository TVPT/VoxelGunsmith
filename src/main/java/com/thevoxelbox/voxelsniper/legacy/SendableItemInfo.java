package com.thevoxelbox.voxelsniper.legacy;

import java.io.Serializable;

public class SendableItemInfo implements Serializable
{

    private final int ID;
    private final int metaData;

    public SendableItemInfo(int id, int metaData)
    {
        this.ID = id;
        this.metaData = metaData;
    }

    public int getID()
    {
        return ID;
    }

    public int getMetaData()
    {
        return metaData;
    }

}
