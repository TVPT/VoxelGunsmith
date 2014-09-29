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