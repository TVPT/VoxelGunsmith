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
 * A node for retrieving a block from a location. Equivalent to {@code location.getWorld().getBlockAt(location);}
 */
public class GetBlockFromLocationNode extends Node implements Opcodes
{

    /**
     * 
     */
    private static final long serialVersionUID = -2082923297938385218L;

    /**
     * Create a new node.
     */
    public GetBlockFromLocationNode()
    {
        super("Block Get From World", "world");
        addInput("location", GunsmithTypes.LOCATION, true, null);
        addOutput("block", GunsmithTypes.BLOCK, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        int location = getInput("location").getSource().get();

        mv.visitVarInsn(ALOAD, location);
        mv.visitMethodInsn(INVOKEINTERFACE, GunsmithTypes.LOCATION.getInternalName(), "getWorld", "()L" + GunsmithTypes.WORLD.getInternalName() + ";",
                false);
        mv.visitVarInsn(ALOAD, location);
        mv.visitMethodInsn(INVOKEINTERFACE, GunsmithTypes.WORLD.getInternalName(), "getBlockAt", "(L" + GunsmithTypes.LOCATION.getInternalName()
                + ";)L" + GunsmithTypes.BLOCK.getInternalName() + ";", false);
        mv.visitVarInsn(ASTORE, localsIndex);
        setOutput("block", localsIndex);
        return localsIndex + 1;
    }
}
