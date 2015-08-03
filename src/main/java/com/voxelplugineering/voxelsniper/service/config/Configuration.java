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

import com.voxelplugineering.voxelsniper.service.Service;

import java.io.File;
import java.io.IOException;

/**
 * A storage space for configuration.
 */
public interface Configuration extends Service
{

    /**
     * Registers the given configuration container.
     * 
     * @param cls The configuration container class
     */
    void register(Class<?> cls);

    /**
     * Initializes available configuration containers into the given directory. This will load
     * configuration values from config files if they exist, and create new files if they do not.
     * 
     * @param dir The directory
     * @param includeHidden If this should include hidden configuration
     * @throws IOException If there is an issue writing or reading the configuration files
     */
    void initialize(File dir, boolean includeHidden) throws IOException;

    /**
     * Saves all available configuration containers into the given directory.
     * 
     * @param dir The directory
     * @param includeHidden If this should include hidden configuration
     * @throws IOException If there is an issue writing the configuration files
     */
    void save(File dir, boolean includeHidden) throws IOException;

    /**
     * Loads all available configuration containers from the given directory.
     * 
     * @param dir The directory
     * @throws IOException If there is an issue reading the configuration files
     */
    void load(File dir) throws IOException;

}
