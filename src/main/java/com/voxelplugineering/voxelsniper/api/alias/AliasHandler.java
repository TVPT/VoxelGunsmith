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
package com.voxelplugineering.voxelsniper.api.alias;

import java.util.Set;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.service.Buildable;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;

/**
 * A handler for targeted {@link AliasRegistry}s.
 */
public interface AliasHandler extends DataSerializable, Buildable
{

    /**
     * Returns a Set of all valid targets in this handler.
     * 
     * @return all valid targets
     */
    Set<String> getValidTargets();

    /**
     * Gets Gets the owner of this alias handler.
     * 
     * @return The owner
     */
    AliasOwner getOwner();

    /**
     * Returns a the {@link AliasRegistry} by the given name.
     * 
     * @param target the name
     * @return the registry for that name
     */
    Optional<AliasRegistry> getRegistry(String target);

    /**
     * Returns whether this handler has the given target.
     * 
     * @param target The target to check
     * @return Whether the target exists
     */
    boolean hasTarget(String target);

    /**
     * Registers a new target.
     * 
     * @param target The new target name
     * @return The newly created {@link AliasRegistry}
     */
    AliasRegistry registerTarget(String target);

}
