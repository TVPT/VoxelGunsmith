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

/**
 * Offsets a location by a given vector offset. Equivalent to {@code location.add(vector.getX(), vector.getY(), vector.getZ());}
 */
public class LocationOffsetNode extends Node implements Opcodes
{

    /**
     * 
     */
    private static final long serialVersionUID = -5841162432155578520L;

    /**
     * Create a new node.
     */
    public LocationOffsetNode()
    {
        super("Location offset", "vector");
        addInput("location", Type.getType("COMMONLOCATION", TypeDepth.SINGLE).get(), true, null);
        addInput("offset", Type.getType("COMMONVECTOR", TypeDepth.SINGLE).get(), true, null);
        addOutput("result", Type.getType("COMMONLOCATION", TypeDepth.SINGLE).get(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        int location = getInput("location").getSource().get();
        int offset = getInput("offset").getSource().get();

        mv.visitVarInsn(ALOAD, location);
        mv.visitVarInsn(ALOAD, offset);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonVector", "getX", "()D", false);
        mv.visitVarInsn(ALOAD, offset);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonVector", "getY", "()D", false);
        mv.visitVarInsn(ALOAD, offset);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonVector", "getZ", "()D", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/common/CommonLocation", "add",
                "(DDD)Lcom/voxelplugineering/voxelsniper/common/CommonLocation;", false);
        mv.visitVarInsn(ASTORE, localsIndex);
        setOutput("result", localsIndex);
        return localsIndex + 1;
    }
}
