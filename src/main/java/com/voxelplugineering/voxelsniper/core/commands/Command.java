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
package com.voxelplugineering.voxelsniper.core.commands;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.api.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.api.service.config.Configuration;
import com.voxelplugineering.voxelsniper.api.service.permission.PermissionProxy;
import com.voxelplugineering.voxelsniper.core.util.Context;

/**
 * A abstract representation of a command.
 */
public abstract class Command
{

    protected final PermissionProxy permsProxy;
    protected final Configuration config;

    /**
     * Aliases for this command.
     */
    protected String[] aliases = new String[0];
    /**
     * The name of this command, the primary command alias.
     */
    private final String name;
    /**
     * A help message for this command.
     */
    private String helpMsg;
    /**
     * If this command is player only or usable by the console as well.
     */
    private boolean playerOnly = false;
    /**
     * An array of permissions nodes, a sniper must have at least one of these in order to execute
     * this command.
     */
    private String[] permissions = null;

    /**
     * Constructs a new command with the given name and help.
     * 
     * @param name the name of this command
     * @param help the help string for this command
     */
    protected Command(final String name, final String help, Context context)
    {
        checkNotNull(name, "Cannot have a null name!");
        checkArgument(!name.isEmpty(), "Cannot have an empty name!");
        this.name = name;
        this.helpMsg = help;
        this.permsProxy = context.getRequired(PermissionProxy.class);
        this.config = context.getRequired(Configuration.class);
        this.helpMsg = this.config.get("defaultHelpMessage", String.class).or("No help was provided for this command.");
    }

    /**
     * Extracts and validates the arguments for the command and passes it to the specific executor.
     * 
     * @param sender the command source
     * @param args the raw command arguments
     * @return a success flag
     */
    public abstract boolean execute(CommandSender sender, String[] args);

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
     * Returns the permission nodes for this command. A sniper is required to have at least one of
     * these in order to execute the command.
     * 
     * @return the permissions
     */
    public String[] getPermissions()
    {
        return this.permissions;
    }

}
