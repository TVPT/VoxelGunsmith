/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 The Voxel Plugin Team
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
package com.voxelplugineering.voxelsniper.common.node.operator;

import com.voxelplugineering.voxelsniper.common.node.InputType;
import com.voxelplugineering.voxelsniper.common.node.NodeInput;
import com.voxelplugineering.voxelsniper.common.node.NodeOutput;
import com.voxelplugineering.voxelsniper.common.node.OperatorNode;

public class StringCatOperatorNode extends OperatorNode
{
    private static final long serialVersionUID = -5683562458231490061L;

    public StringCatOperatorNode()
    {
        inputs.put("a", new NodeInput(InputType.STRING, false, ""));
        inputs.put("b", new NodeInput(InputType.STRING, false, ""));
        outputs.put("cat", new NodeOutput<String>("cat", String.class));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void calculate()
    {
        String a = getInput("a").toString();
        String b = getInput("b").toString();
        ((NodeOutput<String>) outputs.get("cat")).setValue(a+b);
        dirty = false;
    }
}
