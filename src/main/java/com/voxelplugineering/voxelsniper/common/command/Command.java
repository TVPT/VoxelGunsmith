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

/**
 * A abstract representation of a command.
 */
public abstract class Command
{

    /**
     * Aliases for this command.
     */
    String[] aliases;
    /**
     * A map of arguments for this command.
     */
    private Map<String, CommandArgument<?>> arguments;
    /**
     * The name of this command, the primary command alias.
     */
    private final String name;
    /**
     * A help message for this command.
     */
    private String helpMsg = "No help is provided for this command.";
    /**
     * If this command is player only or usable by the console as well.
     */
    private boolean playerOnly = false;
    /**
     * An array of permissions nodes, a sniper must have at least one of these in order to execute this command.
     */
    private String[] permissions = null;

    /**
     * Constructs a new command with the given name
     *
     * @param name the name of this command
     */
    protected Command(final String name)
    {
        this(name, "No help is provided for this command.");
    }

    /**
     * Constructs a new command with the given name and help.
     *
     * @param name the name of this command
     * @param help the help string for this command
     */
    protected Command(final String name, final String help)
    {
        checkNotNull(name, "Cannot have a null name!");
        checkArgument(!name.isEmpty(), "Cannot have an empty name!");
        this.name = name;
        this.helpMsg = help;
    }

    /**
     * Adds a argument for this command.
     * 
     * @param argument the new argument
     */
    protected void addArgument(CommandArgument<?> argument)
    {
        checkNotNull(argument, "Cannot add a null argument!");
        if (this.arguments == null)
        {
            this.arguments = new HashMap<String, CommandArgument<?>>();
        }
        this.arguments.put(argument.getName(), argument);
    }

    /**
     * Executes this command with the given sniper as the source for the command.
     * 
     * @param sniper the command source
     * @param args the arguments for the command
     * @return a success flag
     */
    public abstract boolean execute(ISniper sniper, Map<String, CommandArgument<?>> args);

    /**
     * Extracts and validates the arguments for the command and passes it to the specific executor.
     * 
     * @param sniper the command source
     * @param args the raw command arguments
     * @return a success flag
     */
    public boolean execute(ISniper sniper, String[] args)
    {
        return execute(sniper, extractArguements(sniper, args));
    }

    /**
     * Returns the aliases for this command (not including the primary alias).
     * 
     * @return the aliases
     */
    public String[] getAllAliases()
    {
        return this.aliases;
    }

    /**
     * Sets the aliases for this command.
     * 
     * @param aliases the new aliases
     */
    protected void setAliases(String... aliases)
    {
        this.aliases = aliases;
    }

    /**
     * Extracts and validates the command arguments from the given raw arguments. The returned {@link Map} is unmodifiable.
     * 
     * @param sniper the command source
     * @param args the raw arguments
     * @return the extracted arguments
     */
    private Map<String, CommandArgument<?>> extractArguements(ISniper sniper, String[] args)
    {
        checkNotNull(sniper, "Sniper cannot be null");
        checkNotNull(args, "CommanD arguments cannot be null");
        int i = 0;
        for (String c : this.arguments.keySet())
        {
            CommandArgument<?> ca = this.arguments.get(c);
            ca.parse(sniper, args, i++);
        }
        return Collections.unmodifiableMap(this.arguments);
    }

    /**
     * Returns the help message.
     * 
     * @return the help message
     */
    public String getHelpMsg()
    {
        return this.helpMsg;
    }

    /**
     * Returns the name or primary alias of the command.
     * 
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns whether the command is player only.
     * 
     * @return is player only
     */
    public boolean isPlayerOnly()
    {
        return this.playerOnly;
    }

    /**
     * Sets whether the command is player only.
     * 
     * @param playerOnly is player only
     */
    protected void setPlayerOnly(boolean playerOnly)
    {
        this.playerOnly = playerOnly;
    }

    /**
     * Sets the valid permissions for this command.
     * 
     * @param perms the permissions, cannot be null
     */
    public void setPermissions(String... perms)
    {
        checkNotNull(perms, "Permissions cannot be null");
        this.permissions = perms;
    }

    /**
     * Returns the permission nodes for this command. A sniper is required to have at least one of these in order to execute the command.
     * 
     * @return the permissions
     */
    public String[] getPermissions()
    {
        return this.permissions;
    }

}
