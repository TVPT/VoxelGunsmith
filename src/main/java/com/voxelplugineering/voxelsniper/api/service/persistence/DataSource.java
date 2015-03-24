package com.voxelplugineering.voxelsniper.api.service.persistence;

import com.google.common.base.Optional;

/**
 * Represents a basic source of data for persistence.
 */
public interface DataSource
{

    /**
     * Gets the name of this source if it is available.
     * 
     * @return The name, if available
     */
    Optional<String> getName();

    /**
     * Gets whether the backing of this source exists.
     * 
     * @return If the source exists
     */
    boolean exists();

}
