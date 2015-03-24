package com.voxelplugineering.voxelsniper.service.persistence;

import java.io.IOException;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceBuilder;

/**
 * A {@link StreamDataSource} which ouputs data to {@link System#out} as a UFT-8
 * encoded String.
 * <p>
 * {@link #read()} is no supported.
 * </p>
 */
public class StandardOutDataSource extends StreamDataSource
{

    /**
     * A builder for {@link StandardOutDataSource}s.
     */
    public static final DataSourceBuilder<StandardOutDataSource> BUILDER = new DataSourceBuilder<StandardOutDataSource>()
    {

        @Override
        public Optional<StandardOutDataSource> build(DataContainer args)
        {
            return Optional.of(new StandardOutDataSource());
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getName()
    {
        return Optional.of("System.out");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] read() throws IOException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] data) throws IOException
    {
        String s = new String(data, "UTF-8");
        System.out.print(s);
        System.out.println();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists()
    {
        return true;
    }

}
