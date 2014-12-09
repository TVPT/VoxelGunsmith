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
 * A visual scripting node to set the voxel at a location to the material.
 */
public class MaterialSetNode extends ExecutableNode
{

    /**
     * 
     */
    private static final long serialVersionUID = 7186240432579248565L;

    /**
     * Constructs a new MaterialSetNode
     */
    public MaterialSetNode()
    {
        super("MaterialSet", "world");
        addInput("material", Type.getType("COMMONMATERIAL", TypeDepth.SINGLE).get(), true, null);
        addInput("targetBlock", Type.getType("COMMONBLOCK", TypeDepth.SINGLE).get(), true, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        /*
            ALOAD sniper
            INVOKEINTERFACE com/voxelplugineering/voxelsniper/api/ISniper.getPersonalQueue 
                    ()Lcom/voxelplugineering/voxelsniper/world/BlockChangeQueue;
            NEW com/voxelplugineering/voxelsniper/world/BlockChange
            DUP
            ALOAD target
            ALOAD material
            INVOKESPECIAL com/voxelplugineering/voxelsniper/world/BlockChange.<init> (Lcom/voxelplugineering/voxelsniper/common/CommonBlock;
                    Lcom/voxelplugineering/voxelsniper/common/CommonMaterial;)V
            INVOKEVIRTUAL com/voxelplugineering/voxelsniper/world/BlockChangeQueue.add (Lcom/voxelplugineering/voxelsniper/world/BlockChange;)V
         */
        int targetBlock = getInput("targetBlock").getSource().get();
        int newMaterial = getInput("material").getSource().get();
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "com/voxelplugineering/voxelsniper/api/ISniper", "getActiveQueue",
                "()Lcom/voxelplugineering/voxelsniper/world/BlockChangeQueue;", true);
        mv.visitTypeInsn(Opcodes.NEW, "com/voxelplugineering/voxelsniper/world/BlockChange");
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ALOAD, targetBlock);
        mv.visitVarInsn(Opcodes.ALOAD, newMaterial);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/voxelplugineering/voxelsniper/world/BlockChange", "<init>",
                "(Lcom/voxelplugineering/voxelsniper/common/CommonBlock;Lcom/voxelplugineering/voxelsniper/common/CommonMaterial;)V", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/world/BlockChangeQueue", "add",
                "(Lcom/voxelplugineering/voxelsniper/world/BlockChange;)V", false);
        return localsIndex;
    }
}
