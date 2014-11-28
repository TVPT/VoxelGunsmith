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

/**
 * A visual scripting node to flush the player's active change queue down to the world.
 */
public class FlushPlayerQueueNode extends ExecutableNode
{

    /**
     * 
     */
    private static final long serialVersionUID = 7186240432579248565L;

    /**
     * Constructs a new FlushPlayerQueueNode
     */
    public FlushPlayerQueueNode()
    {
        super("Flush Queue", "world");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        /*
            ALOAD 2: sniper
            INVOKEINTERFACE ISniper.getPersonalQueue () : BlockChangeQueue
            INVOKEVIRTUAL BlockChangeQueue.flush () : void
         */
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "com/voxelplugineering/voxelsniper/api/ISniper", "getActiveQueue",
                "()Lcom/voxelplugineering/voxelsniper/world/BlockChangeQueue;", true);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/voxelplugineering/voxelsniper/world/BlockChangeQueue", "flush", "()V", false);
        return localsIndex;
    }
}
