package com.voxelplugineering.voxelsniper.service.persistence;

import java.io.IOException;

import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;


public abstract class StreamDataSource implements DataSource
{

    public abstract byte[] read() throws IOException;
    
    public abstract void write(byte[] data) throws IOException;
    
}
