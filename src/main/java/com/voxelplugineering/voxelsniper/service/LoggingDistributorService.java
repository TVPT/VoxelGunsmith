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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.logging.LogLevel;
import com.voxelplugineering.voxelsniper.api.logging.Logger;
import com.voxelplugineering.voxelsniper.api.logging.LoggingDistributor;

/**
 * A standard logging distributor.
 */
public class LoggingDistributorService extends AbstractService implements LoggingDistributor
{

    /**
     * The collection of loggers to distribute the logging messages to.
     */
    private Map<String, Logger> loggers = null;
    private LogLevel root = LogLevel.DEBUG;

    /**
     * Creates a new logging distributor.
     */
    public LoggingDistributorService()
    {
        super(-2);
        this.loggers = Maps.newHashMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "logger";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init()
    {
        this.loggers = Maps.newHashMap();
        info("Initializing Logging service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroy()
    {
        info("Stopping Logging service");
        this.loggers = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogLevel getLevel()
    {
        return this.root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLevel(LogLevel level)
    {
        this.root = level;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(LogLevel level, String msg)
    {

        check();
        if (msg == null || msg.isEmpty() || !level.isGEqual(this.root))
        {
            return;
        }
        if (this.loggers.isEmpty())
        {
            System.out.println("[" + level.name() + "] " + msg);
        }
        for (String n : this.loggers.keySet())
        {
            Logger l = this.loggers.get(n);
            l.log(level, msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Logger> getLoggers()
    {
        return this.loggers.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String msg)
    {
        check();
        if (msg == null || msg.isEmpty() || !LogLevel.DEBUG.isGEqual(this.root))
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
        check();
        if (msg == null || msg.isEmpty() || !LogLevel.INFO.isGEqual(this.root))
        {
            return;
        }
        if (this.loggers.isEmpty())
        {
            System.out.println("[INFO] " + msg);
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
        check();
        if (msg == null || msg.isEmpty() || !LogLevel.WARN.isGEqual(this.root))
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
        check();
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
        check();
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
        check();
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
        check();
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
        check();
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        this.loggers.remove(name);
    }

}
