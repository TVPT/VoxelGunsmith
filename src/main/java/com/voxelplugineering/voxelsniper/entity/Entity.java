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
package com.voxelplugineering.voxelsniper.entity;

import com.voxelplugineering.voxelsniper.util.Nameable;
import com.voxelplugineering.voxelsniper.util.math.Vector3d;
import com.voxelplugineering.voxelsniper.world.Location;
import com.voxelplugineering.voxelsniper.world.World;

import java.util.UUID;

/**
 * Represents an entity within a world.
 */
public interface Entity extends Nameable
{

    /**
     * Gets the {@link UUID} for this entity.
     * 
     * @return The UUID
     */
    UUID getUniqueId();

    /**
     * Gets the world that this player is currently within.
     * 
     * @return This player's world
     */
    World getWorld();

    /**
     * Gets the EntityType for this entity.
     * 
     * @return The entity type
     */
    EntityType getType();

    /**
     * Gets the location of this entity.
     * 
     * @return The location
     */
    Location getLocation();

    /**
     * Sets the location of this entity.
     * 
     * @param world The world
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     */
    void setLocation(World world, double x, double y, double z);

    /**
     * Sets the location of this entity.
     * 
     * @param newLocation The new location
     */
    default void setLocation(Location newLocation)
    {
        setLocation(newLocation.getWorld(), newLocation.getX(), newLocation.getY(), newLocation.getZ());
    }

    /**
     * Sets the location of this entity.
     * 
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     */
    default void setPosition(double x, double y, double z)
    {
        setLocation(getWorld(), x, y, z);
    }

    /**
     * Gets the rotation of this entity. The vector components represent pitch, yaw, and roll in
     * order.
     * 
     * @return The rotation
     */
    Vector3d getRotation();

    /**
     * Gets the yaw rotation (Y-axis rotation) of this entity.
     * 
     * @return The yaw
     */
    double getYaw();

    /**
     * Gets the pitch rotation (X-axis rotation) of this entity.
     * 
     * @return The pitch
     */
    double getPitch();

    /**
     * Gets the roll rotation (Z-axis rotation) of this entity.
     * 
     * @return The roll
     */
    double getRoll();

    /**
     * Sets the rotation of this entity.
     * 
     * @param pitch The pitch
     * @param yaw The yaw
     * @param roll The roll
     */
    void setRotation(double pitch, double yaw, double roll);

    /**
     * Sets the rotation of this entity. The vector components represent pitch, yaw, and roll in
     * order.
     * 
     * @param rotation The new rotation
     */
    default void setRotation(Vector3d rotation)
    {
        setRotation(rotation.getX(), rotation.getY(), rotation.getZ());
    }

    /**
     * Attempts to remove this entity from the world.
     * 
     * @return Was successful
     */
    boolean remove();

}
