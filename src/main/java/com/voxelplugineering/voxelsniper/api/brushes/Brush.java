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
package com.voxelplugineering.voxelsniper.api.brushes;

import com.thevoxelbox.vsl.api.node.Node;
import com.thevoxelbox.vsl.api.variables.VariableHolder;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.service.command.ArgumentParser;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;

/**
 * Represents a brush which may be run, only one brush instance should exist and be shared among all
 * users. A users individual runtime settings are stored within the {@link RuntimeState}.
 */
public interface Brush extends DataSerializable
{

    //TODO versioning

    /**
     * Gets the name of this brush, otherwise known as the primary alias.
     * 
     * @return The name
     */
    String getName();

    /**
     * Gets the type of this brush.
     * 
     * @return The type
     */
    BrushPartType getType();

    /**
     * Gets any provided help for the usage of this brush.
     * 
     * @return The help
     */
    String getHelp();

    /**
     * Sets the help message for this brush.
     * 
     * @param help The new help
     */
    void setHelp(String help);

    /**
     * Gets the starting node of this brush.
     * 
     * @return The start node
     */
    Node getStartNode();

    /**
     * Sets the starting node for this brush.
     * 
     * @param node The starting node
     */
    void setStartNode(Node node);

    /**
     * Executes this brush within the context of the given {@link RuntimeState}.
     * 
     * @param state The runtime state
     */
    void run(RuntimeState state);

    /**
     * Adds an argument to this node.
     * 
     * @param name The name
     * @param parser The parser
     */
    void addArgument(String name, ArgumentParser<?> parser);

    /**
     * Sets an argument by name as the primary argument. If only a single argument is provided it is
     * assumed to be this argument.
     * 
     * @param name The argument to set as primary
     */
    void setArgumentAsPrimary(String name);

    /**
     * Parses the arguments for this brush into the given {@link VariableHolder}
     * 
     * @param string The args
     * @param vars The variables
     */
    void parseArguments(String string, VariableHolder vars);

}
