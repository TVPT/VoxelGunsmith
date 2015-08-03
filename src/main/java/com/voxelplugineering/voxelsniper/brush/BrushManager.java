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
package com.voxelplugineering.voxelsniper.brush;

import com.google.common.base.Optional;

/**
 * Handles the registration of {@link Brush}es. Also contains an ordered list of loaders which is
 * traversed to load named brushes. A brush manager may be connected to a particular user in a
 * multi-user environment.
 */
public interface BrushManager
{

    /**
     * Gets the parent {@link BrushManager}. When a request for a brush instance is made and it is
     * not found within this manager the request is passed up to the parent manager.
     * 
     * @return The parent brush manager
     */
    BrushManager getParent();

    /**
     * Sets the parent {@link BrushManager}. When a request for a brush instance is made and it is
     * not found within this manager the request is passed up to the parent manager.
     * 
     * @param parent the parent brush loader
     */
    void setParent(BrushManager parent);

    /**
     * Loads the given brush into this manager. If the brush had been previously loaded then a check
     * is done of the brush version and the copy with the higher version is kept loaded.
     * 
     * @param identifier the brush name, cannot be null or empty
     * @param brush The node graph
     */
    void loadBrush(String identifier, Brush brush);

    /**
     * Returns a new instance of the brush with the given name if it has been previously loaded into
     * this manager. If the brush is not found in this manager the request is passed up to the
     * parent brush manager.
     * 
     * @param identifier the brush name to be loaded, cannot be null or empty
     * @return an instance of the brush
     */
    Optional<BrushWrapper> getBrush(String identifier);

    /**
     * Gets all brushes registered within this {@link BrushManager}.
     * 
     * @return All registered brushes
     */
    Iterable<BrushWrapper> getBrushes();

}
