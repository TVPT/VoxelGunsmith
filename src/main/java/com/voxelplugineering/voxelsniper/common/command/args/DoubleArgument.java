package com.voxelplugineering.voxelsniper.common.command.args;

import java.util.List;

import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.command.CommandArguement;

public class DoubleArgument extends CommandArguement
{
    private double value;
    private String desc = "num";

    public DoubleArgument(boolean required)
    {
        super(required);
    }

    public DoubleArgument setDesc(String description)
    {
        this.desc = description;
        return this;
    }

    public double getValue()
    {
        return this.value;
    }

    public void setValue(double val)
    {
        this.value = val;
    }

    @Override
    public String getUsageString(boolean optional)
    {
        if (optional)
        {
            return '[' + this.desc + ']';
        } else
        {
            return '<' + this.desc + '>';
        }
    }

    @Override
    public int matches(ISniper user, String[] allArgs, int startPosition)
    {
        String arg = allArgs[startPosition];
        try
        {
            Double.parseDouble(arg);
            return 1;
        } catch (NumberFormatException e)
        {
            return -1;
        }
    }

    @Override
    public void parse(ISniper user, String[] allArgs, int startPosition)
    {
        String arg = allArgs[startPosition];
        try
        {
            this.value = Double.parseDouble(arg);
        } catch (NumberFormatException e)
        {
            this.value = -Double.MAX_VALUE;
        }
    }

    @Override
    public void skippedOptional(ISniper user)
    {
        this.value = -Double.MAX_VALUE;
    }

    @Override
    public void clean()
    {
        this.value = -Double.MAX_VALUE;
    }

    @Override
    public List<String> tabComplete(ISniper user, String[] allArgs, int startPosition)
    {
        return null;
    }
}
