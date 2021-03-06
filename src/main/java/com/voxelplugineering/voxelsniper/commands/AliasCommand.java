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

import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.service.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.service.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.service.alias.GlobalAliasHandler;
import com.voxelplugineering.voxelsniper.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.StringUtilities;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class AliasCommand extends Command
{

    private final GlobalAliasHandler aliases;

    /**
     * Constructs a new {@link AliasCommand}.
     * 
     * @param context The context
     */
    public AliasCommand(Context context)
    {
        super("alias", "Sets an alias: /alias [target] [-g] alias=value", context);
        setAliases("voxelalias");
        setPermissions("voxelsniper.command.alias");
        this.aliases = context.getRequired(GlobalAliasHandler.class);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        checkNotNull(sender, "Cannot have a null sniper!");

        if (!sender.isPlayer())
        {
            sender.sendMessage(VoxelSniperConfiguration.commandPlayerOnly);
            return true;
        }
        Player sniper = (Player) sender;
        if (args.length < 2)
        {
            sniper.sendMessage(this.getHelpMsg());
            return true;
        }
        String target = args[0];
        boolean global = "-g".equalsIgnoreCase(args[1]) && getPerms().hasPermission(sniper, "voxelsniper.command.alias.global");
        AliasHandler alias = global ? this.aliases : sniper.getAliasHandler();
        int n = global ? 2 : 1;
        if (alias.hasTarget(target))
        {
            AliasRegistry registry = alias.getRegistry(target).get();
            String arg = StringUtilities.getSection(args, n, args.length - 1);
            if (!arg.contains(VoxelSniperConfiguration.keyValueDeliminator))
            {
                if (StringUtilities.startsWithIgnoreCase(arg, "clear") && !global)
                {
                    registry.clear();
                }
                sniper.sendMessage(this.getHelpMsg());
                return true;
            }
            int k = arg.indexOf(VoxelSniperConfiguration.keyValueDeliminator);
            String key = arg.substring(0, k).trim();
            String value = arg.substring(k + 1, arg.length()).trim();
            registry.register(key, value);
            this.aliases.getAliasSaveTask().addDirty(alias);
            sniper.sendMessage(VoxelSniperConfiguration.aliasSet, key, value);
        } else
        {
            sniper.sendMessage(VoxelSniperConfiguration.aliasNotFound, target, global ? "the global" : "your personal");
        }
        return true;
    }

}
