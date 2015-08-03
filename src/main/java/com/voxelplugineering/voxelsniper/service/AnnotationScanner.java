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

import com.voxelplugineering.voxelsniper.service.meta.AnnotationConsumer;

import java.lang.annotation.Annotation;
import java.net.URLClassLoader;

/**
 * A service for scanning over all classes in a classloader and detecting classes annotated with
 * registered annotations.
 */
public interface AnnotationScanner extends Service
{

    /**
     * Registers a consumer for a particular annotation. All classes in scanned classloaders which
     * are annotated with this annotation will be passed to the given consumer.
     * 
     * @param target The targeted annotation
     * @param consumer The annotation consumer
     */
    void register(Class<? extends Annotation> target, AnnotationConsumer consumer);

    /**
     * Scans the given {@link URLClassLoader} for registered annotations.
     * 
     * @param loader The class loader to scan
     */
    void scanClassPath(URLClassLoader loader);

    /**
     * Adds an exclusion to the scanner. All classes with fully qualified names which start with an
     * item in the exclusion list will be ignored.
     * 
     * <p>Packages should be deliminated with a '/' for the purposes of this exclusion list (eg.
     * 'com/sun/' would exclude all classes in the com.sun package).</p>
     * 
     * @param exc The new scanner exclusion
     */
    void addScannerExclusion(String exc);

}
