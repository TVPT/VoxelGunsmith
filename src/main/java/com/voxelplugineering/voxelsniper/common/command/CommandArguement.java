package com.voxelplugineering.voxelsniper.common.command;

import java.util.List;

import com.voxelplugineering.voxelsniper.api.ISniper;

public abstract class CommandArguement
{

    private final boolean required;
    protected boolean isPresent;

    protected CommandArguement()
    {
        this(false);
    }

    protected CommandArguement(boolean required)
    {
        this.required = required;
    }

    public boolean isOptional()
    {
        return !this.required;
    }

    public boolean isPresent()
    {
        return this.isPresent;
    }

    public abstract String getUsageString(boolean optional);

    public abstract int matches(ISniper user, String[] allArgs, int startPosition);

    public abstract void parse(ISniper user, String[] allArgs, int startPosition);

    /**
     * This method is called instead of parse() when an optional CommandArgument
     * is skipped (i.e. not matched).
     *
     * @param user The user executing this method
     */
    public abstract void skippedOptional(ISniper user);

    public abstract void clean();

    public abstract List<String> tabComplete(ISniper user, String[] allArgs, int startPosition);

}
