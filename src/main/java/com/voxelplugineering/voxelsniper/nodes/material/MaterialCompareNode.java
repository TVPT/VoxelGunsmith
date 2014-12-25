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
package com.voxelplugineering.voxelsniper.nodes.material;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.ExecutableNode;
import com.voxelplugineering.voxelsniper.util.vsl.GunsmithTypes;

/**
 * A node for comparing two materials and executing another {@link ExecutableNode} if the comparison is true.
 */
public class MaterialCompareNode extends ExecutableNode implements Opcodes
{

    private static final long serialVersionUID = -8174504428098635690L;
    private ExecutableNode body = null;

    /**
     * Creates a new {@link MaterialCompareNode}.
     */
    public MaterialCompareNode()
    {
        super("MaterialCompare", "material");
        addInput("a", GunsmithTypes.MATERIAL, true, null);
        addInput("b", GunsmithTypes.MATERIAL, true, null);
    }

    /**
     * Sets the body of the if-statement formed by this comparison.
     * 
     * @param body the node to execute if the comparison is true
     */
    public void setBody(ExecutableNode body)
    {
        this.body = body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        if (this.body == null)
        {
            return localsIndex;
        }
        int a = getInput("a").getSource().get();
        int b = getInput("b").getSource().get();
        mv.visitVarInsn(ALOAD, a);
        mv.visitVarInsn(ALOAD, b);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false);
        Label l = new Label();
        mv.visitJumpInsn(IFEQ, l);
        ExecutableNode current = this.body;
        while (current != null)
        {
            localsIndex = current.insert(mv, localsIndex);
            current = current.getNextNode();
        }
        mv.visitLabel(l);
        return localsIndex;
    }

}
