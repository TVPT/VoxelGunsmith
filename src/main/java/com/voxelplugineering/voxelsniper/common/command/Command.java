package com.voxelplugineering.voxelsniper.common.command;

import java.util.Map;

import com.voxelplugineering.voxelsniper.api.ISniper;

public abstract class Command
{
    
    String[] aliases;

    public abstract boolean execute(ISniper sniper, Map<String, CommandArguement<?>> args);
    
    public boolean execute(ISniper sniper, String[] args)
    {
        return execute(sniper, extractArguements(args));
    }

    public String[] getAllAliases()
    {
        return this.aliases;
    }
    
    public void setAliases(String... aliases)
    {
        this.aliases = aliases;
    }
    
    private Map<String, CommandArguement<?>> extractArguements(String[] args)
    {
        return null;
    }
    
}
