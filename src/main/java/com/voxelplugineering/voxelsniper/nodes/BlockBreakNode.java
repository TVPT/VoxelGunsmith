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
import com.voxelplugineering.voxelsniper.common.CommonLocation;
import com.voxelplugineering.voxelsniper.common.CommonMaterial;

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
        super("BlockLocation", "block");
        addInput("block", CommonBlock.COMMONBLOCK_TYPE, true, null);
        addOutput("location", CommonLocation.COMMONLOCATION_TYPE, this);
        addOutput("material", CommonMaterial.COMMONMATERIAL_TYPE, this);
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
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonBlock", "getLocation",
                "()Lcom/voxelplugineering/voxelsniper/common/CommonLocation;", false);
        mv.visitVarInsn(Opcodes.ASTORE, localsIndex);
        setOutput("location", localsIndex);
        mv.visitVarInsn(Opcodes.ALOAD, block);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonBlock", "getMaterial",
                "()Lcom/voxelplugineering/voxelsniper/common/CommonMaterial;", false);
        mv.visitVarInsn(Opcodes.ASTORE, localsIndex + 1);
        setOutput("material", localsIndex + 1);
        return localsIndex + 2;
    }

}
