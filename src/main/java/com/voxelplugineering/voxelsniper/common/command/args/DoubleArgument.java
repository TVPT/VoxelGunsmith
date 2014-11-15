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

/**
 * A CommandArgument that requires a double value.
 */
public class DoubleArgument extends CommandArgument<Double>
{
    private double value;
    private double defaultValue;

    /**
     * Constructs a normalized argument that parses for a double value.
     *
     * @param name the name of this argument
     * @param required whether this argument is required for validation
     */
    public DoubleArgument(String name, boolean required)
    {
        this(name, required, 0);
    }

    /**
     * Constructs a normalized argument that parses for a double value.
     *
     * @param name the name of this argument
     * @param required whether this argument is required for validation
     * @param defaultValue the default value to return for this argument
     */
    public DoubleArgument(String name, boolean required, double defaultValue)
    {
        super(name, required);
        this.defaultValue = defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    public Double getChoice()
    {
        return this.value != -Double.MAX_VALUE ? this.value : this.defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    public boolean setChoice(Double val)
    {
        if (val == null) {
            this.value = 0;
        } else
        {
            this.value = val;
        }
        return true;
    }

    @Override
    public String getUsageString(boolean optional)
    {
        if (optional)
        {
            return '[' + this.getName() + ']';
        } else
        {
            return '<' + this.getName() + '>';
        }
    }

    @Override
    public int matches(ISniper user, String[] allArgs, int startPosition)
    {
        String arg = allArgs[startPosition];
        try
        {
            Double.parseDouble(arg);
            return 1;
        } catch (NumberFormatException e)
        {
            return -1;
        }
    }

    @Override
    public void parse(ISniper user, String[] allArgs, int startPosition)
    {
        String arg = allArgs[startPosition];
        try
        {
            this.value = Double.parseDouble(arg);
        } catch (NumberFormatException e)
        {
            this.value = -Double.MAX_VALUE;
        }
    }

    @Override
    public void skippedOptional(ISniper user)
    {
        this.value = -Double.MAX_VALUE;
    }

    @Override
    public void clean()
    {
        this.value = -Double.MAX_VALUE;
    }

    @Override
    public List<String> tabComplete(ISniper user, String[] allArgs, int startPosition)
    {
        return null;
    }
}
