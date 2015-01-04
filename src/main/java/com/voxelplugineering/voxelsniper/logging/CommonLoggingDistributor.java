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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.logging.Logger;
import com.voxelplugineering.voxelsniper.api.logging.LoggingDistributor;

/**
 * A standard logging distributor.
 */
public class CommonLoggingDistributor implements LoggingDistributor
{

    /**
     * The collection of loggers to distribute the logging messages to.
     */
    private Map<String, Logger> loggers = null;

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
        this.loggers = Maps.newHashMap();
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
        if (msg == null || msg.isEmpty())
        {
            return;
        }
        for (String n : this.loggers.keySet())
        {
            Logger l = this.loggers.get(n);
            l.debug(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String msg)
    {
        if (msg == null || msg.isEmpty())
        {
            return;
        }
        if (this.loggers.isEmpty())
        {
            System.out.println(msg);
        }
        for (String n : this.loggers.keySet())
        {
            Logger l = this.loggers.get(n);
            l.info(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String msg)
    {
        if (msg == null || msg.isEmpty())
        {
            return;
        }
        if (this.loggers.isEmpty())
        {
            System.out.println("[WARNING] " + msg);
        }
        for (String n : this.loggers.keySet())
        {
            Logger l = this.loggers.get(n);
            l.warn(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String msg)
    {
        if (msg == null || msg.isEmpty())
        {
            return;
        }
        if (this.loggers.isEmpty())
        {
            System.out.println("[ERROR] " + msg);
        }
        for (String n : this.loggers.keySet())
        {
            Logger l = this.loggers.get(n);
            l.error(msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Exception e)
    {
        if (e == null)
        {
            return;
        }
        if (this.loggers.isEmpty())
        {
            e.printStackTrace();
        }
        for (String n : this.loggers.keySet())
        {
            Logger l = this.loggers.get(n);
            l.error(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Exception e, String msg)
    {
        if (msg == null || msg.isEmpty() || e == null)
        {
            return;
        }
        if (this.loggers.isEmpty())
        {
            System.out.println("[ERROR] " + msg);
            e.printStackTrace();
        }
        for (String n : this.loggers.keySet())
        {
            Logger l = this.loggers.get(n);
            l.error(e, msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerLogger(Logger logger, String name)
    {
        checkNotNull(logger, "Logger cannot be null!");
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        this.loggers.put(name, logger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLogger(String name)
    {
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        this.loggers.remove(name);
    }

}
