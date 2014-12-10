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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.voxelplugineering.voxelsniper.api.ILogger;

/**
 * A wrapper for a {@link java.util.logging.Logger} to receive logging messages from gunsmith.
 */
public class JavaUtilLogger implements ILogger
{
    private Logger logger;

    /**
     * Creates a new {@link JavaUtilLogger}.
     * 
     * @param logger the logger to wrap
     */
    public JavaUtilLogger(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String msg)
    {
        logger.log(Level.FINE, msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String msg)
    {
        logger.log(Level.INFO, msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String msg)
    {
        logger.log(Level.WARNING, msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String msg)
    {
        logger.log(Level.SEVERE, msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Exception e)
    {
        logger.log(Level.SEVERE, e.getMessage(), e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Exception e, String msg)
    {
        logger.log(Level.SEVERE, msg, e);

    }
}
