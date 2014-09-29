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

import java.util.Map;

import com.voxelplugineering.voxelsniper.api.ISniper;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ObjectArrays.concat;

public abstract class Command
{

    String[] aliases;
    private CommandArguement[] arguements;
    private final String name;

    protected Command(final String name)
    {
        checkNotNull(name, "Cannot have a null name!");
        checkArgument(!name.isEmpty(), "Cannot have an empty name!");
        this.name = name;
    }

    protected void addArgument(CommandArguement arguement)
    {
        checkNotNull(arguement, "Cannot add a null argument!");
        if (this.arguements == null)
        {
            this.arguements = new CommandArguement[]{arguement};
            return;
        }
        concat(this.arguements, arguement);
    }

    public abstract boolean execute(ISniper sniper, Map<String, CommandArguement> args);

    public boolean execute(ISniper sniper, String[] args)
    {
        return execute(sniper, extractArguements(args));
    }

    public String[] getAllAliases()
    {
        return this.aliases;
    }

    protected void setAliases(String... aliases)
    {
        this.aliases = aliases;
    }

    private Map<String, CommandArguement> extractArguements(String[] args)
    {
        return null;
    }

}
