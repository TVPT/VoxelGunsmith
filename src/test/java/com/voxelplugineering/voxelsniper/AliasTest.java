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

import com.voxelplugineering.voxelsniper.alias.CommonAliasRegistry;
import com.voxelplugineering.voxelsniper.api.alias.AliasRegistry;

/**
 * A test suite for the alias system
 */
public class AliasTest
{

    AliasRegistry alias;

    /**
     * Sets up an {@link AliasRegistry} for use during the tests.
     */
    @Before
    public void setup()
    {
        this.alias = new CommonAliasRegistry();
    }

    /**
     * A basic test
     */
    @Test
    public void test()
    {
        this.alias.register("abc", "absd");

        String init = "test abc def";

        assertEquals("test absd def", this.alias.expand(init));

        this.alias.clear();
    }

    /**
     * A test for shorter aliases having preference.
     */
    @Test
    public void testLength()
    {
        this.alias.register("abc def", "rawr");
        this.alias.register("abc", "absd");

        String init = "test abc def";

        assertEquals("test absd def", this.alias.expand(init));

        this.alias.clear();
    }

    /**
     * A test for aliases containing multiple words.
     */
    @Test
    public void testMulti()
    {
        this.alias.register("abc def", "rawr");

        String init = "test abc def";

        assertEquals("test rawr", this.alias.expand(init));

        this.alias.clear();
    }

    /**
     * A test that aliases cannot be infinitely repeated (if they contain themselves).
     */
    @Test
    public void testInfiniteProtection()
    {
        this.alias.register("abc", "abc def");

        String init = "test abc";

        assertEquals("test abc def", this.alias.expand(init));

        this.alias.clear();
    }

    /**
     * A test that aliases cannot be infinitely repeated (if they contain themselves), but can be applied multiple times to different points.
     */
    @Test
    public void testInfiniteProtection2()
    {
        this.alias.register("abc", "abc def");

        String init = "test abc abc";

        assertEquals("test abc def abc def", this.alias.expand(init));

        this.alias.clear();
    }

    /**
     * A test for a child inheriting a parents aliases
     */
    @Test
    public void testParentage()
    {
        AliasRegistry child = new CommonAliasRegistry(this.alias);

        this.alias.register("abc", "absd");

        String init = "test abc def";

        assertEquals("test absd def", child.expand(init));

        this.alias.clear();
    }

    /**
     * A test for a child's aliases overriding those of a parent
     */
    @Test
    public void testParentage2()
    {
        AliasRegistry child = new CommonAliasRegistry(this.alias);

        this.alias.register("abc", "rawr");
        child.register("abc", "absd");

        String init = "test abc def";

        assertEquals("test absd def", child.expand(init));

        this.alias.clear();
    }

    /**
     * Tests that aliases found inside arguments are not expanded
     */
    @Test
    public void testNonExpansionOfArgs()
    {
        this.alias.register("abc", "abc def");

        String init = "hello { abc }";

        assertEquals("hello {abc}", this.alias.expand(init));
    }

    /**
     * Tests that aliases directly next to args are expanded.
     */
    @Test
    public void testIgnoringArgs()
    {

        this.alias.register("hello", "hello {world}");

        String init = "hello{abc}";

        assertEquals("hello {world} {abc}", this.alias.expand(init));
    }

    /**
     * Tests the normalize method
     */
    @Test
    public void testNormalize()
    {
        assertEquals("{hello}", normalize("{ hello }"));
        assertEquals("{hello}", normalize("{hello }"));
        assertEquals("{hello}", normalize("{ hello}"));
        assertEquals("{hello}", normalize("{hello}"));
        assertEquals("{hello,world}", normalize("{hello world}"));
        assertEquals("{hel,lo,world}", normalize("{hel lo} {world}"));
    }

    /**
     * Tests that expansion without any aliases has no effect (other than normalization).
     */
    @Test
    public void testNonAction()
    {
        this.alias.clear();

        assertEquals("hello {world}", this.alias.expand("hello {world}"));
        assertEquals("hello {world}", this.alias.expand("hello{world}"));
        assertEquals("hello {world} ape", this.alias.expand("hello {world} ape"));
        assertEquals("hello {world} ape orange banana", this.alias.expand("hello {world} ape orange banana"));
        assertEquals("hello {wor,ld} ape", this.alias.expand("hello {wor ld} ape"));
        assertEquals("hello {world} ape or$$$$ange {tst,tsj,;;2*$29f,fsff} banana*@(#%",
                this.alias.expand("hello {world} ape or$$$$ange {tst tsj ;;2*$29f fsff} banana*@(#%"));
        assertEquals("hello {wor,ld,other} ape", this.alias.expand("hello {wor ld}{other} ape"));
        assertEquals("hello {wor,ld,other} ape", this.alias.expand("hello {wor ld} {other} ape"));
        assertEquals("hello {wor,ld}", this.alias.expand("{ignored} hello {wor ld}"));
        assertEquals("", this.alias.expand("invalid {wor ld"));
        assertEquals("", this.alias.expand("invalid {wor ld {embedded} oh dear}"));
    }

    private String normalize(String s)
    {
        Pattern p = Pattern.compile("(\\{[^\\}]*\\})[\\s]*");
        Matcher match = p.matcher(s);
        String f = "";
        while (match.find())
        {
            String m = match.group(1);
            m = m.trim().replace(" ", ",");
            m = m.replace("{,", "{");
            m = m.replace(",}", "}");
            while (m.contains(",,"))
            {
                m = m.replace(",,", ",");
            }
            f += m + " ";
        }
        f = f.replaceAll("\\}[\\s]*\\{", ",");
        f = f.trim();
        return f;
    }

}
