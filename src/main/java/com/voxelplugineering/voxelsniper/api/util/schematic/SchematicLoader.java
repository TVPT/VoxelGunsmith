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
package com.voxelplugineering.voxelsniper.api.util.schematic;

import java.io.File;

import com.voxelplugineering.voxelsniper.shape.MaterialShape;

/**
 * A schematic loader is an interface for loading or saving regions of the world to a persistent storage. TODO: better persistence abstraction TODO:
 * need a more complete representation for a region of a world (w/ entities and tile entites mostly)
 */
public interface SchematicLoader
{

    /**
     * Loads a world region from the given file. TODO: need a more abstract data soruce
     * 
     * @param file The file to load from
     * @return The world region
     */
    MaterialShape load(File file);

    /**
     * Saves a world region to file.
     * 
     * @param file The file to save to
     * @param shape The region to save
     */
    void save(File file, MaterialShape shape);

}
