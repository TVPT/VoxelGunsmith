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

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.brush.BrushContext;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushWrapper;
import com.voxelplugineering.voxelsniper.brush.GlobalBrushManager;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.service.config.Configuration;
import com.voxelplugineering.voxelsniper.service.platform.PlatformProxy;
import com.voxelplugineering.voxelsniper.util.Context;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class VSCommand extends Command
{

    private final PlatformProxy platform;
    private final Map<String, SubCommand> subcommands;

    /**
     * Constructs a new {@link VSCommand}.
     * 
     * @param context The context
     */
    public VSCommand(Context context)
    {
        super("vs", "Sets your current brush", context);
        setAliases("voxelsniper");
        setPermissions("voxelsniper.command.vs");
        this.subcommands = Maps.newHashMap();
        this.platform = context.getRequired(PlatformProxy.class);
        setupSubcommands(context);
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
            sender.sendMessage(VoxelSniperConfiguration.commandPlayerOnly);
            return true;
        }
        Player sniper = (Player) sender;
        String full = "";
        for (String part : args)
        {
            full += part + " ";
        }
        full = full.trim();
        if (full.contains(VoxelSniperConfiguration.keyValueDeliminator))
        {
            String[] split = full.split(VoxelSniperConfiguration.keyValueDeliminator);
            String key = split[0].trim().toLowerCase();
            if(key.startsWith("$")) {
                sniper.sendMessage(VoxelSniperConfiguration.vsInternal);
                return true;
            }
            String value = split[1].trim();
            sniper.getBrushVars().set(BrushContext.GLOBAL, key, value);
            sniper.sendMessage(VoxelSniperConfiguration.globalSet, key, value);
            return true;
        }
        sender.sendMessage("VoxelSniper meta commands, Usage: \'/vs key=value\' or one of:");
        for (String spec : this.subcommands.keySet())
        {
            sender.sendMessage(this.subcommands.get(spec).getHelp());
        }
        return false;
    }

    private void setupSubcommands(Context context)
    {
        this.subcommands.put("brushes", new SubCommand(context, this.platform)
        {

            @Override
            boolean execute(CommandSender sender, String[] args)
            {
                EnumMap<BrushPartType, List<BrushWrapper>> brushes = new EnumMap<BrushPartType, List<BrushWrapper>>(BrushPartType.class);
                for (BrushPartType type : BrushPartType.values())
                {
                    brushes.put(type, new ArrayList<BrushWrapper>());
                }
                for (BrushWrapper b : getContext().getRequired(GlobalBrushManager.class).getBrushes())
                {
                    brushes.get(b.getType()).add(b);
                }
                for (BrushPartType type : BrushPartType.values())
                {
                    String s = type.name() + " brushes: ";
                    List<BrushWrapper> brushesForType = brushes.get(type);
                    for (int i = 0; i < brushesForType.size(); i++)
                    {
                        s += brushesForType.get(i).getName();
                        if (i < brushesForType.size() - 1)
                        {
                            s += ", ";
                        }
                    }
                    sender.sendMessage(s);
                }
                return true;
            }

            @Override
            String getHelp()
            {
                return "  /vs brushes -- displays available brushes";
            }

        });
        this.subcommands.put("range", new SubCommand(context, this.platform)
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
                    if ("reset".equalsIgnoreCase(args[0]))
                    {
                        double range = VoxelSniperConfiguration.rayTraceRange;
                        player.getBrushVars().set(BrushContext.GLOBAL, BrushKeys.RANGE, range);
                        sender.sendMessage(VoxelSniperConfiguration.vsRangeReset, range);
                    }
                    try
                    {
                        double range = Double.parseDouble(args[0]);
                        player.getBrushVars().set(BrushContext.GLOBAL, BrushKeys.RANGE, range);
                        sender.sendMessage(VoxelSniperConfiguration.vsRangeSet, Integer.parseInt(args[0]));
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
        this.subcommands.put("reloadconf", new SubCommand(context, this.platform)
        {

            @Override
            boolean execute(CommandSender sender, String[] args)
            {

                Configuration conf = getContext().getRequired(Configuration.class);
                PlatformProxy platform = getPlatform();
                try
                {
                    conf.load(platform.getRoot());
                    sender.sendMessage(VoxelSniperConfiguration.vsConfigReloadSuccess);
                } catch (IOException e)
                {
                    GunsmithLogger.getLogger().error(e, "Error loading configuration files.");
                    sender.sendMessage(VoxelSniperConfiguration.vsConfigReloadFailed);
                }
                return true;
            }

            @Override
            String getHelp()
            {
                return "  /vs reloadconf - reloads configuration from file";
            }

        });
    }

    /**
     * A subcommand.
     */
    private abstract static class SubCommand
    {

        private final Context context;
        private final PlatformProxy platform;

        public SubCommand(Context context, PlatformProxy platform)
        {
            this.context = context;
            this.platform = platform;
        }

        public PlatformProxy getPlatform()
        {
            return this.platform;
        }

        public Context getContext()
        {
            return this.context;
        }

        abstract boolean execute(CommandSender sender, String[] args);

        abstract String getHelp();

    }
}
