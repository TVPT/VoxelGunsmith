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
package com.voxelplugineering.voxelsniper.commands;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Map;

import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.service.config.Configuration;
import com.voxelplugineering.voxelsniper.service.platform.PlatformProxy;
import com.voxelplugineering.voxelsniper.util.Context;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class VSCommand extends Command
{

    private final PlatformProxy platform;

    private Map<String, SubCommand> subcommands;

    /**
     * Constructs a new BrushCommand
     */
    public VSCommand(Context context)
    {
        super("vs", "Sets your current brush", context);
        setAliases("voxelsniper");
        setPermissions("voxelsniper.command.vs");
        this.subcommands = Maps.newHashMap();
        this.platform = context.getRequired(PlatformProxy.class);
        setupSubcommands();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        checkNotNull(sender, "Cannot have a null sniper!");

        if (args.length >= 1)
        {
            if (this.subcommands.containsKey(args[0]))
            {
                boolean s = this.subcommands.get(args[0]).execute(sender, Arrays.copyOfRange(args, 1, args.length));
                if (s)
                {
                    return true;
                }
            }
        }

        if (!sender.isPlayer())
        {
            sender.sendMessage("Sorry this is a player-only command.");
            return true;
        }
        Player sniper = (Player) sender;
        String full = "";
        for (String part : args)
        {
            full += part + " ";
        }
        full = full.trim();
        if (full.contains("="))
        {
            String[] split = full.split("=");
            String key = split[0].trim().toLowerCase();
            String value = split[1].trim();
            sniper.getBrushVars().set(BrushContext.GLOBAL, key, value);
            sniper.sendMessage("Set " + key + " to " + value);
            return true;
        }
        sender.sendMessage("VoxelSniper meta commands, Usage: \'/vs key=value\' or one of:");
        for (String spec : this.subcommands.keySet())
        {
            sender.sendMessage(this.subcommands.get(spec).getHelp());
        }
        return false;
    }

    private void setupSubcommands()
    {
        this.subcommands.put("version", new SubCommand(this.config, this.platform)
        {

            @Override
            boolean execute(CommandSender sender, String[] args)
            {
                sender.sendMessage("VoxelSniper version " + this.platform.getFullVersion());
                return true;
            }

            @Override
            String getHelp()
            {
                return "  /vs version -- displays version info";
            }

        });
        this.subcommands.put("range", new SubCommand(this.config, this.platform)
        {

            @Override
            boolean execute(CommandSender sender, String[] args)
            {
                if (!sender.isPlayer())
                {
                    return false;
                }
                Player player = (Player) sender;
                if (args.length >= 1)
                {
                    if (args[0].equalsIgnoreCase("reset"))
                    {
                        double range = this.conf.get("rayTraceRange", Double.class).get();
                        player.getBrushVars().set(BrushContext.GLOBAL, BrushKeys.RANGE, range);
                        sender.sendMessage("Reset your maximum range to %d", range);
                    }
                    try
                    {
                        double range = Double.parseDouble(args[0]);
                        player.getBrushVars().set(BrushContext.GLOBAL, BrushKeys.RANGE, range);
                        sender.sendMessage("Set your maximum range to %d", Integer.parseInt(args[0]));
                    } catch (NumberFormatException e)
                    {
                        sender.sendMessage("Usage: /vs range #");
                    }
                    return true;
                }
                sender.sendMessage("Usage: /vs range #");
                return true;
            }

            @Override
            String getHelp()
            {
                return "  /vs range # -- sets maximum range";
            }

        });
        /*this.subcommands.put("unittest", new SubCommand(this.config, this.platform)
        {

            @Override
            boolean execute(CommandSender sender, String[] args)
            {
                if (sender instanceof Player)
                {
                    new Thread(new IngameBrushTest((Player) sender)).start();
                } else
                {
                    sender.sendMessage("Sorry this is a player only sub command.");
                }
                return true;
            }

            @Override
            String getHelp()
            {
                return "  /vs unittest -- Runs an ingame set of brush tests (warning: destructive)";
            }

        });*/
    }
}

abstract class SubCommand
{

    protected final Configuration conf;
    protected final PlatformProxy platform;

    public SubCommand(Configuration conf, PlatformProxy platform)
    {
        this.conf = conf;
        this.platform = platform;
    }

    abstract boolean execute(CommandSender sender, String[] args);

    abstract String getHelp();

}
