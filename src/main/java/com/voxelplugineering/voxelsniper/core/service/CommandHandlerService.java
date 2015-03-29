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
package com.voxelplugineering.voxelsniper.core.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.commands.CommandRegistrar;
import com.voxelplugineering.voxelsniper.api.commands.CommandSender;
import com.voxelplugineering.voxelsniper.api.entity.Player;
import com.voxelplugineering.voxelsniper.core.Gunsmith;
import com.voxelplugineering.voxelsniper.core.commands.Command;

/**
 * A handler for commands which handles registration of command handlers and
 * commands. Also delegates commands to the handlers and registers commands with
 * the underlying registrar.
 * <p>
 * TODO: correctly identify and handle commands with overlapping aliases.
 */
public class CommandHandlerService extends AbstractService
{

    private String permissionMessage;

    private Map<String, Command> commands;
    private CommandRegistrar registrar = null;

    /**
     * Constructs a command handler
     */
    public CommandHandlerService()
    {
        super(10);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return "commandHandler";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init()
    {
        this.permissionMessage =
                Gunsmith.getConfiguration().get("permissionsRequiredMessage", String.class).or("You lack the required permission for this command.");
        this.commands = Maps.newHashMap();
        Gunsmith.getLogger().info("Initialized CommandHandler service");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void destroy()
    {
        this.permissionMessage = null;
        this.commands = null;
        Gunsmith.getLogger().info("Stopped CommandHandler service");
    }

    /**
     * Sets the registrar for this handler.
     * 
     * @param registrar the new registrar, cannot be null
     */
    public synchronized void setRegistrar(CommandRegistrar registrar)
    {
        check();
        checkNotNull(registrar, "Registrar cannot be null");
        this.registrar = registrar;
        // Register all existing commands to the registrar
        for (String name : this.commands.keySet())
        {
            this.registrar.registerCommand(this.commands.get(name));
        }
    }

    /**
     * Registers a new command with this system. Will overwrite any aliases
     * already registered.
     * 
     * @param cmd the new command
     */
    public synchronized void registerCommand(Command cmd)
    {
        check();
        checkNotNull(cmd, "Cannot register a null command!");
        if (this.registrar != null)
        {
            this.registrar.registerCommand(cmd);
        }
        this.commands.put(cmd.getName(), cmd);
        for (String alias : cmd.getAllAliases())
        {
            this.commands.put(alias, cmd);
        }
    }

    /**
     * Attempts to execute the given command.
     * 
     * @param sender the command sender
     * @param command the command
     * @param args the command's arguments
     */
    public synchronized void onCommand(CommandSender sender, String command, String[] args)
    {
        check();
        checkNotNull(sender, "Cannot have a null sniper!");
        checkNotNull(command, "Cannot use a null command!");
        checkNotNull(args, "Command arguments cannot be null!");
        checkArgument(!command.isEmpty(), "Command cannot be empty");
        if (!this.commands.containsKey(command))
        {
            return;
        }
        Command handler = this.commands.get(command);
        boolean allowed = false;
        if (sender.isPlayer())
        {
            for (String s : handler.getPermissions())
            {
                if (Gunsmith.getPermissionsProxy().hasPermission((Player) sender, s))
                {
                    allowed = true;
                    break;
                }
            }
        } else
        {
            allowed = true;
        }

        if (allowed)
        {
            boolean success = handler.execute(sender, args);

            if (!success)
            {
                sender.sendMessage(handler.getHelpMsg());
            }
        } else
        {
            sender.sendMessage(this.permissionMessage);
        }
    }

    /**
     * Attempts to execute the given command.
     * 
     * @param player the command sender
     * @param fullCommand the full command
     */
    public void onCommand(Player player, String fullCommand)
    {
        check();
        checkNotNull(player, "Cannot have a null sniper!");
        checkNotNull(fullCommand, "Cannot use a null command!");
        checkArgument(!fullCommand.isEmpty(), "Command cannot be empty");
        String[] s = fullCommand.split(" ");
        onCommand(player, s[0], Arrays.copyOfRange(s, 1, s.length));
    }

    /**
     * Gets the registrar for this command handler.
     * 
     * @return The registrar, if available
     */
    public Optional<CommandRegistrar> getRegistrar()
    {
        return Optional.fromNullable(this.registrar);
    }

}
