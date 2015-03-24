package com.voxelplugineering.voxelsniper.service.persistence;

import java.io.IOException;

import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;

/**
 * A {@link DataSource} which is backed by a stream based interface.
 */
public abstract class StreamDataSource implements DataSource
{

    /**
     * Reads from this {@link DataSource} completely and returns the result of
     * the read as a byte array.
     * 
     * @return The data
     * @throws IOException If an error occurred while reading
     */
    public abstract byte[] read() throws IOException;

    /**
     * Writes the given byte array of data to this {@link DataSource}.
     * 
     * @param data The data
     * @throws IOException If an error occurred while writing
     */
    public abstract void write(byte[] data) throws IOException;

}
