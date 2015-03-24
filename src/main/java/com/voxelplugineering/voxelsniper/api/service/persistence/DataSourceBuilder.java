package com.voxelplugineering.voxelsniper.api.service.persistence;

import com.google.common.base.Optional;

/**
 * Represents a builder which creates a {@link DataSource}.
 * 
 * @param <T> The data source type
 */
public interface DataSourceBuilder<T extends DataSource>
{

    /**
     * Builds a new instance of the data source with arguments from the given
     * {@link DataContainer}.
     * 
     * @param args The arguments
     * @return The new data source
     */
    Optional<T> build(DataContainer args);

}
