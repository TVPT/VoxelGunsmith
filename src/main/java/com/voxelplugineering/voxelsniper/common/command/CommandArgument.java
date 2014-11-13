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
package com.voxelplugineering.voxelsniper.common.command;

import java.util.List;

import com.voxelplugineering.voxelsniper.api.ISniper;

public abstract class CommandArgument
{

    private final boolean required;
    private final String name;
    protected boolean isPresent;

    protected CommandArgument(String name)
    {
        this(name, false);
    }

    protected CommandArgument(String name, boolean required)
    {
        this.required = required;
        this.name = name;
    }

    public boolean isOptional()
    {
        return !this.required;
    }

    public boolean isPresent()
    {
        return this.isPresent;
    }
    
    public String getName()
    {
        return this.name;
    }

    public abstract String getUsageString(boolean optional);

    public abstract int matches(ISniper user, String[] allArgs, int startPosition);

    public abstract void parse(ISniper user, String[] allArgs, int startPosition);

    /**
     * This method is called instead of parse() when an optional CommandArgument
     * is skipped (i.e. not matched).
     *
     * @param user The user executing this method
     */
    public abstract void skippedOptional(ISniper user);

    public abstract void clean();

    public abstract List<String> tabComplete(ISniper user, String[] allArgs, int startPosition);

}
