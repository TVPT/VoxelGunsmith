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
package com.voxelplugineering.voxelsniper.brushes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thevoxelbox.vsl.classloader.ASMClassLoader;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.brushes.Brush;
import com.voxelplugineering.voxelsniper.api.brushes.BrushLoader;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;

/**
 * A standard brush manager.
 */
public class CommonBrushManager implements BrushManager
{

    /**
     * The parent brush manager, referenced by the brush getter if it cannot be found within this brush manager.
     */
    private BrushManager parent = null;
    /**
     * The classloader to be used by loaders associated with this manager.
     */
    private ASMClassLoader classLoader = null;
    /**
     * A map of brushes loaded in this manager.
     */
    private Map<String, Class<? extends Brush>> brushes = Maps.newHashMap();
    /**
     * An ordered list of loaders used to load brushes by name.
     */
    private List<BrushLoader> loaders = Lists.newArrayList();

    /**
     * Creates a new CommonBrushManager.
     */
    public CommonBrushManager()
    {
        if (Gunsmith.getDefaultBrushLoader() != null)
        {
            this.loaders.add(Gunsmith.getDefaultBrushLoader());
        } else
        {
            Gunsmith.getLogger().warn("Created Brush Manager before default BrushLoader was set.");
        }
        this.classLoader = new ASMClassLoader(Gunsmith.getPlatformProxy().getGunsmithClassLoader(), Gunsmith.getCompilerFactory());
    }

    /**
     * Creates a new CommonBrushManager with the subscribed parent
     *
     * @param parent the parent
     */
    public CommonBrushManager(BrushManager parent)
    {
        this();
        setParent(parent);
    }

    /**
     * {@inheritDoc}
     */
    public void init()
    {

    }

    /**
     * {@inheritDoc}
     */
    public void stop()
    {
        this.brushes.clear();
    }

    /**
     * {@inheritDoc}
     */
    public void restart()
    {
        stop();
        init();
    }

    /**
     * {@inheritDoc}
     */
    public void addLoader(BrushLoader loader)
    {
        this.loaders.add(0, loader);
    }

    /**
     * {@inheritDoc}
     */
    public void loadBrush(String identifier, Class<? extends Brush> clazz)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        checkNotNull(clazz, "Brush class cannot be null!");
        //TODO: Check version if already loaded
        this.brushes.put(identifier, clazz);
    }

    /**
     * {@inheritDoc}
     */
    public void loadBrush(String identifier)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        Class<? extends Brush> cls = null;
        for (Iterator<BrushLoader> iter = this.loaders.iterator(); iter.hasNext() && cls == null;)
        {
            BrushLoader loader = iter.next();
            cls = loader.loadBrush(this.classLoader, identifier);
        }
        if (cls != null)
        {
            loadBrush(identifier, cls);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Optional<Brush> getNewBrushInstance(String identifier)
    {
        checkNotNull(identifier, "Name cannot be null!");
        checkArgument(!identifier.isEmpty(), "Name cannot be empty");
        Class<? extends Brush> br = this.brushes.get(identifier);
        if (br == null)
        {
            if (this.parent != null)
            {
                return this.parent.getNewBrushInstance(identifier);
            }
            return Optional.absent();
        }
        System.out.println(identifier + " " + br.getName() + " " + br.getSuperclass().getName());
        for (Class<?> i : br.getInterfaces())
        {
            System.out.println("\t" + i.getName());
        }
        try
        {
            Brush brush = br.newInstance();
            return Optional.<Brush>of(brush);
        } catch (Throwable e)
        //catching all thrown errors and exceptions as things like a VerifyError are far more likely than in the conventional usage of newInstance
        // due the the custom bytecode within nodes and should not halt the execution
        {
            e.printStackTrace();
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    public void setParent(BrushManager parent)
    {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ASMClassLoader getClassLoader()
    {
        return this.classLoader;
    }

}
