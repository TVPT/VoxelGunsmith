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

import java.util.Arrays;
import java.util.Map;

import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.ICommandRegistrar;
import com.voxelplugineering.voxelsniper.api.ISniper;

/**
 * A handler for commands which handles registration of command handlers and commands. Also delegates commands to the handlers and registers commands
 * with the underlying registrar.
 * <p>
 * TODO: correctly identify and handle commands with overlapping aliases.
 */
public class CommandHandler
{
    /**
     * The message sent to players when they lack the required permission for a command.
     */
    private String permissionMessage = "You lack the required permission for this command.";

    /**
     * A Map of commands with their primary alias as the key.
     */
    private final Map<String, Command> commands;
    /**
     * The registrar of the underlying system for registering commands.
     */
    private ICommandRegistrar registrar;

    /**
     * Constructs a command handler
     */
    public CommandHandler()
    {
        if (Gunsmith.getConfiguration().has("permissionsRequiredMessage"))
        {
            this.permissionMessage = Gunsmith.getConfiguration().get("permissionsRequiredMessage").get().toString();
        }
        this.commands = Maps.newHashMap();
    }

    /**
     * Sets the registrar for this handler.
     * 
     * @param registrar the new registrar, cannot be null
     */
    public void setRegistrar(ICommandRegistrar registrar)
    {
        checkNotNull(registrar, "Registrar cannot be null");
        this.registrar = registrar;
    }

    /**
     * Registers a new command with this system. Will overwrite any aliases already registered.
     * 
     * @param cmd the new command
     */
    public void registerCommand(Command cmd)
    {
        checkNotNull(cmd, "Cannot register a null command!");
        checkNotNull(this.registrar, "Cannot register a command without setting the registrar first.");
        this.registrar.registerCommand(cmd);
        for (String alias : cmd.getAllAliases())
        {
            this.commands.put(alias, cmd);
        }
    }

    /**
     * Attempts to execute the given command.
     * 
     * @param player the command sender
     * @param command the command
     * @param args the command's arguments
     */
    public void onCommand(ISniper player, String command, String[] args)
    {
        checkNotNull(player, "Cannot have a null sniper!");
        checkNotNull(command, "Cannot use a null command!");
        checkNotNull(args, "Command arguments cannot be null!");
        checkArgument(!command.isEmpty(), "Command cannot be empty");
        if (!this.commands.containsKey(command))
        {
            return;
        }
        Command handler = this.commands.get(command);
        boolean allowed = false;
        for (String s : handler.getPermissions())
        {
            if (Gunsmith.getVoxelSniper().getPermissionProxy().hasPermission(player, s))
            {
                allowed = true;
                break;
            }
        }
        if (allowed)
        {
            boolean success = handler.execute(player, args);

            if (!success)
            {
                player.sendMessage(handler.getHelpMsg());
            }
        } else
        {
            player.sendMessage(this.permissionMessage);
        }
    }

    /**
     * Attempts to execute the given command.
     * 
     * @param player the command sender
     * @param fullCommand the full command
     */
    public void onCommand(ISniper player, String fullCommand)
    {
        checkNotNull(player, "Cannot have a null sniper!");
        checkNotNull(fullCommand, "Cannot use a null command!");
        checkArgument(!fullCommand.isEmpty(), "Command cannot be empty");
        String[] s = fullCommand.split(" ");
        onCommand(player, s[0], Arrays.copyOfRange(s, 1, s.length));
    }

}
