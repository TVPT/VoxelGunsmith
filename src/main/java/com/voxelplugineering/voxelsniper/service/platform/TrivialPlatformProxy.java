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
package com.voxelplugineering.voxelsniper.service.platform;

import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.service.AbstractService;
import com.voxelplugineering.voxelsniper.util.Context;

import java.io.File;
import java.net.URISyntaxException;

/**
 * A trivial {@link PlatformProxy} which is used in the case of no platform being present, such as
 * when tests are being run.
 */
public class TrivialPlatformProxy extends AbstractService implements PlatformProxy
{

    /**
     * Creates a new {@link TrivialPlatformProxy}.
     * 
     * @param context The context
     */
    public TrivialPlatformProxy(Context context)
    {
        super(context);
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
    public String getPlatformName()
    {
        return "none";
    }

    @Override
    public String getVersion()
    {
        return "none";
    }

    @Override
    public String getFullVersion()
    {
        return "none";
    }

    @Override
    public File getRoot()
    {
        try
        {
            String path = Gunsmith.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if (path == null)
            {
                path = "";
            }
            File directory = new File(path);
            if (!directory.isDirectory())
            {
                directory = directory.getParentFile();
            }
            return directory;
        } catch (URISyntaxException ignored)
        {
            throw new RuntimeException("Could not create trivial root data source");
        }
    }

    @Override
    public File getMetricsFile()
    {
        return null;
    }

    @Override
    public int getNumberOfPlayersOnline()
    {
        return 0;
    }

}
