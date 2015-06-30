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
package com.voxelplugineering.voxelsniper.service.logging;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A wrapper for a {@link java.util.logging.Logger} to receive logging messages from gunsmith.
 */
public class JavaUtilLogger implements com.voxelplugineering.voxelsniper.service.logging.Logger
{

    private static LogLevel toLevel(Level level)
    {
        if (level == Level.CONFIG || level == Level.FINE || level == Level.FINER || level == Level.FINEST)
        {
            return LogLevel.DEBUG;
        }
        if (level == Level.OFF)
        {
            return LogLevel.OFF;
        }
        if (level == Level.WARNING)
        {
            return LogLevel.WARN;
        }
        if (level == Level.SEVERE || level == Level.ALL)
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
            return Level.FINE;
        case INFO:
            return Level.INFO;
        case WARN:
            return Level.WARNING;
        case ERROR:
            return Level.SEVERE;
        default:
            return Level.INFO;
        }
    }

    private final Logger logger;

    /**
     * Creates a new {@link JavaUtilLogger}.
     * 
     * @param logger the logger to wrap
     */
    public JavaUtilLogger(Logger logger)
    {
        this.logger = checkNotNull(logger);
    }

    @Override
    public LogLevel getLevel()
    {
        return toLevel(this.logger.getLevel());
    }

    @Override
    public void setLevel(LogLevel level)
    {
        checkNotNull(level);
        this.logger.setLevel(fromLevel(level));
    }

    @Override
    public void log(LogLevel level, String msg)
    {
        this.logger.log(fromLevel(level), msg);
    }

    @Override
    public void debug(String msg)
    {
        this.logger.log(Level.FINE, msg);
    }

    @Override
    public void info(String msg)
    {
        this.logger.log(Level.INFO, msg);
    }

    @Override
    public void warn(String msg)
    {
        this.logger.log(Level.WARNING, msg);
    }

    @Override
    public void error(String msg)
    {
        this.logger.log(Level.SEVERE, msg);
    }

    @Override
    public void error(Exception e)
    {
        this.logger.log(Level.SEVERE, e.getMessage(), e);
    }

    @Override
    public void error(Exception e, String msg)
    {
        this.logger.log(Level.SEVERE, msg, e);

    }
}
