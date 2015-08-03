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

import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.service.config.ConfigurationContainer;
import com.voxelplugineering.voxelsniper.service.config.ConfigurationService;
import com.voxelplugineering.voxelsniper.service.meta.AnnotationScannerService;
import com.voxelplugineering.voxelsniper.util.ContextTestUtil;
import com.voxelplugineering.voxelsniper.util.DataTranslator;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;

@SuppressWarnings("javadoc")
public class ConfigurationTest
{

    // @Test
    public void testCreate()
    {
        ConfigurationService conf = getConfig();
    }

    // @Test
    public void testSave() throws IOException
    {
        ConfigurationService conf = getConfig();
        File test = new File("config");
        if (!test.exists())
        {
            test.mkdirs();
        }
        conf.save(test, true);
    }

    // @Test
    public void testLoad() throws IOException
    {
        DataTranslator.initialize(ContextTestUtil.create());
        System.out.println(VoxelSniperConfiguration.rayTraceRange);
        ConfigurationService conf = getConfig();
        File test = new File("config");
        if (!test.exists())
        {
            test.mkdirs();
        }
        conf.initialize(test, false);
        System.out.println(VoxelSniperConfiguration.rayTraceRange);

    }

    private static ConfigurationService getConfig()
    {
        ConfigurationService srv = new ConfigurationService(ContextTestUtil.create());
        AnnotationScannerService scanner = new AnnotationScannerService(ContextTestUtil.create());
        scanner.register(ConfigurationContainer.class, srv);
        scanner.scanClassPath((URLClassLoader) Gunsmith.getClassLoader());
        return srv;
    }

}
