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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.voxelplugineering.voxelsniper.api.ICommandRegistrar;
import com.voxelplugineering.voxelsniper.api.ISniper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A handler for commands which handles registration of command handlers and commands. Also delegates commands to the handlers and registers commands
 * with the underlying registrar.
 * <p>
 * TODO: correctly identify and handle commands with overlapping aliases.
 */
public class CommandHandler
{

    public CommandHandler()
    {

    }

    /**
     * A Map of commands with their primary alias as the key.
     */
    private Map<String, Command> commands = new HashMap<String, Command>();
    /**
     * The registrar of the underlying system for registering commands.
     */
    private ICommandRegistrar registrar;

    /**
     * Sets the registrar for this handler.
     * 
     * @param registrar
     *            the new registrar
     */
    public void setRegistrar(ICommandRegistrar registrar)
    {
        this.registrar = registrar;
    }

    /**
     * Registers a new command with this system. Will overwrite any aliases already registered.
     * 
     * @param cmd
     *            the new command
     */
    public void registerCommand(Command cmd)
    {
        checkNotNull(cmd, "Cannot register a null command!");
        this.registrar.registerCommand(cmd);
        for (String alias : cmd.getAllAliases())
        {
            this.commands.put(alias, cmd);
        }
    }

    /**
     * Attempts to execute the given command.
     * 
     * @param player
     *            the command sender
     * @param command
     *            the command
     * @param args
     *            the command's arguments
     */
    public void onCommand(ISniper player, String command, String[] args)
    {
        checkNotNull(player, "Cannot have a null sniper!");
        checkNotNull(command, "Cannot use a null command!");
        if (!this.commands.containsKey(command))
        {
            return;
        }
        Command handler = this.commands.get(command);
        boolean success = handler.execute(player, args);

        if (!success)
        {
            player.sendMessage(handler.getHelpMsg());
        }
    }

    /**
     * Attempts to execute the given command.
     * 
     * @param player
     *            the command sender
     * @param fullCommand
     *            the full command
     */
    public void onCommand(ISniper player, String fullCommand)
    {
        checkNotNull(player, "Cannot have a null sniper!");
        checkNotNull(fullCommand, "Cannot use a null command!");
        String[] s = fullCommand.split(" ");
        onCommand(player, s[0], Arrays.copyOfRange(s, 1, s.length));
    }

}
