package com.voxelplugineering.voxelsniper.api.service.persistence;

/**
 * A type which may be serialized from/to a {@link DataContainer}. Types
 * implementing this interface may optionally implement a static method with the
 * following signature:
 * <p>
 * {@code public static T buildFromContainer(DataContainer container)}
 * </p>
 * <p>
 * This static method will be used to construct new instances of the
 * implementing type based on a {@link DataContainer}.
 * </p>
 */
public interface DataSerializable
{

    /**
     * Loads the data from the given {@link DataContainer} to this object.
     * 
     * @param container The container to load from
     */
    void fromContainer(DataContainer container);

    /**
     * Writes the data of this object to a new {@link DataContainer}.
     * 
     * @return The data container representing this object
     */
    DataContainer toContainer();

}
