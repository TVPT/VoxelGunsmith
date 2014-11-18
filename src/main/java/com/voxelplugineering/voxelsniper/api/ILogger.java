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
package com.voxelplugineering.voxelsniper.api;

/**
 * Interface for a log message output.
 */
public interface ILogger
{

    /**
     * Logs a debug message to the output. Will not be displayed unless the output's logging level is set to debug.
     * 
     * @param msg the debug message
     */
    void debug(String msg);

    /**
     * Logs an info message to the output.
     * 
     * @param msg the info message
     */
    void info(String msg);

    /**
     * Logs a warning to the output.
     * 
     * @param msg the warning
     */
    void warning(String msg);

    /**
     * Logs an error to the output.
     * 
     * @param msg the error message
     */
    void error(String msg);

    /**
     * Logs an error to the output.
     * 
     * @param e the exception
     */
    void error(Exception e);

    /**
     * Logs an error to the output.
     * 
     * @param e the exception
     * @param msg an included message
     */
    void error(Exception e, String msg);

}
