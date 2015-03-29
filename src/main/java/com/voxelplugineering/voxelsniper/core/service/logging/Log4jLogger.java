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
package com.voxelplugineering.voxelsniper.core.service.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.voxelplugineering.voxelsniper.api.logging.LogLevel;

/**
 * A wrapper for a {@link org.apache.logging.log4j.Logger}.
 */
public class Log4jLogger implements com.voxelplugineering.voxelsniper.api.logging.Logger
{

    private static LogLevel toLevel(Level level)
    {
        if (level == Level.DEBUG || level == Level.TRACE)
        {
            return LogLevel.DEBUG;
        }
        if (level == Level.OFF)
        {
            return LogLevel.OFF;
        }
        if (level == Level.WARN)
        {
            return LogLevel.WARN;
        }
        if (level == Level.ERROR || level == Level.ALL || level == Level.FATAL)
        {
            return LogLevel.ERROR;
        }
        return LogLevel.INFO;
    }

    private static Level fromLevel(LogLevel level)
    {
        switch (level)
        {
        case OFF:
            return Level.OFF;
        case DEBUG:
            return Level.DEBUG;
        case INFO:
            return Level.INFO;
        case WARN:
            return Level.WARN;
        case ERROR:
            return Level.ERROR;
        default:
            return Level.INFO;
        }
    }

    private final Logger logger;

    /**
     * Creates a new {@link Log4jLogger}.
     * 
     * @param logger the logger to wrap
     */
    public Log4jLogger(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogLevel getLevel()
    {
        return toLevel(this.logger.getLevel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLevel(LogLevel level)
    {
        // TODO set log4j logger level ?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(LogLevel level, String msg)
    {
        this.logger.log(fromLevel(level), msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String msg)
    {
        this.logger.debug(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String msg)
    {
        this.logger.info(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String msg)
    {
        this.logger.warn(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String msg)
    {
        this.logger.error(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Exception e)
    {
        this.logger.error("", e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Exception e, String msg)
    {
        this.logger.error(msg, e);
    }
}
