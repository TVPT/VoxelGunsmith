/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 The Voxel Plugineering Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.voxelplugineering.voxelsniper.api.world;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.entity.Entity;
import com.voxelplugineering.voxelsniper.api.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.biome.Biome;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;

/**
 * Represents a world.
 */
public interface World extends BlockVolume
{

    /**
     * Gets the name of the world.
     * 
     * @return The name
     */
    String getName();

    /**
     * Gets the material registry for this world.
     * 
     * @return The material registry
     */
    MaterialRegistry<?> getMaterialRegistry();

    /**
     * Gets a collection of the loaded entities for this world.
     * 
     * @return The loaded entities
     */
    Iterable<Entity> getLoadedEntities();

    /**
     * Gets the chunk at the given xyz position. In minecraft the y position is ignored.
     * 
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     * @return The chunk
     */
    Optional<Chunk> getChunk(int x, int y, int z);

    /**
     * Gets the chunk at the given vector position. In minecraft the Y value of the vector is ignored.
     * 
     * @param vector The vector position
     * @return The chunk
     */
    Optional<Chunk> getChunk(Vector3i vector);

    /**
     * Gets the biome at the given coordinates.
     * 
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     * @return The biome
     */
    Optional<Biome> getBiome(int x, int y, int z);

    /**
     * Gets the biome at the given location.
     * 
     * @param location The location
     * @return The biome
     */
    Optional<Biome> getBiome(Location location);

    /**
     * Gets the biome at the given vector position within this world.
     * 
     * @param vector The vector position
     * @return The biome
     */
    Optional<Biome> getBiome(Vector3i vector);

    /**
     * Sets the biome at the given coordinates to the given biome.
     * 
     * @param biome The new biome
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     */
    void setBiome(Biome biome, int x, int y, int z);

    /**
     * Sets the biome at the given location to the given biome.
     * 
     * @param biome The new biome
     * @param location The location
     */
    void setBiome(Biome biome, Location location);

    /**
     * Sets the biome at the given vector position to the given biome.
     * 
     * @param biome The new biome
     * @param vector The vector position
     */
    void setBiome(Biome biome, Vector3i vector);

    /**
     * Creates a new {@link MaterialShape} and populates it with the current contents of the world.
     * 
     * @param origin The origin
     * @param shape The shape
     * @return The new material shape
     */
    MaterialShape getShapeFromWorld(Location origin, Shape shape);

}
