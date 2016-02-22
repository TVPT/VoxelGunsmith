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

import com.google.common.collect.Lists;
import com.voxelplugineering.voxelsniper.entity.EntityType;
import com.voxelplugineering.voxelsniper.registry.WeakRegistry;
import com.voxelplugineering.voxelsniper.service.registry.EntityRegistry;
import com.voxelplugineering.voxelsniper.util.Context;

import java.util.Optional;

public class EntityRegistryService<T> extends AbstractService implements EntityRegistry<T> {

    private WeakRegistry<T, EntityType> registry;

    public EntityRegistryService(Context context) {
        super(context);
    }

    @Override
    protected void _init() {
        this.registry = new WeakRegistry<>();
        this.registry.setCaseSensitiveKeys(false);
    }

    @Override
    protected void _shutdown() {
        this.registry = null;
    }

    @Override
    public Optional<EntityType> getEntityType(String name) {
        check("getEntityType");
        return this.registry.get(name);
    }

    @Override
    public Optional<EntityType> getEntityType(T type) {
        check("getEntityType");
        return this.registry.get(type);
    }

    @Override
    public void registerEntityType(String name, T object, EntityType entityType) {
        check("registerEntityType");
        this.registry.register(name, object, entityType);
    }

    @Override
    public Iterable<EntityType> getEntityTypes() {
        check("getEntityTypes");
        return Lists.newArrayList(this.registry.values());
    }
}
