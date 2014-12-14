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
import com.thevoxelbox.vsl.node.ExecutableNode;
import com.thevoxelbox.vsl.type.Type;
import com.thevoxelbox.vsl.type.TypeDepth;

/**
 * A visual scripting node to set the a shape to a material.
 */
public class ShapeMaterialSetNode extends ExecutableNode implements Opcodes
{
    private static final long serialVersionUID = 7186240432579248565L;

    /**
     * Constructs a new ShapeMaterialSetNode
     */
    public ShapeMaterialSetNode()
    {
        super("MaterialSet", "world");
        addInput("material", Type.getType("COMMONMATERIAL", TypeDepth.SINGLE).get(), true, null);
        addInput("target", Type.getType("COMMONLOCATION", TypeDepth.SINGLE).get(), true, null);
        addInput("shape", Type.getType("SHAPE", TypeDepth.SINGLE).get(), true, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        int target = getInput("target").getSource().get();
        int shape = getInput("shape").getSource().get();
        int material = getInput("material").getSource().get();
        mv.visitTypeInsn(NEW, "com/voxelplugineering/voxelsniper/world/ShapeChangeQueue");
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, target);
        mv.visitTypeInsn(NEW, "com/voxelplugineering/voxelsniper/shape/MaterialShape");
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, shape);
        mv.visitVarInsn(ALOAD, material);
        mv.visitMethodInsn(INVOKESPECIAL, "com/voxelplugineering/voxelsniper/shape/MaterialShape", "<init>",
                "(Lcom/voxelplugineering/voxelsniper/shape/Shape;Lcom/voxelplugineering/voxelsniper/common/CommonMaterial;)V", false);
        mv.visitMethodInsn(INVOKESPECIAL, "com/voxelplugineering/voxelsniper/world/ShapeChangeQueue", "<init>",
                "(Lcom/voxelplugineering/voxelsniper/api/ISniper;Lcom/voxelplugineering/voxelsniper/common/CommonLocation;"
                        + "Lcom/voxelplugineering/voxelsniper/shape/MaterialShape;)V", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/world/ShapeChangeQueue", "flush", "()V", false);
        return localsIndex;
    }

    /*    public void run(IVariableScope vars, ISniper sniper)
        {
            CommonLocation target = null;
            Shape shape = null;
            CommonMaterial<?> material = null;
            new ShapeChangeQueue(sniper, target, new MaterialShape(shape, material)).flush();
        }*/
}
