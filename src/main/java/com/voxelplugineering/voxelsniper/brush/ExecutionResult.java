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

    /**
     * Delays the remaining execution of this brush until the next execution. Upon the next input
     * the execution will continue from this brush part. This allows brushes to execute across
     * multiple inputs.
     * 
     * @param continueBrush The brush to continue execution from
     * @return The execution brush
     */
    public static ExecutionResult delayExecution(BrushWrapper continueBrush)
    {
        return new Delay(continueBrush);
    }

    private final boolean shouldContinue;

    /**
     * Create a new {@link ExecutionResult}.
     * 
     * @param cont If the execution should continue
     */
    protected ExecutionResult(boolean cont)
    {
        this.shouldContinue = cont;
    }

    /**
     * Gets whether the execution should continue.
     * 
     * @return Should continue
     */
    public boolean shouldContinue()
    {
        return this.shouldContinue;
    }

    /**
     * An execution result which holds the brush to continue execution from.
     * 
     * @see #delayExecution(BrushWrapper)
     */
    public static class Delay extends ExecutionResult
    {

        private BrushWrapper continuePoint;

        private Delay(BrushWrapper continuePoint)
        {
            super(false);
            this.continuePoint = continuePoint;
        }

        /**
         * Gets the brush to continue execution from.
         * 
         * @return The brush to contine from
         */
        public BrushWrapper getContinuePoint()
        {
            return this.continuePoint;
        }

    }

}
