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

import java.io.PrintStream;
import java.util.logging.Level;

/**
 * A logger wrapping a {@link PrintStream}. Designed for use for logging to consoles such as standard out.
 */
public class PrintStreamLogger implements com.voxelplugineering.voxelsniper.api.logging.Logger
{

    private final PrintStream stream;
    private final Level level;

    /**
     * Creates a new {@link PrintStreamLogger}.
     * 
     * @param stream The print stream to wrap
     * @param level The logging level
     */
    public PrintStreamLogger(PrintStream stream, Level level)
    {
        this.stream = stream;
        this.level = level;
    }

    /**
     * Creates a new {@link PrintStreamLogger}.
     * 
     * @param stream The print stream to wrap
     */
    public PrintStreamLogger(PrintStream stream)
    {
        this(stream, Level.INFO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String msg)
    {
        if(this.level.intValue() <= Level.FINE.intValue())
        {
            printWithPrefix("[DEBUG]", msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String msg)
    {
        if(this.level.intValue() <= Level.INFO.intValue())
        {
            printWithPrefix("[INFO]", msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String msg)
    {
        if(this.level.intValue() <= Level.WARNING.intValue())
        {
            printWithPrefix("[WARNING]", msg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String msg)
    {
        printWithPrefix("[ERROR]", msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Exception e)
    {
        e.printStackTrace();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(Exception e, String msg)
    {
        printWithPrefix("[ERROR]", msg);
        e.printStackTrace();
    }

    private void printWithPrefix(String prefix, String msg)
    {
        String[] lines = msg.split("\n");
        for (String l : lines)
        {
            this.stream.println(prefix + " " + l);
        }
    }

}
