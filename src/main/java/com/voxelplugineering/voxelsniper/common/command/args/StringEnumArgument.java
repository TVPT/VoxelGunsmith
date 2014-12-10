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

import com.google.common.collect.Iterables;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.command.CommandArgument;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.voxelplugineering.voxelsniper.util.Utilities.findMatches;

/**
 * A command argument that is validated by a set of provided Strings.
 **/
public class StringEnumArgument extends CommandArgument<String>
{

    private final String def;
    private final String[] choices;
    private final String usage;

    private String choice = null;

    /**
     * Constructs a normalized argument that parses for a set of string based choices.
     *
     * @param name the name of this argument
     * @param required whether this argument is required for validation
     * @param def the default value
     * @param choices the choices of which can be chosen from
     */
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

    /**
     * Constructs a normalized argument that parses for a set of string based choices.
     * 
     * @param name the name of this argument
     * @param required whether this argument is required for validation
     * @param def the default value
     * @param choices the choices of which can be chosen from
     */
    public StringEnumArgument(String name, boolean required, String def, Iterable<String> choices)
    {
        super(name, required);
        if (!required)
        {
            checkNotNull(def, "Cannot have a null default if the argument is not required.");
        }
        checkNotNull(choices, "Cannot have null choices");
        this.def = def;
        this.choices = Iterables.toArray(choices, String.class);
        StringBuilder sb = new StringBuilder().append(!required ? '[' : '<');
        for (String s : choices)
        {
            sb.append(s);
            sb.append('|');
        }
        if (this.choices.length != 0)
        {
            sb.setLength(sb.length() - 1);
        }
        sb.append(!required ? ']' : '>');
        this.usage = sb.toString();
    }

    /**
     * Constructs a normalized argument that parses for a set of string based choices.
     * 
     * @param name the name of this argument
     * @param required whether this argument is required for validation
     * @param def the default value
     * @param enumClass An Enum type to use as valid choices
     */
    public StringEnumArgument(String name, boolean required, String def, Class<?> enumClass)
    {
        super(name, required);
        if (!required)
        {
            checkNotNull(def, "Cannot have a null default if the argument is not required.");
        }
        checkNotNull(enumClass, "Cannot have null enumClass");
        checkArgument(enumClass.isEnum(), "Enum class must be an enum type.");
        this.def = def;
        Object[] values = enumClass.getEnumConstants();
        this.choices = new String[values.length];
        int i = 0;
        for (Object o : values)
        {
            choices[i++] = o.toString();
        }
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

    /**
     * {@inheritDoc}
     */
    public String getChoice()
    {
        return this.choice;
    }

    /**
     * {@inheritDoc}
     */
    public boolean setChoice(String s)
    {
        if (Arrays.asList(this.choices).contains(s))
        {
            this.choice = s;
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsageString(boolean optional)
    {
        return this.usage;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void skippedOptional(ISniper user)
    {
        this.choice = this.def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clean()
    {
        this.choice = this.def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> tabComplete(ISniper caster, String[] allArgs, int startPosition)
    {
        return findMatches(allArgs[startPosition], Arrays.asList(this.choices));
    }
}
