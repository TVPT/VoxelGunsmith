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
package com.voxelplugineering.voxelsniper.commands;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.service.permission.PermissionProxy;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.Nameable;

/**
 * A abstract representation of a command.
 */
public abstract class Command implements Nameable
{

    private final PermissionProxy permsProxy;
    private String[] aliases = new String[0];
    private final String name;
    private String helpMsg;
    private boolean playerOnly;
    private String[] permissions;

    /**
     * Constructs a new command with the given name and help.
     * 
     * @param name The name of this command
     * @param help The help string for this command
     */
    protected Command(final String name, final String help, Context context)
    {
        checkNotNull(name, "Cannot have a null name!");
        checkArgument(!name.isEmpty(), "Cannot have an empty name!");
        this.name = name;
        if (help == null || help.isEmpty())
        {
            this.helpMsg = VoxelSniperConfiguration.defaultHelpMessage;
        } else
        {
            this.helpMsg = help;
        }
        this.permsProxy = context.getRequired(PermissionProxy.class);
        this.permissions = new String[0];
    }

    /**
     * Gets the permission proxy service.
     * 
     * @return The permission proxy
     */
    protected PermissionProxy getPerms()
    {
        return this.permsProxy;
    }

    /**
     * Extracts and validates the arguments for the command and passes it to the specific executor.
     * 
     * @param sender The command source
     * @param args The raw command arguments
     * @return A success flag
     */
    public abstract boolean execute(CommandSender sender, String[] args);

    /**
     * Returns the aliases for this command (not including the primary alias).
     * 
     * @return The aliases
     */
    public String[] getAllAliases()
    {
        return this.aliases;
    }

    /**
     * Sets the aliases for this command.
     * 
     * @param aliases The new aliases
     */
    protected void setAliases(String... aliases)
    {
        this.aliases = aliases;
    }

    /**
     * Returns the help message.
     * 
     * @return The help message
     */
    public String getHelpMsg()
    {
        return this.helpMsg;
    }

    /**
     * Returns the name or primary alias of the command.
     * 
     * @return The name
     */
    @Override
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns whether the command is player only.
     * 
     * @return Is player only
     */
    public boolean isPlayerOnly()
    {
        return this.playerOnly;
    }

    /**
     * Sets whether the command is player only.
     * 
     * @param playerOnly Is player only
     */
    protected void setPlayerOnly(boolean playerOnly)
    {
        this.playerOnly = playerOnly;
    }

    /**
     * Sets the valid permissions for this command.
     * 
     * @param perms The permissions, cannot be null
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
     * @return The permissions
     */
    public String[] getPermissions()
    {
        return this.permissions;
    }

}
