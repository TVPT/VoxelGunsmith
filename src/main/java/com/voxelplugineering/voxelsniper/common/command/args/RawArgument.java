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
package com.voxelplugineering.voxelsniper.common.command.args;

import java.util.List;

import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.command.CommandArgument;

public class RawArgument extends CommandArgument
{
    private String[] args;
    
    public RawArgument(final String name)
    {
        super(name);
    }
    
    public String[] getValue()
    {
        return this.args;
    }

    @Override
    public String getUsageString(boolean optional)
    {
        return "";
    }

    @Override
    public int matches(ISniper user, String[] allArgs, int startPosition)
    {
        return 1;
    }

    @Override
    public void parse(ISniper user, String[] allArgs, int startPosition)
    {
        args = allArgs;
    }

    @Override
    public void skippedOptional(ISniper user)
    {
        
    }

    @Override
    public void clean()
    {
        args = null;
    }

    @Override
    public List<String> tabComplete(ISniper user, String[] allArgs, int startPosition)
    {
        return null;
    }

}
