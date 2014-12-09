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

import static com.google.common.base.Preconditions.checkNotNull;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.ExecutableNode;
import com.thevoxelbox.vsl.type.Type;
import com.thevoxelbox.vsl.type.TypeDepth;

/**
 * Iterates over each block of a shape and executes the body for-each block.
 */
public class ShapeForEachNode extends ExecutableNode implements Opcodes
{

    /**
     * 
     */
    private static final long serialVersionUID = -8693243523600819364L;
    /**
     * The body of the for loop.
     */
    ExecutableNode body = null;

    /**
     * Create a new node.
     */
    public ShapeForEachNode()
    {
        super("Shape for-each", "shape");
        addInput("shape", Type.getType("SHAPE", TypeDepth.SINGLE).get(), true, null);
        addOutput("next", Type.getType("COMMONVECTOR", TypeDepth.SINGLE).get(), this);
        addOutput("index", Type.INTEGER, this);
    }

    /**
     * Sets the {@link ExecutableNode} to insert into the body of this for-each loop.
     * 
     * @param body the new body
     */
    public void setBody(ExecutableNode body)
    {
        checkNotNull(body, "Loop body cannot be null");
        this.body = body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        if (body == null)
        {
            return localsIndex;
        }

        int shape = getInput("shape").getSource().get();
        int array = localsIndex++;
        int i = localsIndex++;
        int target = localsIndex++;
        int next = localsIndex++;
        mv.visitVarInsn(ALOAD, shape);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/shape/Shape", "getShape",
                "()[Lcom/voxelplugineering/voxelsniper/common/CommonVector;", false);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ASTORE, array);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitVarInsn(ISTORE, target);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, i);
        Label l2 = new Label();
        mv.visitJumpInsn(GOTO, l2);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitVarInsn(ALOAD, array);
        mv.visitVarInsn(ILOAD, i);
        mv.visitInsn(AALOAD);
        mv.visitVarInsn(ASTORE, next);
        setOutput("next", next);
        setOutput("index", i);
        ExecutableNode current = this.body;
        while (current != null)
        {
            localsIndex = current.insert(mv, localsIndex);
            current = current.getNextNode();
        }
        mv.visitIincInsn(i, 1);
        mv.visitLabel(l2);
        mv.visitVarInsn(ILOAD, i);
        mv.visitVarInsn(ILOAD, target);
        mv.visitJumpInsn(IF_ICMPLT, l3);
        return localsIndex;
    }
}
