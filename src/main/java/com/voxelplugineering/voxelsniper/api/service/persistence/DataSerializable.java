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
package com.voxelplugineering.voxelsniper.api.service.persistence;

/**
 * A type which may be serialized from/to a {@link DataContainer}. Types
 * implementing this interface may optionally implement a static method with the
 * following signature:
 * <p>
 * {@code public static T buildFromContainer(DataContainer container)}
 * </p>
 * <p>
 * This static method will be used to construct new instances of the
 * implementing type based on a {@link DataContainer}.
 * </p>
 */
public interface DataSerializable
{

    /**
     * Loads the data from the given {@link DataContainer} to this object.
     * 
     * @param container The container to load from
     */
    void fromContainer(DataContainer container);

    /**
     * Writes the data of this object to a new {@link DataContainer}.
     * 
     * @return The data container representing this object
     */
    DataContainer toContainer();

}
