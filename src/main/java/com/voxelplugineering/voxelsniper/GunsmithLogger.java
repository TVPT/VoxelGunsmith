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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.service.logging.LogLevel;
import com.voxelplugineering.voxelsniper.service.logging.Logger;

/**
 * A standard logging distributor.
 */
public class GunsmithLogger
{
    private static GunsmithLogger INSTANCE = null;
    
    public static GunsmithLogger getLogger() {
        if(INSTANCE == null) {
            INSTANCE = new GunsmithLogger();
        }
        return INSTANCE;
    }
    
    public static void setLogger(GunsmithLogger logger) {
        INSTANCE = logger;
    }

    /**
     * The collection of loggers to distribute the logging messages to.
     */
    private final Map<String, Logger> loggers;
    private LogLevel root = LogLevel.DEBUG;

    /**
     * Creates a new logging distributor.
     */
    private GunsmithLogger()
    {
        this.loggers = Maps.newHashMap();
    }

    public LogLevel getLevel()
    {
        return this.root;
    }

    public void setLevel(LogLevel level)
    {
        this.root = level;
    }

    public void log(LogLevel level, String msg)
    {
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

    public Collection<Logger> getLoggers()
    {
        return this.loggers.values();
    }

    public void debug(String msg)
    {
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

    public void info(String msg)
    {
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

    public void warn(String msg)
    {
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

    public void error(Throwable e, String msg)
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

    public void registerLogger(Logger logger, String name)
    {
        checkNotNull(logger, "Logger cannot be null!");
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        this.loggers.put(name, logger);
    }

    public void removeLogger(String name)
    {
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        this.loggers.remove(name);
    }

}
