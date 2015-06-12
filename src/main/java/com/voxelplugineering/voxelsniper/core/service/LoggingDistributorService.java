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
package com.voxelplugineering.voxelsniper.core.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.service.logging.LogLevel;
import com.voxelplugineering.voxelsniper.api.service.logging.Logger;
import com.voxelplugineering.voxelsniper.api.service.logging.LoggingDistributor;
import com.voxelplugineering.voxelsniper.core.util.Context;

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
    public LoggingDistributorService(Context context)
    {
        super(context);
        this.loggers = Maps.newHashMap();
    }

    @Override
    protected void _init()
    {
        this.loggers = Maps.newHashMap();
        info("Initializing Logging service");
    }

    @Override
    protected void _shutdown()
    {
        info("Stopping Logging service");
        this.loggers = null;
    }

    @Override
    public LogLevel getLevel()
    {
        return this.root;
    }

    @Override
    public void setLevel(LogLevel level)
    {
        this.root = level;
    }

    @Override
    public void log(LogLevel level, String msg)
    {

        check("log");
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

    @Override
    public Collection<Logger> getLoggers()
    {
        return this.loggers.values();
    }

    @Override
    public void debug(String msg)
    {
        check("debug");
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

    @Override
    public void info(String msg)
    {
        check("info");
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

    @Override
    public void warn(String msg)
    {
        check("warn");
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

    @Override
    public void error(String msg)
    {
        check("error");
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

    @Override
    public void error(Exception e)
    {
        check("error");
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

    @Override
    public void error(Exception e, String msg)
    {
        check("error");
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

    @Override
    public void registerLogger(Logger logger, String name)
    {
        check("registerLogger");
        checkNotNull(logger, "Logger cannot be null!");
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        this.loggers.put(name, logger);
    }

    @Override
    public void removeLogger(String name)
    {
        check("removeLogger");
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        this.loggers.remove(name);
    }

}
