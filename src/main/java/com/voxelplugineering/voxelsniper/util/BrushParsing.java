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
package com.voxelplugineering.voxelsniper.util;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.api.brushes.Brush;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.brushes.BrushChain;

/**
 * A utility for parsing a command into a {@link BrushChain}.
 */
public class BrushParsing
{

    /**
     * Attempts to parse the given command to a {@link BrushChain} in the
     * context of the given {@link BrushManager}.
     * 
     * @param cmd The command
     * @param manager The brush manager
     * @param aliases The alias registry to use to expand the brush names with
     * @return The brush chain, if no errors occurred
     */
    public static Optional<BrushChain> parse(String cmd, BrushManager manager, AliasRegistry aliases)
    {
        if (aliases != null)
        {
            cmd = aliases.expand(cmd);
        }
        BrushChain brush = new BrushChain(cmd);
        String br = "";
        String arg = "";
        boolean inarg = false;
        boolean clearOnNext = false;
        for (char c : cmd.toCharArray())
        {
            if (c == '{')
            {
                inarg = true;
                if (!br.isEmpty())
                {
                    Optional<Brush> attempt = manager.getBrush(br);
                    if (attempt.isPresent())
                    {
                        brush.chain(attempt.get());
                        continue;
                    } else
                    {
                        return Optional.absent();
                    }
                }
                continue;
            } else if (c == '}')
            {
                inarg = false;
                brush.setBrushArgument(br, arg);
                br = "";
                arg = "";
                continue;
            } else if (c == ' ')
            {
                if (inarg)
                {
                    arg += c;
                    continue;
                } else if (!br.isEmpty())
                {
                    Optional<Brush> attempt = manager.getBrush(br);
                    if (attempt.isPresent())
                    {
                        brush.chain(attempt.get());
                        clearOnNext = true;
                        continue;
                    } else
                    {
                        return Optional.absent();
                    }
                }
            } else
            {
                if (inarg)
                {
                    arg += c;
                } else
                {
                    if (clearOnNext)
                    {
                        clearOnNext = false;
                        br = "";
                    }
                    br += c;
                }
            }

        }
        if (inarg)
        {
            brush.setBrushArgument(br, arg);

        } else
        {
            if (!br.isEmpty())
            {
                Optional<Brush> attempt = manager.getBrush(br);
                if (attempt.isPresent())
                {
                    brush.chain(attempt.get());
                } else
                {
                    return Optional.absent();
                }
            }

        }

        return Optional.of(brush);
    }

}
