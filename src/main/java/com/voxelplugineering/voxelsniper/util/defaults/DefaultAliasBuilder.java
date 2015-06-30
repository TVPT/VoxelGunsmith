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
package com.voxelplugineering.voxelsniper.util.defaults;

import com.voxelplugineering.voxelsniper.service.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.service.alias.AliasRegistry;

/**
 * A utility for loading default aliases.
 */
public class DefaultAliasBuilder
{

    /**
     * Loads the standard default aliases into the given {@link AliasHandler}.
     * 
     * @param registry The handler
     */
    public static void loadDefaultAliases(AliasHandler registry)
    {

        { // brushes

            if (!registry.hasTarget("brush"))
            {
                registry.registerTarget("brush");
            }
            AliasRegistry alias = registry.getRegistry("brush").get();

            alias.register("b", "ball");
            alias.register("d", "disc");
            alias.register("m", "material");
            alias.register("mm", "materialmask material");
            alias.register("s", "snipe");
            alias.register("v", "voxel");
            alias.register("vd", "voxeldisc");
            alias.register("over", "overlay");
            alias.register("e melt", "ball melt");
            alias.register("e fill", "ball fill");
            alias.register("sp", "splatter");
            alias.register("sb", "ball splatter");
            alias.register("sd", "disc splatter");
            alias.register("sover", "splatter overlay");

        }

        { // materials

            if (!registry.hasTarget("material"))
            {
                registry.registerTarget("material");
            }
            AliasRegistry alias = registry.getRegistry("material").get();

            alias.register("0", "air");
            alias.register("1", "stone");
            alias.register("2", "grass");
            alias.register("3", "dirt");
            alias.register("4", "cobblestone");

        }
    }

}
