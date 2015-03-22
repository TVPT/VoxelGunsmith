package com.voxelplugineering.voxelsniper.service.persistence;

import java.io.IOException;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceBuilder;


public class StandardOutDataSource extends StreamDataSource
{

    public static final DataSourceBuilder BUILDER = new DataSourceBuilder()
    {
        
        @Override
        public Optional<DataSource> build(DataContainer args)
        {
            return Optional.<DataSource>of(new StandardOutDataSource());
        }
    };

    @Override
    public Optional<String> getName()
    {
        return Optional.of("System.out");
    }

    @Override
    public byte[] read() throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(byte[] data) throws IOException
    {
        String s = new String(data, "UTF-8");
        System.out.print(s);
        System.out.println();
    }

    @Override
    public boolean exists()
    {
        return true;
    }

}
