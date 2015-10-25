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
package com.voxelplugineering.voxelsniper.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.commands.Command;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.command.CommandHandler;
import com.voxelplugineering.voxelsniper.service.command.CommandRegistrar;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.service.permission.PermissionProxy;
import com.voxelplugineering.voxelsniper.util.Context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A handler for commands which handles registration of command handlers and commands. Also
 * delegates commands to the handlers and registers commands with the underlying registrar.
 * 
 * <p> TODO: correctly identify and handle commands with overlapping aliases. </p>
 */
public class CommandHandlerService extends AbstractService implements CommandHandler
{

    private final PermissionProxy perms;

    private Map<String, Command> commands;
    private List<Command> unique;
    private CommandRegistrar registrar;

    /**
     * Constructs a command handler.
     * 
     * @param context The context
     */
    public CommandHandlerService(Context context)
    {
        super(context);
        this.perms = context.getRequired(PermissionProxy.class, this);
    }

    @Override
    protected void _init()
    {
        this.commands = Maps.newHashMap();
        this.unique = Lists.newArrayList();

    }

    @Override
    protected void _shutdown()
    {
        this.commands = null;
        this.unique = null;
    }

    @Override
    public synchronized void setRegistrar(CommandRegistrar registrar)
    {
        check("setRegistrar");
        this.registrar = checkNotNull(registrar, "Registrar cannot be null");
        // Register all existing commands to the registrar
        this.unique.forEach(this.registrar::registerCommand);
    }

    @Override
    public synchronized void registerCommand(Command cmd)
    {
        check("registerCommand");
        checkNotNull(cmd, "Cannot register a null command!");
        if (this.registrar != null)
        {
            this.registrar.registerCommand(cmd);
        }
        this.unique.add(cmd);
        this.commands.put(cmd.getName(), cmd);
        for (String alias : cmd.getAllAliases())
        {
            this.commands.put(alias, cmd);
        }
    }

    @Override
    public synchronized void onCommand(CommandSender sender, String command, String[] args)
    {
        check("onCommand");
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
                if (this.perms.hasPermission((Player) sender, s))
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
            sender.sendMessage(VoxelSniperConfiguration.permissionsRequiredMessage);
        }
    }

    @Override
    public void onCommand(CommandSender sender, String fullCommand)
    {
        check("onCommand");
        checkNotNull(sender, "Cannot have a null sniper!");
        checkNotNull(fullCommand, "Cannot use a null command!");
        checkArgument(!fullCommand.isEmpty(), "Command cannot be empty");
        String[] s = fullCommand.split(" ");
        onCommand(sender, s[0], Arrays.copyOfRange(s, 1, s.length));
    }

    @Override
    public Optional<CommandRegistrar> getRegistrar()
    {
        return Optional.ofNullable(this.registrar);
    }

}
