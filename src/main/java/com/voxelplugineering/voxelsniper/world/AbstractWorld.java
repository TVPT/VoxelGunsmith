package com.voxelplugineering.voxelsniper.world;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Chunk;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.api.world.biome.Biome;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.registry.WeakWrapper;
import com.voxelplugineering.voxelsniper.shape.ComplexMaterialShape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * An abstract world.
 * 
 * @param <T> The specific world type
 */
public abstract class AbstractWorld<T> extends WeakWrapper<T> implements World
{

    public AbstractWorld(T value)
    {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Block> getBlock(Location location)
    {
        if (location.getWorld() != this)
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
        if (location.getWorld() != this)
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
    public Optional<Chunk> getChunk(Vector3i vector)
    {
        return getChunk(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Biome> getBiome(Location location)
    {
        if (location.getWorld() != this)
        {
            return Optional.absent();
        }
        return getBiome(location.getFlooredX(), location.getFlooredY(), location.getFlooredZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Biome> getBiome(Vector3i vector)
    {
        return getBiome(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBiome(Biome biome, Location location)
    {
        if (location.getWorld() != this)
        {
            return;
        }
        setBiome(biome, location.getFlooredX(), location.getFlooredY(), location.getFlooredZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBiome(Biome biome, Vector3i vector)
    {
        setBiome(biome, vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MaterialShape getShapeFromWorld(Location origin, Shape shape)
    {
        MaterialShape mat = new ComplexMaterialShape(shape, Gunsmith.getDefaultMaterialRegistry().getAirMaterial());
        for (int x = 0; x < shape.getWidth(); x++)
        {
            int ox = x + origin.getFlooredX() - shape.getOrigin().getX();
            for (int y = 0; y < shape.getHeight(); y++)
            {
                int oy = y + origin.getFlooredY() - shape.getOrigin().getY();
                for (int z = 0; z < shape.getLength(); z++)
                {
                    int oz = z + origin.getFlooredZ() - shape.getOrigin().getZ();
                    if (shape.get(x, y, z, false))
                    {
                        Optional<Block> block = getBlock(ox, oy, oz);
                        if (!block.isPresent())
                        {
                            shape.unset(x, y, z, false);
                        } else
                        {
                            mat.setMaterial(x, y, z, false, block.get().getMaterial());
                        }
                    }
                }
            }
        }
        return mat;
    }

}
