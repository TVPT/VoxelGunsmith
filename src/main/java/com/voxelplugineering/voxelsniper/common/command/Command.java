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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.voxelplugineering.voxelsniper.api.ISniper;

public abstract class Command
{

    String[] aliases;
    private Map<String, CommandArgument> arguments;
    private final String name;
    private String helpMsg = "No help is provided for this command.";
    private boolean playerOnly = false;

    protected Command(final String name)
    {
        this(name, "No help is provided for this command.");
    }

    protected Command(final String name, final String help)
    {
        checkNotNull(name, "Cannot have a null name!");
        checkArgument(!name.isEmpty(), "Cannot have an empty name!");
        this.name = name;
        this.helpMsg = help;
    }

    protected void addArgument(CommandArgument argument)
    {
        checkNotNull(argument, "Cannot add a null argument!");
        if (this.arguments == null)
        {
            this.arguments = new HashMap<String, CommandArgument>();
        }
        this.arguments.put(argument.getName(), argument);
    }

    public abstract boolean execute(ISniper sniper, Map<String, CommandArgument> args);

    public boolean execute(ISniper sniper, String[] args)
    {
        return execute(sniper, extractArguements(sniper, args));
    }

    public String[] getAllAliases()
    {
        return this.aliases;
    }

    protected void setAliases(String... aliases)
    {
        this.aliases = aliases;
    }

    private Map<String, CommandArgument> extractArguements(ISniper sniper, String[] args)
    {
        int i = 0;
        for (String c : this.arguments.keySet())
        {
            CommandArgument ca = this.arguments.get(c);
            ca.parse(sniper, args, i++);
        }
        return Collections.unmodifiableMap(this.arguments);
    }

    public String getHelpMsg()
    {
        return this.helpMsg;
    }

    public String getName()
    {
        return this.name;
    }
    
    public boolean isPlayerOnly()
    {
        return this.playerOnly;
    }
    
    public void setPlayerOnly(boolean playerOnly)
    {
        this.playerOnly = playerOnly;
    }

}
