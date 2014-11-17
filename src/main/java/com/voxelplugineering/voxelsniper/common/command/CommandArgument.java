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

import java.util.List;

import com.voxelplugineering.voxelsniper.api.ISniper;

/**
 * A command argument handling validation of arguments passed to commands.
 *
 * @param <T> the generic implied type for getting and setting choice
 */
public abstract class CommandArgument<T>
{
    /**
     * Whether this argument is required.
     */
    private final boolean required;
    /**
     * The name of this argument. Used to reference this in the command executor.
     */
    private final String name;
    /**
     * Whether the argument was present in the last parse.
     */
    protected boolean isPresent;

    /**
     * Creates a new CommandArgument with the given name.
     *
     * @param name the name of this argument
     */
    protected CommandArgument(String name)
    {
        this(name, false);
    }

    /**
     * Creates a new CommandArgument with the given name and requirement.
     *
     * @param name the name of this argument
     * @param required whether this argument is required for command validation
     */
    protected CommandArgument(String name, boolean required)
    {
        this.required = required;
        this.name = name;
    }

    /**
     * Returns if this argument is optional.
     * 
     * @return is optional
     */
    public boolean isOptional()
    {
        return !this.required;
    }

    /**
     * Whether this argument was present.
     * 
     * @return is present
     */
    public boolean isPresent()
    {
        return this.isPresent;
    }

    /**
     * The name of this argument.
     * 
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the current choice for the ISniper executing the command containing this argument. The choice should already be validated after parsing.
     *
     * @return the parsed choice
     */
    public abstract T getChoice();

    /**
     * Sets the current choice for the ISniper executing the command containing this argument. The choice will be re-validated.
     * 
     * @param choice the choice
     * @return true if validated and successful
     */
    public abstract boolean setChoice(T choice);

    /**
     * Returns the sub-part of the usage string representing this argument.
     * 
     * @param optional whether this argument is optional
     * @return the part of the usage string
     */
    public abstract String getUsageString(boolean optional);

    /**
     * Attempts to match the argument in the argument array at the startPosition to this arguments parameters.
     * 
     * @param user the command sender
     * @param allArgs the argument array
     * @param startPosition the current position of the argument validation
     * @return the match result
     */
    public abstract int matches(ISniper user, String[] allArgs, int startPosition);

    /**
     * Parses the args array at the startPosition for this argument and stores it. Sets the flag if the argument is present.
     * 
     * @param user the command sender
     * @param allArgs the args array
     * @param startPosition the current position of the argument validation
     */
    public abstract void parse(ISniper user, String[] allArgs, int startPosition);

    /**
     * This method is called instead of parse() when an optional CommandArgument is skipped (i.e. not matched).
     *
     * @param user The user executing this method
     */
    public abstract void skippedOptional(ISniper user);

    /**
     * Resets the state of the CommandArgument ready for another execution cycle.
     */
    public abstract void clean();

    /**
     * Returns a {@link List} of possible values based on the current value of this argument.
     * 
     * @param user the command sender
     * @param allArgs the args array
     * @param startPosition the current position of the argument validation
     * @return a list of possible values
     */
    public abstract List<String> tabComplete(ISniper user, String[] allArgs, int startPosition);

}
