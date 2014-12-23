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

import com.voxelplugineering.voxelsniper.api.ISniper;
import com.voxelplugineering.voxelsniper.common.command.Command;

/**
 * Standard brush command to select a brush and provide the necessary arguments to said brush.
 */
public class VSCommand extends Command
{

    /**
     * Constructs a new BrushCommand
     */
    public VSCommand()
    {
        super("vs", "Sets your current brush");
        setAliases("voxelsniper");
        setPermissions("voxelsniper.command.vs");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(ISniper sniper, String[] args)
    {
        checkNotNull(sniper, "Cannot have a null sniper!");
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
            sniper.getBrushSettings().set(key, value);
            sniper.sendMessage("Set " + key + " to " + value);
            return true;
        }
        return false;
    }

}
