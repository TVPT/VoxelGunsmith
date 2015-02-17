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

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;
import com.thevoxelbox.vsl.api.node.NodeGraph;
import com.thevoxelbox.vsl.api.variables.VariableHolder;
import com.thevoxelbox.vsl.node.RunnableNodeGraph;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.brushes.BrushPartType;
import com.voxelplugineering.voxelsniper.api.commands.ArgumentParser;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSerializable;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormat;

/**
 * The Gunsmith specific {@link NodeGraph}.
 */
public class BrushNodeGraph extends RunnableNodeGraph implements DataSerializable
{
    
    /**
     * Attempts to create a {@link BrushNodeGraph} from the given {@link DataContainer}.
     * 
     * @param container The container
     * @return The graph, or null
     */
    public static BrushNodeGraph buildFromContainer(DataContainer container)
    {
        if(container.contains("name"))
        {
            BrushNodeGraph brush = new BrushNodeGraph(container.readString("name").get());
            brush.fromContainer(container);
            return brush;
        }
        else
        {
            return null;
        }
    }

    private String help = Gunsmith.getConfiguration().get("defaultBrushHelpMessage", String.class)
            .or(TextFormat.RED + "No help is provided for this brush part.");
    private final Map<String, ArgumentParser<?>> arguments;
    private final Map<String, String> argDefaults;
    private String primary = null;
    private final BrushPartType type;

    /**
     * Creates a new {@link BrushNodeGraph}.
     * 
     * @param name The brush name
     * @param type The type
     */
    public BrushNodeGraph(String name, BrushPartType type)
    {
        super(name);
        this.arguments = Maps.newHashMap();
        this.argDefaults = Maps.newHashMap();
        this.type = type;
    }

    /**
     * Creates a new {@link BrushNodeGraph}.
     * 
     * @param name The brush name
     */
    public BrushNodeGraph(String name)
    {
        this(name, BrushPartType.MISC);
    }

    /**
     * Gets the general type of this brush part.
     * 
     * @return The type
     */
    public BrushPartType getType()
    {
        return this.type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(VariableHolder vars)
    {
        run(vars, null);
    }

    /**
     * Executes this graph with the given arguments.
     * 
     * @param vars The variables
     * @param map The arguments
     */
    public void run(VariableHolder vars, Map<String, String> map)
    {
        RuntimeState state = new RuntimeState(vars);
        BrushNodeGraph ng = this;
        while (ng != null && map != null)
        {
            String inv = ng.parseArguments(map.get(ng.getName()), state.getVars(), vars.<Player> get("__PLAYER__", Player.class).get());
            if (!inv.isEmpty())
            {
                vars.<Player> get("__PLAYER__", Player.class).get().sendMessage("Invalid arguments: " + inv);
                return;
            }
            ng = (BrushNodeGraph) ng.getNextGraph();
        }
        String missing = "";
        for (String s : this.arguments.keySet())
        {
            if (!vars.hasValue(s))
            {
                if (this.argDefaults.containsKey(s))
                {
                    vars.set(s, this.arguments.get(s).get(this.argDefaults.get(s)));
                } else
                {
                    if (!missing.isEmpty())
                    {
                        missing += " ";
                    }
                    missing += s;
                }
            }
        }
        if (!missing.isEmpty())
        {
            vars.<Player> get("__PLAYER__", Player.class).get()
                    .sendMessage("Missing required variable" + (missing.indexOf(" ") != -1 ? "s" : " ") + ": " + missing);
            return;
        }
        exec(state);
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

    /**
     * Adds an argument.
     * 
     * @param name The name
     * @param parser The argument parser
     * @param defaultValue The default value, or null
     * @param aliases Any aliases, optional
     */
    public void addArgument(String name, ArgumentParser<?> parser, String defaultValue, String... aliases)
    {
        if (defaultValue != null && !defaultValue.isEmpty())
        {
            this.argDefaults.put(name, defaultValue);
        }
        this.arguments.put(name, parser);
        for (String alias : aliases)
        {
            this.arguments.put(alias, parser);
        }
    }

    /**
     * Sets a certain argument as the primary argument. The argument specific
     * must have already been added with
     * {@link #addArgument(String, ArgumentParser, String, String...)}.
     * 
     * @param arg The new primary argument
     */
    public void setArgumentAsPrimary(String arg)
    {
        if (!this.arguments.containsKey(arg))
        {
            this.primary = null;
            return;
        }
        this.primary = arg;
    }

    private String parseArguments(String group, VariableHolder vars, Player player)
    {
        if (group == null)
        {
            return "";
        }
        String arg = group.replaceAll("[\\{\\}]", " ").trim();
        String[] args = arg.split(" ");
        if (args.length == 1 && this.primary != null)
        {
            vars.set(this.primary, this.arguments.get(this.primary).get(arg).get());
        } else
        {
            String invalid = "";
            for (String s : args)
            {
                if (!s.contains("="))
                {
                    invalid += s + " ";
                    continue;
                }
                String[] kv = s.split("=");
                if (this.arguments.containsKey(kv[0]))
                {
                    vars.set(kv[0], this.arguments.get(kv[0]).get(kv[1]));
                } else
                {
                    invalid += kv[0] + " ";
                }
            }
            if (!invalid.isEmpty())
            {
                return invalid;
            }
        }
        return "";
    }

    /**
     * Gets the primary argument name.
     * 
     * @return The primary argument
     */
    public String getPrimaryArgument()
    {
        return this.primary;
    }

    /**
     * Gets all arguments.
     * 
     * @return The arguments
     */
    public Map<String, ArgumentParser<?>> getArguments()
    {
        return Collections.unmodifiableMap(this.arguments);
    }

    @Override
    public void fromContainer(DataContainer container)
    {
        // TODO fromContainer
        
    }

    @Override
    public DataContainer toContainer()
    {
        // TODO toContainer
        return null;
    }

}
