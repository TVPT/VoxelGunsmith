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
import com.thevoxelbox.vsl.node.ExecutableNode;
import com.thevoxelbox.vsl.type.Type;
import com.voxelplugineering.voxelsniper.util.vsl.GunsmithTypes;

/**
 * A node for setting the biome of a world at a location.
 */
public class SetBiomeNode extends ExecutableNode implements Opcodes
{
    private static final long serialVersionUID = 4934446335692436694L;

    /**
     * Creates a new {@link SetBiomeNode}.
     */
    public SetBiomeNode()
    {
        super("Set biome", "world");
        addInput("location", GunsmithTypes.LOCATION, true, null);
        addInput("biome", Type.STRING, true, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        int location = getInput("location").getSource().get();
        int biome = getInput("biome").getSource().get();
        mv.visitVarInsn(ALOAD, location);
        mv.visitMethodInsn(INVOKEINTERFACE, GunsmithTypes.LOCATION.getInternalName(), "getWorld", "()L" + GunsmithTypes.WORLD.getInternalName() + ";",
                false);
        mv.visitVarInsn(ALOAD, location);
        mv.visitMethodInsn(INVOKEINTERFACE, GunsmithTypes.LOCATION.getInternalName(), "getFlooredX", "()I", false);
        mv.visitVarInsn(ALOAD, location);
        mv.visitMethodInsn(INVOKEINTERFACE, GunsmithTypes.LOCATION.getInternalName(), "getFlooredZ", "()I", false);
        mv.visitVarInsn(ALOAD, biome);
        mv.visitMethodInsn(INVOKEINTERFACE, GunsmithTypes.WORLD.getInternalName(), "setBiomeAt", "(IILjava/lang/String;)V", false);
        return localsIndex;
    }

}
