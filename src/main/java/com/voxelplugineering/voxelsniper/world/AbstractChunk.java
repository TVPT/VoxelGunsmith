package com.voxelplugineering.voxelsniper.world;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Chunk;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.registry.WeakWrapper;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * An abstract chunk.
 * 
 * @param <T> The type of the underlying chunk.
 */
public abstract class AbstractChunk<T> extends WeakWrapper<T> implements Chunk
{

    private final World world;

    /**
     * Sets up an {@link AbstractChunk}.
     * 
     * @param chunk The chunk to wrap
     * @param world The parent world
     */
    public AbstractChunk(T chunk, World world)
    {
        super(chunk);
        this.world = world;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Block> getBlock(Location location)
    {
        if (location.getWorld() != this.world)
        {
            return Optional.absent();
        }
        return getBlock(location.getFlooredX(), location.getFlooredY(), location.getFlooredZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Block> getBlock(Vector3i vector)
    {
        return getBlock(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlock(Material material, Location location)
    {
        if (location.getWorld() != this.world)
        {
            return;
        }
        setBlock(material, location.getFlooredX(), location.getFlooredY(), location.getFlooredZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlock(Material material, Vector3i vector)
    {
        setBlock(material, vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public World getWorld()
    {
        return this.world;
    }

}
