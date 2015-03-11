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

import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.api.commands.CommandSender;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.command.Command;
import com.voxelplugineering.voxelsniper.util.StringUtilities;

/**
 * Standard brush command to select a brush and provide the necessary arguments
 * to said brush.
 */
public class AliasCommand extends Command
{

    /**
     * Constructs a new BrushCommand
     */
    public AliasCommand()
    {
        super("alias", "Sets an alias: /alias [target] [-g] alias=value");
        setPermissions("voxelsniper.command.alias");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        checkNotNull(sender, "Cannot have a null sniper!");

        if (!sender.isPlayer())
        {
            sender.sendMessage("Sorry this is a player-only command.");
            return true;
        }
        Player sniper = (Player) sender;
        if (args.length < 2)
        {
            sniper.sendMessage(this.getHelpMsg());
            return true;
        }
        String target = args[0];
        boolean global = args[1].equalsIgnoreCase("-g") && Gunsmith.getPermissionsProxy().hasPermission(sniper, "voxelsniper.command.alias.global");
        AliasHandler alias = global ? Gunsmith.getGlobalAliasHandler() : sniper.getPersonalAliasHandler();
        int n = global ? 2 : 1;
        if (alias.hasTarget(target))
        {
            AliasRegistry registry = alias.getRegistry(target).get();
            String arg = StringUtilities.getSection(args, n, args.length - 1);
            if (!arg.contains("="))
            {
                if (StringUtilities.startsWithIgnoreCase(arg, "clear") && !global)
                {
                    registry.clear();
                }
                sniper.sendMessage(this.getHelpMsg());
                return true;
            }
            int k = arg.indexOf("=");
            String key = arg.substring(0, k).trim();
            String value = arg.substring(k + 1, arg.length()).trim();
            registry.register(key, value);
            //TODO alias save handler
            //Gunsmith.getAliasSaveHandler().addDirty(alias);
            sniper.sendMessage("Added alias mapping '%s' to '%s'", key, value);
        } else
        {
            sniper.sendMessage("Alias target %s was not found in %s registry.", target, global ? "the global" : "your personal");
        }
        return true;
    }

}
