package com.voxelplugineering.voxelsniper.common.command.args;

import java.util.List;

import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.command.CommandArgument;

public class RawArgument extends CommandArgument
{
    private String[] args;
    
    public RawArgument(final String name)
    {
        super(name);
    }
    
    public String[] getValue()
    {
        return this.args;
    }

    @Override
    public String getUsageString(boolean optional)
    {
        return "";
    }

    @Override
    public int matches(ISniper user, String[] allArgs, int startPosition)
    {
        return 1;
    }

    @Override
    public void parse(ISniper user, String[] allArgs, int startPosition)
    {
        args = allArgs;
    }

    @Override
    public void skippedOptional(ISniper user)
    {
        
    }

    @Override
    public void clean()
    {
        args = null;
    }

    @Override
    public List<String> tabComplete(ISniper user, String[] allArgs, int startPosition)
    {
        return null;
    }

}
