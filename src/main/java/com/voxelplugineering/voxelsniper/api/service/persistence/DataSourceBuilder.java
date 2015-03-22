package com.voxelplugineering.voxelsniper.api.service.persistence;

import com.google.common.base.Optional;

public interface DataSourceBuilder
{

	Optional<DataSource> build(DataContainer args);
	
}
