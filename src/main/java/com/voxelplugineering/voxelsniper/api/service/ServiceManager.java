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

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.logging.Logger;

/**
 * A manager for {@link Service}s, handling registration of services and
 * {@link ServiceProvider}s as well as initialization and shutdown of all
 * services.
 */
public interface ServiceManager
{

    /**
     * Initializes all services.
     */
    void init();

    /**
     * Shuts down all services.
     */
    void stop();

    /**
     * Registers a {@link ServiceProvider} which provides services.
     * 
     * @param provider The new provider
     */
    void registerServiceProvider(ServiceProvider provider);

    /**
     * Registers a service by name which may then be referenced by builders and
     * initialization hooks.
     * 
     * @param service The service name
     */
    void registerService(String service);

    /**
     * Gets whether a service has been built and initialized.
     * 
     * @param service The service to check
     * @return Is started
     */
    boolean hasService(String service);

    /**
     * Gets the service with the given name. If the service is available then it
     * is implied to be built but may not be initialized yet.
     * 
     * @param service The service name
     * @return The service, if available
     */
    Optional<Service> getService(String service);

    /**
     * Shuts down the given service and dereferences it.
     * 
     * @param service The service
     */
    void stopService(Service service);

    /**
     * Sets this service to 'testing mode' where any requests for services which
     * are not found are created on the fly using the given provider. This is so
     * that classes using services such as the {@link Logger} to not error out
     * due to Gunsmith not having been initialized.
     * 
     * @param provider The provider to use for on the fly construction
     */
    void setTesting(ServiceProvider provider);

}
