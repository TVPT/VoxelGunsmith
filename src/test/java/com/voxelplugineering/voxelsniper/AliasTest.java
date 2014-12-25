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

import com.voxelplugineering.voxelsniper.alias.AliasRegistry;

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
        this.alias = new AliasRegistry();
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
        AliasRegistry child = new AliasRegistry(this.alias);

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
        AliasRegistry child = new AliasRegistry(this.alias);

        this.alias.register("abc", "rawr");
        child.register("abc", "absd");

        String init = "test abc def";

        assertEquals("test absd def", child.expand(init));

        this.alias.clear();
    }

}
