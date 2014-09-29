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

public class CommandHandler
{

    public static CommandHandler COMMAND_HANDLER = null;

    private CommandHandler()
    {

    }

    public static void create()
    {
        if (COMMAND_HANDLER != null) return;
        COMMAND_HANDLER = new CommandHandler();
    }

    private Map<String, Command> commands = new HashMap<String, Command>();

    private ICommandRegistrar registrar;

    public void setRegistrar(ICommandRegistrar r)
    {
        this.registrar = r;
    }

    public void registerCommand(Command cmd)
    {
        checkNotNull(cmd, "Cannot register a null command!");
        this.registrar.registerCommand(cmd);
        for (String alias : cmd.getAllAliases())
        {
            this.commands.put(alias, cmd);
        }
    }

    public void onCommand(ISniper player, String command, String[] args)
    {
        checkNotNull(player, "Cannot have a null sniper!");
        checkNotNull(command, "Cannot use a null command!");
        if (!this.commands.containsKey(command)) return;
        Command handler = this.commands.get(command);
        boolean success = handler.execute(player, args);
    }

    public void onCommand(ISniper player, String fullCommand)
    {
        checkNotNull(player, "Cannot have a null sniper!");
        checkNotNull(fullCommand, "Cannot use a null command!");
        String[] s = fullCommand.split(" ");
        onCommand(player, s[0], Arrays.copyOfRange(s, 1, s.length));
    }

}