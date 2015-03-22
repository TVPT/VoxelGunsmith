package com.voxelplugineering.voxelsniper.api.service.persistence;

import com.google.common.base.Optional;

public interface DataSourceFactory
{

    Optional<DataSource> build(String name, DataContainer args);

    Optional<DataSource> build(String name);

    <T extends DataSource> Optional<T> build(Class<T> type);

    <T extends DataSource> Optional<T> build(Class<T> type, DataContainer args);

    <T extends DataSource> void register(String name, Class<T> type, DataSourceBuilder builder);

}
