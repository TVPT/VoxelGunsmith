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

import java.util.Arrays;
import java.util.List;

import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.command.CommandArgument;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.voxelplugineering.voxelsniper.util.Utilities.findMatches;


public class StringEnumArgument extends CommandArgument
{

    private final String def;
    private final String[] choices;
    private final String usage;

    private String choice = null;

    public StringEnumArgument(String name, boolean required, String def, String... choices)
    {
        super(name, required);
        checkNotNull(def, "Cannot have a null default");
        checkNotNull(choices, "Cannot have null choices");
        this.def = def;
        this.choices = choices;

        StringBuilder sb = new StringBuilder().append(!required ? '[' : '<');
        for (String s : choices)
        {
            sb.append(s);
            sb.append('|');
        }
        if (choices.length != 0)
        {
            sb.setLength(sb.length() - 1);
        }
        sb.append(!required ? ']' : '>');
        this.usage = sb.toString();
    }

    public String getChoice()
    {
        return this.choice;
    }

    public boolean setChoice(String s)
    {
        if (Arrays.asList(this.choices).contains(s))
        {
            this.choice = s;
            return true;
        }
        return false;
    }

    @Override
    public String getUsageString(boolean optional)
    {
        return this.usage;
    }

    @Override
    public int matches(ISniper caster, String[] allArgs, int startPosition)
    {
        checkArgument(allArgs != null, "Cannot have null args!");
        checkArgument(startPosition >= 0, "Cannot have a negative argument position");

        String arg = allArgs[startPosition];
        if (Arrays.asList(this.choices).contains(arg))
        {
            return 1;
        }
        return -1;
    }

    @Override
    public void parse(ISniper caster, String[] allArgs, int startPosition)
    {
        String arg = allArgs[startPosition];
        if (Arrays.asList(this.choices).contains(arg))
        {
            this.choice = arg;
        } else
        {
            this.choice = this.def;
        }
    }

    @Override
    public void skippedOptional(ISniper user)
    {
        this.choice = this.def;
    }

    @Override
    public void clean()
    {
        this.choice = this.def;
    }

    @Override
    public List<String> tabComplete(ISniper caster, String[] allArgs, int startPosition)
    {
        return findMatches(allArgs[startPosition], Arrays.asList(this.choices));
    }
}
