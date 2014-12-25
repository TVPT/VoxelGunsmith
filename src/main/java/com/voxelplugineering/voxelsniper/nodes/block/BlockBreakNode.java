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
package com.voxelplugineering.voxelsniper.nodes.block;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.Node;
import com.voxelplugineering.voxelsniper.util.vsl.GunsmithTypes;

/**
 * Node to get location and material from block
 */
public class BlockBreakNode extends Node
{

    /**
     * 
     */
    private static final long serialVersionUID = -3407486022020201155L;

    /**
     * Creates a new node.
     */
    public BlockBreakNode()
    {
        super("BreakBlock", "block");
        addInput("block", GunsmithTypes.BLOCK, true, null);
        addOutput("location", GunsmithTypes.LOCATION, this);
        addOutput("material", GunsmithTypes.MATERIAL, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        /*
         * ALOAD block
         * INVOKEVIRTUAL CommonBlock.getLocation () : CommonLocation
         * ASTORE location
         * ALOAD block
         * INVOKEVIRTUAL CommonBlock.getMaterial () : CommonMaterial
         * ASTORE material
         */
        int block = getInput("block").getSource().get();
        mv.visitVarInsn(Opcodes.ALOAD, block);
        mv.visitTypeInsn(Opcodes.CHECKCAST, GunsmithTypes.BLOCK.getInternalName());
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, GunsmithTypes.BLOCK.getInternalName(), "getLocation",
                "()L" + GunsmithTypes.LOCATION.getInternalName() + ";", false);
        mv.visitVarInsn(Opcodes.ASTORE, localsIndex);
        setOutput("location", localsIndex);
        mv.visitVarInsn(Opcodes.ALOAD, block);
        mv.visitTypeInsn(Opcodes.CHECKCAST, GunsmithTypes.BLOCK.getInternalName());
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, GunsmithTypes.BLOCK.getInternalName(), "getMaterial",
                "()L" + GunsmithTypes.MATERIAL.getInternalName() + ";", false);
        mv.visitVarInsn(Opcodes.ASTORE, localsIndex + 1);
        setOutput("material", localsIndex + 1);
        return localsIndex + 2;
    }
}
