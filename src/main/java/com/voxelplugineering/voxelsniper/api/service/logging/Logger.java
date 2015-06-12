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
package com.voxelplugineering.voxelsniper.api.service.logging;

/**
 * Interface for a log message output.
 */
public interface Logger
{

    /**
     * Gets the current logging level of this logger. Messages at or above this level will be
     * logged.
     * 
     * @return The logging level
     */
    LogLevel getLevel();

    /**
     * Sets the {@link LogLevel} of this logger. Messages at or above this level will be logged.
     * 
     * @param level The new logging level
     */
    void setLevel(LogLevel level);

    /**
     * Log a message at the given {@link LogLevel}.
     * 
     * @param level The log level
     * @param msg The message
     */
    void log(LogLevel level, String msg);

    /**
     * Logs a debug message to the output. Will not be displayed unless the output's logging level
     * is set to debug.
     * 
     * @param msg the debug message, cannot be null or empty
     */
    void debug(String msg);

    /**
     * Logs an info message to the output.
     * 
     * @param msg the info message, cannot be null or empty
     */
    void info(String msg);

    /**
     * Logs a warning to the output.
     * 
     * @param msg the warning, cannot be null or empty
     */
    void warn(String msg);

    /**
     * Logs an error to the output.
     * 
     * @param msg the error message, cannot be null or empty
     */
    void error(String msg);

    /**
     * Logs an error to the output.
     * 
     * @param e the exception, cannot be null
     */
    void error(Exception e);

    /**
     * Logs an error to the output.
     * 
     * @param e the exception, cannot be null
     * @param msg an included message, cannot be null or empty
     */
    void error(Exception e, String msg);

}
