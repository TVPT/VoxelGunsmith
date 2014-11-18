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

import com.thevoxelbox.vsl.api.IGraphCompiler;
import com.thevoxelbox.vsl.api.INodeGraph;
import com.thevoxelbox.vsl.classloader.ASMClassLoader;
import com.thevoxelbox.vsl.node.NodeGraph;
import com.thevoxelbox.vsl.node.variables.VariableGetNode;
import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.nodes.MaterialSetNode;

/**
 * In lieu of having flat file brushes this will temporarily serve as a builder for brushes at runtime for debugging during development.
 */
public class TemporaryBrushBuilder
{

    /**
     * Creates and loads all brushes into the blobal brush manager.
     */
    public static void buildBrushes()
    {
        ASMClassLoader classloader = Gunsmith.getGlobalBrushManager().getClassLoader();

        try
        { //Snipe brush
            VariableGetNode getTarget = new VariableGetNode("targetBlock");
            VariableGetNode getMaterial = new VariableGetNode("setMaterial");
            MaterialSetNode set = new MaterialSetNode();
            set.mapInput("targetBlock", getTarget.getOutput("value"));
            set.mapInput("material", getMaterial.getOutput("value"));

            INodeGraph brush = new NodeGraph("snipe");
            brush.setStartNode(set);
            IGraphCompiler compiler = new BrushCompiler();
            @SuppressWarnings("unchecked")
            Class<? extends IBrush> compiled = (Class<? extends IBrush>) compiler.compile(classloader, brush);
            Gunsmith.getGlobalBrushManager().loadBrush("snipe", compiled);
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Failed to create Sniper brush");
        }
    }

}
