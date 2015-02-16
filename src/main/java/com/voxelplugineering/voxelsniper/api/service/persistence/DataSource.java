package com.voxelplugineering.voxelsniper.api.service.persistence;

import java.io.IOException;

import com.google.common.base.Optional;

/**
 * Represents a source which {@link DataContainer}s may be written to/read from.
 */
public interface DataSource
{

    /**
     * Writes the given object which implements {@link DataSerializable} to this
     * source as if by passing {@link DataSerializable#toContainer()} into
     * {@link #write(DataContainer)} .
     * 
     * @param serial The object
     * @throws IOException If there is a problem writing to the source
     */
    void write(DataSerializable serial) throws IOException;

    /**
     * Writes the given {@link DataContainer} to this source.
     * 
     * @param container The container to write
     * @throws IOException If there is a problem writing to the source
     */
    void write(DataContainer container) throws IOException;

    /**
     * Reads this source entirely into a DataContainer. This will attempt to
     * read the entire source into a single {@link DataContainer}.
     * 
     * @return The new data container
     * @throws IOException If there is a problem writing to the source
     */
    DataContainer read() throws IOException;

    /**
     * Reads this source into a {@link DataContainer} and loads it into the
     * given object by calling
     * {@link DataSerializable#fromContainer(DataContainer)}.
     * 
     * @param object The object to load the data into
     * @return The same object
     * @throws IOException If there is a problem writing to the source
     */
    <T extends DataSerializable> T read(T object) throws IOException;

    /**
     * Gets the name of this data source, if available.
     * 
     * @return The name
     */
    Optional<String> getName();

}
