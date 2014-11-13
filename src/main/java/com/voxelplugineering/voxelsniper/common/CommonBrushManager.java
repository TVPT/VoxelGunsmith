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
package com.voxelplugineering.voxelsniper.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thevoxelbox.vsl.classloader.ASMClassLoader;
import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.api.IBrushLoader;
import com.voxelplugineering.voxelsniper.api.IBrushManager;

public class CommonBrushManager implements IBrushManager
{

    IBrushManager parent = null;
    ASMClassLoader classLoader = null;
    Map<String, Class<? extends IBrush>> brushes = new HashMap<String, Class<? extends IBrush>>();
    List<IBrushLoader> loaders = new ArrayList<IBrushLoader>();

    public CommonBrushManager()
    {
        if (Gunsmith.getDefaultBrushLoader() != null)
        {
            loaders.add(Gunsmith.getDefaultBrushLoader());
        } else
        {
            System.out.println("WARNING: Created Brush Manager before default BrushLoader was set.");
        }
        this.classLoader = new ASMClassLoader();
    }

    public CommonBrushManager(IBrushManager parent)
    {
        this();
        setParent(parent);
    }

    public void init()
    {

    }

    public void stop()
    {
        brushes.clear();
    }

    public void restart()
    {
        stop();
        init();
    }

    public void addLoader(IBrushLoader loader)
    {
        this.loaders.add(0, loader);
    }

    public void loadBrush(String identifier, Class<? extends IBrush> clazz)
    {
        //TODO: Check version if already loaded
        this.brushes.put(identifier, clazz);
    }

    public void loadBrush(String identifier)
    {
        Class<? extends IBrush> cls = null;
        for (Iterator<IBrushLoader> iter = this.loaders.iterator(); iter.hasNext() && cls == null;)
        {
            IBrushLoader loader = iter.next();
            cls = loader.loadBrush(classLoader, identifier);
        }
        if (cls != null)
        {
            loadBrush(identifier, cls);
        }
    }

    public IBrush getNewBrushInstance(String identifier)
    {
        Class<? extends IBrush> br = this.brushes.get(identifier);
        if (br == null)
        {
            if (this.parent != null)
            {
                return this.parent.getNewBrushInstance(identifier);
            }
            return null;
        }
        try
        {
            return br.newInstance();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setParent(IBrushManager parent)
    {
        this.parent = parent;
    }

    @Override
    public ASMClassLoader getClassLoader()
    {
        return this.classLoader;
    }

}
