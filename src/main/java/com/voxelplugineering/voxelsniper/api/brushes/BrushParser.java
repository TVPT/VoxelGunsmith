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
package com.voxelplugineering.voxelsniper.api.brushes;

import com.google.common.base.Optional;

/**
 * A parser for separating the brush names and arguments as well as performing validation.
 */
public interface BrushParser
{

    /**
     * Validates that the given brush string is valid.
     * 
     * @param full The brush string
     * @return Is valid
     */
    boolean validate(String full);

    /**
     * Parses the separate brush parts out of the given brush string.
     * 
     * @param full The brush string
     * @return The brush parts.
     */
    Optional<BrushPart[]> parse(String full);

    /**
     * Represents a single brush part.
     */
    public static class BrushPart
    {
        private final String brushName;
        private final String brushArgument;

        /**
         * Creates a new {@link BrushPart}.
         * 
         * @param name The brush name
         * @param arg The brush argument
         */
        public BrushPart(String name, String arg)
        {
            this.brushName = name;
            if (arg == null)
            {
                this.brushArgument = "";
            } else
            {
                this.brushArgument = arg;
            }
        }

        /**
         * Gets the brush name.
         * 
         * @return The brush name
         */
        public String getBrushName()
        {
            return this.brushName;
        }

        /**
         * Gets the brush argument.
         * 
         * @return The brush argument
         */
        public String getBrushArgument()
        {
            return this.brushArgument;
        }

    }

}
