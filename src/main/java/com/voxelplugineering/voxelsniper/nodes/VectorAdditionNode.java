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
package com.voxelplugineering.voxelsniper.nodes;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.Node;
import com.thevoxelbox.vsl.type.Type;
import com.thevoxelbox.vsl.type.TypeDepth;
import com.voxelplugineering.voxelsniper.common.CommonVector;

/**
 * Adds two {@link CommonVector}s together and returns the result.
 */
public class VectorAdditionNode extends Node implements Opcodes
{

    /**
     * 
     */
    private static final long serialVersionUID = 8532132984520786760L;

    /**
     * Create a new node.
     */
    public VectorAdditionNode()
    {
        super("Vector Addition", "vector");
        addInput("a", Type.getType("COMMONVECTOR", TypeDepth.SINGLE).get(), true, null);
        addInput("b", Type.getType("COMMONVECTOR", TypeDepth.SINGLE).get(), true, null);
        addOutput("result", Type.getType("COMMONVECTOR", TypeDepth.SINGLE).get(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        int a = getInput("a").getSource().get();
        int b = getInput("b").getSource().get();

        mv.visitVarInsn(ALOAD, a);
        mv.visitVarInsn(ALOAD, b);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonVector", "add",
                "(Lcom/voxelplugineering/voxelsniper/common/CommonVector;)Lcom/voxelplugineering/voxelsniper/common/CommonVector;", false);
        mv.visitVarInsn(ASTORE, localsIndex);
        setOutput("result", localsIndex);
        return localsIndex + 1;
    }
}
