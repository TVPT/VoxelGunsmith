package com.voxelplugineering.voxelsniper.api.brushes;

import com.google.common.base.Optional;


public interface BrushLoader
{

    Optional<Brush> loadBrush(String identifier);

}
