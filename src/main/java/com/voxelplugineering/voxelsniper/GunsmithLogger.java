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

import com.voxelplugineering.voxelsniper.service.logging.LogLevel;
import com.voxelplugineering.voxelsniper.service.logging.Logger;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

/**
 * A logging distributor which handles the registration of child loggers and the distribution of all
 * logged messages to these child loggers. If no loggers are registered then the logged messages are
 * passed to standard out instread.
 */
public final class GunsmithLogger implements Logger
{

    private static GunsmithLogger instance;

    /**
     * Gets the global logger for gunsmith.
     *
     * @return The gunsmith logger.
     */
    public static GunsmithLogger getLogger()
    {
        if (instance == null)
        {
            instance = new GunsmithLogger();
        }
        return instance;
    }

    /**
     * Overrides the global logger with the given logger.
     *
     * @param logger The new logger
     */
    public static void setLogger(GunsmithLogger logger)
    {
        instance = logger;
    }

    private final Map<String, Logger> loggers;
    private LogLevel root = LogLevel.DEBUG;

    /**
     * Creates a new logging distributor.
     */
    private GunsmithLogger()
    {
        this.loggers = Maps.newHashMap();
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
        if (msg == null || msg.isEmpty() || !level.isGEqual(this.root))
        {
            return;
        }
        if (this.loggers.isEmpty() && level.isGEqual(this.root))
        {
            System.out.println("[" + level.name() + "] " + msg);
            return;
        }
        for (Logger l : this.loggers.values())
        {
            l.log(level, msg);
        }
    }

    @Override
    public void debug(String msg)
    {
        if (msg == null || msg.isEmpty() || !LogLevel.DEBUG.isGEqual(this.root))
        {
            return;
        }
        for (Logger l : this.loggers.values())
        {
            l.debug(msg);
        }
    }

    @Override
    public void info(String msg)
    {
        if (msg == null || msg.isEmpty() || !LogLevel.INFO.isGEqual(this.root))
        {
            return;
        }
        if (this.loggers.isEmpty())
        {
            System.out.println("[INFO] " + msg);
            return;
        }
        for (Logger l : this.loggers.values())
        {
            l.info(msg);
        }
    }

    @Override
    public void warn(String msg)
    {
        if (msg == null || msg.isEmpty() || !LogLevel.WARN.isGEqual(this.root))
        {
            return;
        }
        if (this.loggers.isEmpty())
        {
            System.out.println("[WARNING] " + msg);
            return;
        }
        for (Logger l : this.loggers.values())
        {
            l.warn(msg);
        }
    }

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
            return;
        }
        for (Logger l : this.loggers.values())
        {
            l.error(msg);
        }
    }

    @Override
    public void error(Throwable e)
    {
        if (e == null)
        {
            return;
        }
        if (this.loggers.isEmpty())
        {
            e.printStackTrace();
            return;
        }
        for (Logger l : this.loggers.values())
        {
            l.error(e);
        }
    }

    @Override
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
            return;
        }
        for (Logger l : this.loggers.values())
        {
            l.error(e, msg);
        }
    }

    /**
     * Registers a new child logger. Logged messages will be distributed to all registered child
     * loggers.
     *
     * @param name The name of the logger
     * @param logger The logger
     */
    public void registerLogger(String name, Logger logger)
    {
        checkNotNull(logger, "Logger cannot be null!");
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        this.loggers.put(name, logger);
    }

    /**
     * Removes the given logger by name.
     *
     * @param name The name of the logger to remove
     */
    public void removeLogger(String name)
    {
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        this.loggers.remove(name);
    }

    /**
     * Gets all registered child loggers.
     *
     * @return The registered loggers
     */
    public Collection<Logger> getLoggers()
    {
        return this.loggers.values();
    }

}
