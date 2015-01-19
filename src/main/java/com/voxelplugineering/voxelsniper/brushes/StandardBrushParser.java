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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.voxelplugineering.voxelsniper.api.brushes.BrushParser;

/**
 * A standard brush parser.
 */
public class StandardBrushParser implements BrushParser
{

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(String full)
    {
        int co = 0;
        for (char c : full.toCharArray())
        {
            if (c == '{')
            {
                if (co > 0)
                {
                    return false;
                }
                co++;
            }
            if (c == '}')
            {
                if (co < 0)
                {
                    return false;
                }
                co--;
            }
        }
        return co == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BrushPart[]> parse(String full)
    {
        if (!validate(full))
        {
            return Optional.absent();
        }
        if (full.startsWith("{"))
        {
            full = "_" + full;
        }
        Pattern pattern = Pattern.compile("([\\S&&[^\\{]]+)[\\s]*(?:((?:\\{[^\\}]*\\}[\\s]*)+))?");
        Matcher match = pattern.matcher(full);
        List<BrushPart> parts = Lists.newArrayList();
        while (match.find())
        {
            String brushName = match.group(1);
            if (brushName == null || brushName.isEmpty() || brushName.equals("_"))
            {
                continue;
            }
            String brushArgument = getFirst(match.group(2));
            parts.add(new BrushPart(brushName, brushArgument));
        }
        return Optional.of(parts.toArray(new BrushPart[parts.size()]));
    }

    private String getFirst(String arg)
    {
        if (arg == null)
        {
            return null;
        }
        String ret = arg.trim();
        Pattern pattern = Pattern.compile("\\{([^\\}]*)\\}");
        Matcher match = pattern.matcher(ret);
        match.find();
        return match.group(1);
    }

}
