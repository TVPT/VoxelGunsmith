package com.voxelplugineering.voxelsniper.api.service.persistence;

import com.google.common.base.Optional;

/**
 * A factory into which data source types may be registered along with their
 * {@link DataSourceBuilder}.
 */
public interface DataSourceFactory
{

    /**
     * Requests a new instance of a {@link DataSource} by name with the given
     * arguments.
     * 
     * @param name The source name
     * @param args The source arguments
     * @return A new instance of the data source, if successful
     */
    Optional<DataSource> build(String name, DataContainer args);

    /**
     * Requests a new instance of a {@link DataSource} by name.
     * 
     * @param name The source name
     * @return A new instance of the data source, if successful
     */
    Optional<DataSource> build(String name);

    /**
     * Requests a new instance of a {@link DataSource} from a class reference.
     * 
     * @param type The data source type
     * @return A new instance of the data source, if successful
     */
    <T extends DataSource> Optional<T> build(Class<T> type);

    /**
     * Requests a new instance of a {@link DataSource} from a class reference,
     * with the given arguments.
     * 
     * @param type The data source type
     * @param args The source arguments
     * @return A new instance of the data source, if successful
     */
    <T extends DataSource> Optional<T> build(Class<T> type, DataContainer args);

    /**
     * Registers a new {@link DataSource} to this factory with an associated
     * {@link DataSourceBuilder}.
     * 
     * @param name The data source name
     * @param type The data source class
     * @param builder The data source builder
     */
    <T extends DataSource> void register(String name, Class<T> type, DataSourceBuilder<T> builder);

}
