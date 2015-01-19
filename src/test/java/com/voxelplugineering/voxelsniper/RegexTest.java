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

import org.junit.Before;
import org.junit.Test;

import com.voxelplugineering.voxelsniper.api.brushes.BrushParser;
import com.voxelplugineering.voxelsniper.api.brushes.BrushParser.BrushPart;
import com.voxelplugineering.voxelsniper.brushes.StandardBrushParser;

/**
 * Regex tests TODO: The layout of this test is bad, as is the coverage
 */
public class RegexTest
{

    BrushParser parser;

    /**
     * 
     */
    @Before
    public void setup()
    {
        this.parser = new StandardBrushParser();
    }

    /**
     * 
     */
    @Test
    public void check0()
    {
        test("hello", new String[] { "hello" }, new String[] { "" });
    }

    /**
     * 
     */
    @Test
    public void check1()
    {
        test("hello {world}", new String[] { "hello" }, new String[] { "world" });
    }

    /**
     * 
     */
    @Test
    public void check2()
    {
        test("hello{world}", new String[] { "hello" }, new String[] { "world" });
    }

    /**
     * 
     */
    @Test
    public void check3()
    {
        test("hello {world} ape", new String[] { "hello", "ape" }, new String[] { "world", "" });
    }

    /**
     * 
     */
    @Test
    public void check4()
    {
        test("hello {world} ape orange banana", new String[] { "hello", "ape", "orange", "banana" }, new String[] { "world", "", "", "" });
    }

    /**
     * 
     */
    @Test
    public void check5()
    {
        test("hello {wor ld} ape", new String[] { "hello", "ape" }, new String[] { "wor ld", "" });
    }

    /**
     * 
     */
    @Test
    public void check6()
    {
        test("hello {world} ape or$$$$ange {tst tsj ;;2*$29f fsff} banana*@(#%", new String[] { "hello", "ape", "or$$$$ange", "banana*@(#%" },
                new String[] { "world", "", "tst tsj ;;2*$29f fsff", "" });
    }

    /**
     * 
     */
    @Test
    public void check7()
    {
        test("hello {wor ld}{other} ape", new String[] { "hello", "ape" }, new String[] { "wor ld", "" });
    }

    /**
     * 
     */
    @Test
    public void check8()
    {
        test("hello {wor ld} {other} ape", new String[] { "hello", "ape" }, new String[] { "wor ld", "" });
    }

    /**
     * 
     */
    @Test
    public void check9()
    {
        test("{ignored} hello {wor ld}", new String[] { "hello" }, new String[] { "wor ld" });
    }

    /**
     * 
     */
    @Test
    public void check10()
    {
        test("invalid {wor ld", new String[] {}, new String[] {});
    }

    /**
     * 
     */
    @Test
    public void check11()
    {
        test("invalid {wor ld {embedded} oh dear}", new String[] {}, new String[] {});
    }

    private void test(String full, String[] brushes, String[] arguments)
    {
        if (brushes.length == 0 || arguments.length == 0)
        {
            assertEquals(false, this.parser.validate(full));
            return;
        }
        assertEquals(true, this.parser.validate(full));
        BrushPart[] parts = this.parser.parse(full).get();
        assertEquals(brushes.length, parts.length);
        assertEquals(arguments.length, parts.length);
        int i = 0;
        for (BrushPart part : parts)
        {
            assertEquals(brushes[i], part.getBrushName());
            assertEquals(arguments[i], part.getBrushArgument());
            i++;
        }
    }

}
