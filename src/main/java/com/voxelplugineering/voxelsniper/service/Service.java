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
package com.voxelplugineering.voxelsniper.service;

import com.voxelplugineering.voxelsniper.util.Contextable;

/**
 * Represents a service, which is a portion of the system which may be started and stopped. It has
 * priority and implicit dependencies (TODO to be made explicit).
 */
public interface Service extends Contextable
{

    /**
     * Gets whether this service has been started.
     * 
     * @return Is started
     */
    boolean isInitialized();

    /**
     * Starts this service.
     */
    void start();

    /**
     * Stops this service.
     */
    void shutdown();

    /**
     * Adds a service as depending on this service. Dependent services will be shutdown before this
     * service.
     * 
     * @param service The service
     * @param <T> The service type
     */
    <T extends Service> void addDependent(T service);

}
