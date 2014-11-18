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
package com.voxelplugineering.voxelsniper.logging;

import java.util.HashMap;
import java.util.Map;

import com.voxelplugineering.voxelsniper.api.ILogger;
import com.voxelplugineering.voxelsniper.api.ILoggingDistributor;

/**
 * A standard logging distributor.
 */
public class CommonLoggingDistributor implements ILoggingDistributor
{

    /**
     * The collection of loggers to distribute the logging messages to.
     */
    private Map<String, ILogger> loggers = null;

    /**
     * Creates a new logging distributor.
     */
    public CommonLoggingDistributor()
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init()
    {
        this.loggers = new HashMap<String, ILogger>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop()
    {
        this.loggers = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restart()
    {
        stop();
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String msg)
    {
        for (String n : this.loggers.keySet())
        {
            ILogger l = this.loggers.get(n);
            l.debug(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String msg)
    {
        for (String n : this.loggers.keySet())
        {
            ILogger l = this.loggers.get(n);
            l.info(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warning(String msg)
    {
        for (String n : this.loggers.keySet())
        {
            ILogger l = this.loggers.get(n);
            l.warning(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String msg)
    {
        for (String n : this.loggers.keySet())
        {
            ILogger l = this.loggers.get(n);
            l.error(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Exception e)
    {
        for (String n : this.loggers.keySet())
        {
            ILogger l = this.loggers.get(n);
            l.error(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Exception e, String msg)
    {
        for (String n : this.loggers.keySet())
        {
            ILogger l = this.loggers.get(n);
            l.error(e, msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerLogger(ILogger logger, String name)
    {
        this.loggers.put(name, logger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLogger(String name)
    {
        this.loggers.remove(name);
    }

}
