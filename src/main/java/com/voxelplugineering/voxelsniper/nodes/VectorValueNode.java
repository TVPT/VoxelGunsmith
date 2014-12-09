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
 * Inserts a {@link CommonVector} value.
 */
public class VectorValueNode extends Node implements Opcodes
{

    /**
     * 
     */
    private static final long serialVersionUID = -9020243582936473792L;
    /**
     * The value to insert.
     */
    CommonVector value;

    /**
     * Creates a new node.
     * 
     * @param v the value to insert
     */
    public VectorValueNode(CommonVector v)
    {
        super("Vector Value", "vector");
        addOutput("value", Type.getType("COMMONVECTOR", TypeDepth.SINGLE).get(), this);
        this.value = v;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        /*
         
           NEW com/voxelplugineering/voxelsniper/common/CommonVector
           DUP
           LDC x
           LDC y
           LDC z
           INVOKESPECIAL com/voxelplugineering/voxelsniper/common/CommonVector : <init> (DDD)V
           ASTORE value
         
         */

        mv.visitTypeInsn(NEW, "com/voxelplugineering/voxelsniper/common/CommonVector");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(value.getX());
        mv.visitLdcInsn(value.getY());
        mv.visitLdcInsn(value.getZ());
        mv.visitMethodInsn(INVOKESPECIAL, "com/voxelplugineering/voxelsniper/common/CommonVector", "<init>", "(DDD)V", false);
        mv.visitVarInsn(ASTORE, localsIndex);
        setOutput("value", localsIndex);
        return localsIndex + 1;
    }
}
