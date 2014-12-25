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
package com.voxelplugineering.voxelsniper.nodes.shape;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.ExecutableNode;
import com.voxelplugineering.voxelsniper.util.vsl.GunsmithTypes;

/**
 * A visual scripting node to set the a shape to a material.
 */
public class ShapeSetNode extends ExecutableNode implements Opcodes
{
    private static final long serialVersionUID = 7186240432579248565L;

    /**
     * Constructs a new {@link ShapeSetNode}
     */
    public ShapeSetNode()
    {
        super("ShapeSet", "world");
        addInput("target", GunsmithTypes.VECTOR3I, true, null);
        addInput("shape", GunsmithTypes.SHAPE, true, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        int target = getInput("target").getSource().get();
        int shape = getInput("shape").getSource().get();
        mv.visitVarInsn(ALOAD, shape);
        mv.visitVarInsn(ALOAD, target);
        mv.visitMethodInsn(INVOKEVIRTUAL, GunsmithTypes.VECTOR3I.getInternalName(), "getX", "()D", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "floor", "(D)D", false);
        mv.visitInsn(D2I);
        mv.visitVarInsn(ALOAD, target);
        mv.visitMethodInsn(INVOKEVIRTUAL, GunsmithTypes.VECTOR3I.getInternalName(), "getY", "()D", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "floor", "(D)D", false);
        mv.visitInsn(D2I);
        mv.visitVarInsn(ALOAD, target);
        mv.visitMethodInsn(INVOKEVIRTUAL, GunsmithTypes.VECTOR3I.getInternalName(), "getZ", "()D", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "floor", "(D)D", false);
        mv.visitInsn(D2I);
        mv.visitInsn(ICONST_1);
        mv.visitMethodInsn(INVOKEVIRTUAL, GunsmithTypes.SHAPE.getInternalName(), "set", "(IIIZ)V", false);
        return localsIndex;
    }

}
