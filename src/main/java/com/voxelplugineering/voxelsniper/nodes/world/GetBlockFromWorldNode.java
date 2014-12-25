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
package com.voxelplugineering.voxelsniper.nodes.world;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.Node;
import com.voxelplugineering.voxelsniper.util.vsl.GunsmithTypes;

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
        addInput("world", GunsmithTypes.WORLD, true, null);
        addInput("vector", GunsmithTypes.VECTOR3I, true, null);
        addOutput("block", GunsmithTypes.BLOCK, this);
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
        mv.visitMethodInsn(INVOKEVIRTUAL, GunsmithTypes.VECTOR3I.getInternalName(), "getX", "()D", false);
        mv.visitInsn(D2I);
        mv.visitVarInsn(ALOAD, vector);
        mv.visitMethodInsn(INVOKEVIRTUAL, GunsmithTypes.VECTOR3I.getInternalName(), "getY", "()D", false);
        mv.visitInsn(D2I);
        mv.visitVarInsn(ALOAD, vector);
        mv.visitMethodInsn(INVOKEVIRTUAL, GunsmithTypes.VECTOR3I.getInternalName(), "getZ", "()D", false);
        mv.visitInsn(D2I);
        mv.visitMethodInsn(INVOKEINTERFACE, GunsmithTypes.WORLD.getInternalName(), "getBlockAt",
                "(III)L" + GunsmithTypes.BLOCK.getInternalName() + ";", false);
        mv.visitVarInsn(ASTORE, localsIndex);
        setOutput("block", localsIndex);
        return localsIndex + 1;
    }
}
