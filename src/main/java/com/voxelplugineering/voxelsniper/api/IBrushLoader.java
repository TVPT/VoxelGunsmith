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

import java.io.ObjectOutputStream;

import com.thevoxelbox.vsl.classloader.ASMClassLoader;

/**
 * Handles the loading of compiled or non-compiled {@link IBrush}s from various sources.
 */
public interface IBrushLoader
{

    /**
     * Loads a brush specified by the given byte array. The Class is loaded by the given class loader. The format for the serialization is as follows:
     * 4-bytes format version 4-bytes brush version {@link com.thevoxelbox.vsl.node.NodeGraph} serialized by java's {@link ObjectOutputStream}
     * 
     * @param classLoader the class loader to use to load the compiled class, cannot be null
     * @param serialized the serialized version of the brush, cannot be null
     * @return the compiled class
     */
    Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, byte[] serialized);

    /**
     * Loads the specified brush from the default source for this brush loader. The class is loaded by the given class loader.
     * 
     * @param classLoader the classloader to use to load the compiled class, cannot be null
     * @param identifier the identifier of the brush to search the default source for, cannot be null
     * @return the compiled class
     */
    Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, String identifier);

}
