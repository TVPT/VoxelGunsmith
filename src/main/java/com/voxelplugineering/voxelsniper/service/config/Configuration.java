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
package com.voxelplugineering.voxelsniper.service.config;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.service.Service;
import com.voxelplugineering.voxelsniper.service.persistence.DataSerializable;

/**
 * A storage space for configuration.
 */
public interface Configuration extends DataSerializable, Service
{

    /**
     * Returns a value from configuration.
     * 
     * @param name the name or key to search and return from the configuration storage, cannot be
     *            null or empty
     * @return the value stored in configuration, or null if the key is not found
     */
    Optional<Object> get(String name);

    /**
     * Returns a value from configuration. The the type of the config value is not assignable to the
     * expectedType then {@link Optional#absent()} is returned.
     * 
     * @param name the name or key to search and return from the configuration storage, cannot be
     *            null or empty
     * @param expectedType The expected type of the config value
     * @param <T> The expected type
     * @return the value stored in configuration, or null if the key is not found
     */
    <T> Optional<T> get(String name, Class<T> expectedType);

    /**
     * Sets a value in configuration.
     * 
     * @param name the name of the configuration key, cannot be null or empty
     * @param value the new value, cannot be null
     */
    void set(String name, Object value);

    /**
     * Returns whether the given key exists within this configuration registry.
     * 
     * @param name the key to check, cannot be null or empty
     * @return whether the key exists
     */
    boolean has(String name);

    /**
     * Returns the Class of a previously registered container. Returns null if no container matching
     * the given name has been registered previously.
     * 
     * @param containerName the name of the container, cannot be null or empty
     * @return the container class
     */
    Optional<ConfigurationContainer> getContainer(String containerName);

    /**
     * Attempts to register the container with this configuration storage. This will load all values
     * from the fields of the given object and place them into the configuration. It will also keep
     * the Class of the Object as a reference to the fields contained within this container in order
     * to store or load them from/to a source. <p> If any of the fields have names matching keys
     * already stored within this configuration storage the values in this configuration storage
     * will be overwritten. </p>
     * 
     * @param container the new container to load, cannot be null
     * @param <T> The container type
     */
    <T extends ConfigurationContainer> void registerContainer(Class<T> container);

    /**
     * Returns an array of the Classes all previously registered containers.
     * 
     * @return an array of Classes
     */
    ConfigurationContainer[] getContainers();

    /**
     * Refreshes the given container.
     * 
     * @param containerName The container to refresh
     */
    void refreshContainer(String containerName);

    /**
     * Refreshes the values of all registered configuration containers. <P> TODO need a priority for
     * containers, probably just the registration order </p>
     */
    void refreshAllContainers();

}
