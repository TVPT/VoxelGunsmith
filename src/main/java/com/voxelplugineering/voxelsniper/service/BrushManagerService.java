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

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.brush.Brush;
import com.voxelplugineering.voxelsniper.brush.BrushInfo;
import com.voxelplugineering.voxelsniper.brush.BrushInstance;
import com.voxelplugineering.voxelsniper.brush.BrushManager;
import com.voxelplugineering.voxelsniper.brush.BrushWrapper;
import com.voxelplugineering.voxelsniper.brush.GlobalBrushManager;
import com.voxelplugineering.voxelsniper.util.Context;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;

/**
 * A service containing a {@link BrushManager}.
 */
public class BrushManagerService extends AbstractService implements GlobalBrushManager
{

    private final BrushManager wrapped;

    /**
     * Creates a new {@link BrushManagerService}.
     * 
     * @param context The context
     * @param manager The manager to use within this service
     */
    public BrushManagerService(Context context, BrushManager manager)
    {
        super(context);
        this.wrapped = manager;
    }

    @Override
    protected void _init()
    {

    }

    @Override
    protected void _shutdown()
    {

    }

    @Override
    public void loadBrush(String identifier, Brush graph)
    {
        check("loadBrush");
        this.wrapped.loadBrush(identifier, graph);
    }

    @Override
    public Optional<BrushWrapper> getBrush(String identifier)
    {
        check("getBrush");
        return this.wrapped.getBrush(identifier);
    }

    @Override
    public void setParent(BrushManager parent)
    {
        throw new UnsupportedOperationException("Cannot set the parent of the global brush manager.");
    }

    @Override
    public BrushManager getParent()
    {
        return null;
    }

    @Override
    public Collection<BrushWrapper> getBrushes()
    {
        return this.wrapped.getBrushes();
    }

    @Override
    public void consume(Class<?> cls)
    {
        if (!Brush.class.isAssignableFrom(cls))
        {
            GunsmithLogger.getLogger().warn("Class " + cls.getName() + " was annotated with @BrushInfo but is not a Brush");
            return;
        }
        try
        {
            Brush brush = (Brush) cls.newInstance();
            BrushInfo info = cls.getAnnotation(BrushInfo.class);
            GunsmithLogger.getLogger().debug("Loaded brush " + info.name() + " from " + cls.getName());
            loadBrush(info.name(), brush);
            // Inject instance fields
            BrushWrapper bw = getBrush(info.name()).get();
            for (Field f : cls.getDeclaredFields())
            {
                if (f.isAnnotationPresent(BrushInstance.class) && BrushWrapper.class.equals(f.getType()))
                {
                    f.setAccessible(true);
                    f.set(brush, bw);
                }
            }
        } catch (InstantiationException e)
        {
            GunsmithLogger.getLogger().warn("Class " + cls.getName() + " was annotated with @BrushInfo but has no accessible no-args constructor");
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e)
        {
            GunsmithLogger.getLogger().warn("Class " + cls.getName() + " was annotated with @BrushInfo but has no accessible no-args constructor");
            e.printStackTrace();
            return;
        }
    }

}
