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
package com.voxelplugineering.voxelsniper.common.commands;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.common.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.common.command.Command;
import com.voxelplugineering.voxelsniper.common.command.CommandArgument;
import com.voxelplugineering.voxelsniper.common.command.args.RawArgument;
import com.voxelplugineering.voxelsniper.util.Utilities;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class AliasCommand extends Command
{

    /**
     * Constructs a new BrushCommand
     */
    public AliasCommand()
    {
        super("alias", "Sets an alias: /alias [target] [-g] alias=value");
        addArgument(new RawArgument("raw"));
        setPermissions("voxelsniper.command.alias");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(ISniper sniper, Map<String, CommandArgument<?>> args)
    {
        checkNotNull(sniper, "Cannot have a null sniper!");
        String[] s = ((RawArgument) args.get("raw")).getChoice();

        if (s.length < 2)
        {
            sniper.sendMessage(this.getHelpMsg());
            return true;
        }
        String target = s[0];
        boolean global = s[1].equalsIgnoreCase("-g");
        AliasHandler alias = global ? Gunsmith.getGlobalAliasHandler() : sniper.getPersonalAliasHandler();
        int n = global ? 2 : 1;
        if (alias.hasTarget(target))
        {
            AliasRegistry registry = alias.getRegistry(target).get();
            String arg = Utilities.getSection(s, n, s.length - 1);
            if (!arg.contains("="))
            {
                if (Utilities.startsWithIgnoreCase(arg, "clear") && !global)
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
            sniper.sendMessage("Added alias mapping '%s' to '%s'", key, value);
        } else
        {
            sniper.sendMessage("Alias target %s was not found in %s registry.", target, global ? "the global" : "your personal");
        }
        return true;
    }

}
