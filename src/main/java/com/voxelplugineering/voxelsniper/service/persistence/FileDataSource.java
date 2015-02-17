package com.voxelplugineering.voxelsniper.service.persistence;

import java.io.File;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSource;

/**
 * An abstract data source based on a {@link File}.
 */
public abstract class FileDataSource implements DataSource
{

    protected final File file;

    /**
     * Sets up this {@link FileDataSource}
     * 
     * @param file The file
     */
    public FileDataSource(File file)
    {
        this.file = file;
    }

    /**
     * Gets the file that this data source is based in.
     * 
     * @return The file
     */
    public File getFile()
    {
        return this.file;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getName()
    {
        return Optional.of(this.file.getName());
    }

    /**
     * A builder for {@link DataSource}s from a file.
     */
    public static interface Builder
    {
        
        /**
         * Creates a new {@link DataSource} for the given file.
         * 
         * @param file The file
         * @return The {@link DataSource}
         */
        DataSource build(File file);
    }
    
}
