package com.voxelplugineering.voxelsniper.entity;

import com.voxelplugineering.voxelsniper.api.entity.Entity;
import com.voxelplugineering.voxelsniper.registry.WeakWrapper;


/**
 * An abstract entity.
 *
 * @param <T> The underlying entity type
 */
public abstract class AbstractEntity<T> extends WeakWrapper<T> implements Entity
{
    public AbstractEntity(T value)
    {
        super(value);
    }

}
