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
package com.voxelplugineering.voxelsniper;

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.expansion.Expansion;
import com.voxelplugineering.voxelsniper.expansion.ExpansionManager;
import com.voxelplugineering.voxelsniper.service.Service;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * The core service and expansion manager.
 */
public final class Gunsmith extends ServiceManager implements ExpansionManager
{

    /**
     * Gets the {@link ServiceManager} which is in change of the registration, initialization, and shutdown of all {@link Service}s.
     *
     * @return The service manager
     */
    public static ServiceManager getServiceManager()
    {
        return Holder.INSTANCE;
    }

    /**
     * Gets the {@link ExpansionManager} which is in change of the registration of all {@link Expansion}s.
     *
     * @return The expansion manager
     */
    public static ExpansionManager getExpansionManager()
    {
        return Holder.INSTANCE;
    }

    /**
     * Gets the main thread of the system.
     *
     * @return The main thread
     */
    public static Thread getMainThread()
    {
        return Holder.INSTANCE.mainThread;
    }

    /**
     * Gets the system {@link ClassLoader}.
     *
     * @return The class loader
     */
    public static ClassLoader getClassLoader()
    {
        return Holder.INSTANCE.classloader;
    }

    private ClassLoader classloader;
    private List<Expansion> expansions;
    private Thread mainThread;

    private Gunsmith()
    {
        this.mainThread = Thread.currentThread();
        this.classloader = Gunsmith.class.getClassLoader();
        initExpansionManager();
        register(new CoreServiceProvider());
    }

    @Override
    public void start()
    {
        super.start();
    }

    @Override
    public void shutdown()
    {
        super.shutdown();
    }

    private void initExpansionManager()
    {
        this.expansions = Lists.newArrayList();
    }

    @Override
    public void registerExpansion(Expansion ex)
    {
        checkNotNull(ex);
        synchronized (this.expansions)
        {
            if (!this.expansions.contains(ex))
            {
                this.expansions.add(ex);
            }
        }
    }

    @Override
    public Collection<Expansion> getExpansions()
    {
        return this.expansions;
    }

    /**
     * An on demand holder for the Gunsmith instance.
     */
    private static final class Holder
    {

        protected static final Gunsmith INSTANCE = new Gunsmith();

        private Holder()
        {
        }
    }

}
