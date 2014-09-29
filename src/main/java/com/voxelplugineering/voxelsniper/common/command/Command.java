package com.voxelplugineering.voxelsniper.common.command;

import java.util.Map;

import com.voxelplugineering.voxelsniper.api.ISniper;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ObjectArrays.concat;

public abstract class Command
{

    String[] aliases;
    private CommandArguement[] arguements;
    private final String name;

    protected Command(final String name)
    {
        checkNotNull(name, "Cannot have a null name!");
        checkArgument(!name.isEmpty(), "Cannot have an empty name!");
        this.name = name;
    }

    protected void addArgument(CommandArguement arguement)
    {
        checkNotNull(arguement, "Cannot add a null argument!");
        if (this.arguements == null)
        {
            this.arguements = new CommandArguement[]{arguement};
            return;
        }
        concat(this.arguements, arguement);
    }

    public abstract boolean execute(ISniper sniper, Map<String, CommandArguement> args);

    public boolean execute(ISniper sniper, String[] args)
    {
        return execute(sniper, extractArguements(args));
    }

    public String[] getAllAliases()
    {
        return this.aliases;
    }

    protected void setAliases(String... aliases)
    {
        this.aliases = aliases;
    }

    private Map<String, CommandArguement> extractArguements(String[] args)
    {
        return null;
    }

}
