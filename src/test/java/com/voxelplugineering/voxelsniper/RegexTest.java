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
package com.voxelplugineering.voxelsniper;

import static org.junit.Assert.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

/**
 * Regex tests
 */
public class RegexTest
{

    Pattern pattern;

    /**
     * 
     */
    @Before
    public void setup()
    {
        this.pattern = Pattern.compile("([\\S&&[^\\{]]+)[\\s]*(?:((?:\\{[^\\}]*\\}[\\s]*)*))?");
    }

    /**
     * 
     */
    @Test
    public void test()
    {
        assertEquals(1, check("hello {world}"));
        assertEquals(1, check("hello{world}"));
        assertEquals(2, check("hello {world} ape"));
        assertEquals(4, check("hello {world} ape orange banana"));
        assertEquals(2, check("hello {wor ld} ape"));
        assertEquals(4, check("hello {world} ape or$$$$ange {tst tsj ;;2*$29f fsff} banana*@(#%"));
        assertEquals(2, check("hello {wor ld}{other} ape"));
        assertEquals(2, check("hello {wor ld} {other} ape"));
        assertEquals(1, check("{ignored} hello {wor ld}"));
        assertEquals(0, check("invalid {wor ld"));
        assertEquals(0, check("invalid {wor ld {embedded} oh dear}"));
    }

    private String prep(String s)
    {
        int count = 0;
        for (char c : s.toCharArray())
        {
            if (c == '{')
            {
                if (count > 0)
                {
                    return "";
                }
                count++;
            } else if (c == '}')
            {
                if (count < 0)
                {
                    return "";
                }
                count--;
            }
        }
        if (count != 0)
        {
            return "";
        }
        s = s.trim();
        while (s.startsWith("{"))
        {
            int index = s.indexOf("}");
            if (index == -1)
            {
                s = s.substring(1);
            }
            s = s.substring(index + 1).trim();
        }
        return s;
    }

    private int check(String s)
    {
        Matcher m = this.pattern.matcher(prep(s));
        int i = 0;
        while (m.find())
        {
            i++;
        }
        return i;
    }
}
