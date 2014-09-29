package com.voxelplugineering.voxelsniper.common.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.voxelplugineering.voxelsniper.api.ICommandRegistrar;
import com.voxelplugineering.voxelsniper.api.ISniper;

public class CommandHandler
{
    
    public static CommandHandler COMMAND_HANDLER = null;
    
    private CommandHandler()
    {
        
    }
    
    public static void create()
    {
        if(COMMAND_HANDLER != null) return;
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
        this.registrar.registerCommand(cmd);
        for(String alias: cmd.getAllAliases())
        {
            commands.put(alias, cmd);
        }
    }
    
    public void onCommand(ISniper player, String command, String[] args)
    {
        if(!commands.containsKey(command)) return;
        Command handler = commands.get(command);
        boolean success = handler.execute(player, args);
    }

    public void onCommand(ISniper player, String fullCommand)
    {
        String[] s = fullCommand.split(" ");
        onCommand(player, s[0], Arrays.copyOfRange(s, 1, s.length));
    }
    
}