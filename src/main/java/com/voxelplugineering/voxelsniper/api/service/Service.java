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
package com.voxelplugineering.voxelsniper.api.service;

/**
 * Represents a service, which is a portion of the system which may be started and stopped. It has
 * priority and implicit dependencies (TODO to be made explicit).
 */
public interface Service
{

    /**
     * Gets the targeted class of this service.
     * 
     * @return The target class
     */
    Class<?> getTargetedService();

    /**
     * Gets the name of the service.
     * 
     * @return The name
     */
    String getName();

    /**
     * Gets the priority of this service. Services will be sorted lowest to highest for
     * initialization and the inverse for shutdown.
     * 
     * @return The priority
     */
    int getPriority();

    /**
     * Gets whether this service has been started.
     * 
     * @return is started
     */
    boolean isStarted();

    /**
     * Starts this service.
     */
    void start();

    /**
     * Stops this service.
     */
    void stop();

}
