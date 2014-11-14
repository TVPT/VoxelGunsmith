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
package com.voxelplugineering.voxelsniper.api;

/**
 * A storage space for configuration.
 */
public interface IConfiguration
{

    /**
     * Sets a value in configuration.
     * 
     * @param name
     *            the name of the configuration key
     * @param value
     *            the new value
     */
    void set(String name, Object value);

    /**
     * Returns a value from configuration.
     * 
     * @param name
     *            the name or key to search and return from the configuration storage
     * @return the value stored in configuration, or null if the key is not found
     */
    Object get(String name);

    /**
     * Attempts to register the container with this configuration storage. This will load all values from the fields of the given object and place
     * them into the configuration. It will also keep the Class of the Object as a reference to the fields contained within this container in order to
     * store or load them from/to a source.
     * <p>
     * If any of the fields have names matching keys already stored within this configuration storage the values in this configuration storage will be
     * overwritten.
     * 
     * @param container
     *            the new container to load.
     */
    void registerContainer(Object container);

    /**
     * Returns the Class of a previously registered container. Returns null if no container matching the given name has been registered previously.
     * 
     * @param containerName
     * @return the container class
     */
    Class<?> getContainer(String containerName);

    /**
     * Returns an array of the Classes all previously registered containers.
     * 
     * @return an array of Classes
     */
    Class<?>[] getContainers();

}
