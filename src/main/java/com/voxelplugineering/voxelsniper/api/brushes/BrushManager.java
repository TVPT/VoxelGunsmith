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
package com.voxelplugineering.voxelsniper.api.brushes;

import com.google.common.base.Optional;
import com.thevoxelbox.vsl.node.NodeGraph;
import com.voxelplugineering.voxelsniper.api.Manager;
import com.voxelplugineering.voxelsniper.brushes.BrushNodeGraph;

/**
 * Handles the registration of {@link NodeGraph}s. Also contains an ordered list of loaders which is traversed to load named brushes. Contains a
 * reference to a ClassLoader to be used by all associated loaders. A brush manager may be connected to a particular user in a multi-user environment.
 * Therefore, by connecting the classloader to the brush manager when the manager is garbage collected (eg. when the user disconnects from the
 * environment) the classloader (and by extension all classes loaded explicitly for this manager) are also garbage connected.
 */
public interface BrushManager extends Manager
{

    /**
     * Loads the given brush into this manager. If the brush had been previously loaded then a check is done of the brush version and the copy with
     * the higher version is kept loaded.
     * 
     * @param identifier the brush name, cannot be null or empty
     * @param graph The node graph
     */
    void loadBrush(String identifier, BrushNodeGraph graph);

    /**
     * Walks through registered loaders in order and attempts to load the brush with the given name.
     * 
     * @param identifier the brush name, cannot be null or empty
     */
    void loadBrush(String identifier);

    /**
     * Adds a loader to the list of loaders used by this manager for loading brushes by name. The loader is added top the end of the existing list of
     * loaders.
     * 
     * @param loader the new loader, cannot be null
     */
    void addLoader(BrushLoader loader);

    /**
     * Returns a new instance of the brush with the given name if it has been previously loaded into this manager. If the brush is not found in this
     * manager the request is passed up to the parent brush manager.
     * 
     * @param identifier the brush name to be loaded, cannot be null or empty
     * @return an instance of the brush
     */
    Optional<BrushNodeGraph> getBrush(String identifier);

    /**
     * Sets the parent brush loader, when a request for a brush instance is made and it is not found within this manager the request is passed up to
     * the parent manager.
     * 
     * @param parent the parent brush loader
     */
    void setParent(BrushManager parent);

}
