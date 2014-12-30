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
package com.voxelplugineering.voxelsniper.brushes;

import com.thevoxelbox.vsl.api.IVariableHolder;
import com.thevoxelbox.vsl.node.NodeGraph;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;

/**
 * The Gunsmith specific {@link NodeGraph}.
 */
public class BrushNodeGraph extends NodeGraph
{

    String help = "No help is provided for this brush part.";
    String[] requiredVars = new String[0];

    /**
     * Creates a new {@link BrushNodeGraph}.
     * 
     * @param name The brush name
     */
    public BrushNodeGraph(String name)
    {
        super(name);

    }

    @Override
    public void run(IVariableHolder vars)
    {
        String missing = "";
        for (String s : requiredVars)
        {
            if (!vars.hasValue(s))
            {
                if (!missing.isEmpty())
                {
                    missing += " ";
                }
                missing += s;
            }
        }
        if (!missing.isEmpty())
        {
            vars.<Player>get("__PLAYER__", Player.class).get()
                    .sendMessage("Missing required variable" + (missing.indexOf(" ") != -1 ? "s" : " ") + ": " + missing);
            return;
        }
        RuntimeState state = new RuntimeState(vars);
        exec(state);
    }

    /**
     * Sets the variables required to be set for this brush to function.
     * 
     * @param req The required variables
     */
    public void setRequiredVars(String... req)
    {
        this.requiredVars = req;
    }

    /**
     * Sets the help message for this brush.
     * 
     * @param help The help message
     */
    public void setHelp(String help)
    {
        this.help = help;
    }

    /**
     * Gets the help message for this brush.
     * 
     * @return The help message
     */
    public String getHelp()
    {
        return this.help;
    }

}
