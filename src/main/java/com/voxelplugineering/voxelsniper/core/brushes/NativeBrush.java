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
package com.voxelplugineering.voxelsniper.core.brushes;

import com.thevoxelbox.vsl.api.node.Node;
import com.voxelplugineering.voxelsniper.api.brushes.Brush;
import com.voxelplugineering.voxelsniper.api.brushes.BrushPartType;
import com.voxelplugineering.voxelsniper.api.commands.ArgumentParser;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;

/**
 * A brush which executes its function by direct java code.
 */
public abstract class NativeBrush implements Brush
{

    private final BrushPartType type;
    private String help = "No help provided.";

    /**
     * Sets up a new {@link NativeBrush} with the given name and type.
     * 
     * @param name The brush name
     * @param type The brush type
     */
    public NativeBrush(String name, BrushPartType type)
    {
        this.type = type;
    }

    @Override
    public BrushPartType getType()
    {
        return this.type;
    }

    @Override
    public String getHelp()
    {
        return this.help;
    }

    @Override
    public void setHelp(String help)
    {
        this.help = help;
    }

    @Override
    public Node getStartNode()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStartNode(Node node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addArgument(String name, ArgumentParser<?> parser, String defaultValue, String... aliases)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setArgumentAsPrimary(String name)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fromContainer(DataContainer container)
    {

    }

    @Override
    public DataContainer toContainer()
    {
        return null;
    }

}
