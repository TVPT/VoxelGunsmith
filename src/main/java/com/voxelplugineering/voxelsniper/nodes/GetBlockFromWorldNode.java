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
import com.voxelplugineering.voxelsniper.common.CommonBlock;
import com.voxelplugineering.voxelsniper.common.CommonVector;
import com.voxelplugineering.voxelsniper.common.CommonWorld;

/**
 * Takes a world and a vector position and returns the block at that location. Equivalent to
 * {@code world.getBlockAt(vector.getX(), vector.getY(), vector.getZ());}
 */
public class GetBlockFromWorldNode extends Node implements Opcodes
{

    /**
     * 
     */
    private static final long serialVersionUID = -5847496393651127838L;

    /**
     * Creates a new node.
     */
    public GetBlockFromWorldNode()
    {
        super("Block Get From World", "world");
        addInput("world", CommonWorld.COMMONVECTOR_TYPE, true, null);
        addInput("vector", CommonVector.COMMONVECTOR_TYPE, true, null);
        addOutput("block", CommonBlock.COMMONBLOCK_TYPE, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        int world = getInput("world").getSource().get();
        int vector = getInput("vector").getSource().get();

        mv.visitVarInsn(ALOAD, world);
        mv.visitVarInsn(ALOAD, vector);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonVector", "getX", "()D", false);
        mv.visitInsn(D2I);
        mv.visitVarInsn(ALOAD, vector);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonVector", "getY", "()D", false);
        mv.visitInsn(D2I);
        mv.visitVarInsn(ALOAD, vector);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonVector", "getZ", "()D", false);
        mv.visitInsn(D2I);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonWorld", "getBlockAt",
                "(III)Lcom/voxelplugineering/voxelsniper/common/CommonBlock;", false);
        mv.visitVarInsn(ASTORE, localsIndex);
        setOutput("block", localsIndex);
        return localsIndex + 1;
    }
}
