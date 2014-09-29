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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.command.CommandArguement;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.voxelplugineering.voxelsniper.util.Utilities.findMatches;

public class JavaEnumArgument<T extends Enum<T>> extends CommandArguement
{
    private final T defaultValue;
    private final Class<T> clazz;
    private final List<String> names;
    private T choice = null;

    public JavaEnumArgument(boolean required, Class<T> clazz, T defaultValue) throws Exception
    {
        super(required);
        checkNotNull(clazz, "Cannot add a null Java Enum Class argument!");
        this.defaultValue = defaultValue;

        this.clazz = clazz;
        Method valuesMethod = clazz.getDeclaredMethod("values", (Class[]) null);

        @SuppressWarnings("unchecked")
        T[] allValues = (T[]) valuesMethod.invoke(null);

        this.names = new ArrayList<String>(allValues.length);
        for (T t : allValues)
        {
            String temp = t.toString();
            this.names.add(temp);
            if (!temp.equalsIgnoreCase(t.name()))
            {
                this.names.add(t.name());
            }
        }
    }

    public T getChoice()
    {
        return this.choice;
    }

    public void setChoice(T c)
    {
        this.choice = c;
    }

    @Override
    public String getUsageString(boolean optional)
    {
        if (optional)
        {
            return "[" + this.clazz.getSimpleName().toLowerCase() + "]";
        } else
        {
            return "<" + this.clazz.getSimpleName().toLowerCase() + ">";
        }
    }

    @Override
    public int matches(ISniper user, String[] allArgs, int startPosition)
    {
        String arg = allArgs[startPosition];
        try
        {
            Enum.valueOf(this.clazz, arg);
            return 1;
        } catch (IllegalArgumentException e)
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
            this.choice = Enum.valueOf(this.clazz, arg);
        } catch (IllegalArgumentException e)
        {
            this.choice = this.defaultValue;
        }
    }

    @Override
    public void skippedOptional(ISniper user)
    {
        this.choice = this.defaultValue;
    }

    @Override
    public void clean()
    {
        this.choice = this.defaultValue;
    }

    @Override
    public List<String> tabComplete(ISniper user, String[] allArgs, int startPosition)
    {
        return findMatches(allArgs[startPosition], this.names);
    }
}
