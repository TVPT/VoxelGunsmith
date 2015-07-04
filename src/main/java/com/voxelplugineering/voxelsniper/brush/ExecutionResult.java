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
package com.voxelplugineering.voxelsniper.brush;

/**
 * Represents the result of a brushes execution and whether the brush chain's execution should
 * continue.
 */
public class ExecutionResult
{

    private static final ExecutionResult ABORT = new ExecutionResult(false);
    private static final ExecutionResult CONTINUE = new ExecutionResult(true);

    /**
     * Gets an {@link ExecutionResult} which will abort brush chain execution.
     * 
     * @return The execution result
     */
    public static ExecutionResult abortExecution()
    {
        return ABORT;
    }

    /**
     * Gets an {@link ExecutionResult} which will continue brush chain execution.
     * 
     * @return The execution result
     */
    public static ExecutionResult continueExecution()
    {
        return CONTINUE;
    }

    private final boolean cont;

    private ExecutionResult(boolean c)
    {
        this.cont = c;
    }

    /**
     * Gets whether the execution should continue.
     * 
     * @return Should continue
     */
    public boolean shouldContinue()
    {
        return this.cont;
    }

}
