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
package com.voxelplugineering.voxelsniper.util;

import static org.mockito.Mockito.mock;

import com.voxelplugineering.voxelsniper.service.config.Configuration;

/**
 * A util for creating mock {@link Context}s.
 */
public class ContextTestUtil
{

    /**
     * Creates a completely mock context.
     */
    public static Context create()
    {
        return create(null);
    }

    /**
     * Creates a context with the given objects in it, if null is passed for any params then the
     * class is mocked.
     */
    public static Context create(Configuration conf)
    {
        Context c = new Context();
        if (conf != null)
        {
            if (!conf.isInitialized())
            {
                conf.start();
            }
            c.put(conf);
        } else
        {
            c.put(mock(Configuration.class));
        }
        return c;
    }
}
