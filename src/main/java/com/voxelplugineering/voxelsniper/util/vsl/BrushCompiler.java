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
package com.voxelplugineering.voxelsniper.util.vsl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.CheckClassAdapter;

import com.thevoxelbox.vsl.api.IChainableNodeGraph;
import com.thevoxelbox.vsl.api.IGraphCompiler;
import com.thevoxelbox.vsl.api.INodeGraph;
import com.thevoxelbox.vsl.api.IRunnableGraph;
import com.thevoxelbox.vsl.classloader.ASMClassLoader;
import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.ExecutableNode;
import com.voxelplugineering.voxelsniper.api.brushes.Brush;

/**
 * A compiler for compiling {@link com.thevoxelbox.vsl.api.INodeGraph}s into {@link Brush}s.
 * <p>
 * TODO replace internal names with call to GunsmithTypes
 */
public class BrushCompiler implements IGraphCompiler, Opcodes
{

    /**
     * Compiles the given {@link com.thevoxelbox.vsl.api.INodeGraph} into an {@link Brush}. The Class is defined using the given class loader.
     * 
     * @param cl the classloader to define the class with
     * @param graph the {@link com.thevoxelbox.vsl.api.INodeGraph} to compile
     * @return The new class
     * @throws NullPointerException if the graph's starting node is null
     * @throws GraphCompilationException if there is an error within the graph
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends IRunnableGraph> compile(ASMClassLoader cl, INodeGraph graph) throws NullPointerException, GraphCompilationException
    {
        checkNotNull(cl, "Classloader cannot be null");
        checkNotNull(graph, "Node graph cannot be null");
        checkNotNull(graph.getStart(), "Graph start cannot be null");
        if (!(graph instanceof IChainableNodeGraph))
        {
            throw new GraphCompilationException("Graph type is incorrect type for compiler!");
        }
        BrushPartNodeGraph cgraph = (BrushPartNodeGraph) graph;
        while (cl.isClassLoaded("com.thevoxelbox.custom." + cgraph.getName() + cgraph.getIncrement()))
        {
            cgraph.incrementName();
        }
        byte[] cls = createClass(cgraph);
        try
        {
            FileOutputStream out = new FileOutputStream(new File(cgraph.getName() + ".class"));
            out.write(cls);
            out.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return (Class<? extends IRunnableGraph>) cl.defineClass("com.thevoxelbox.custom." + cgraph.getName() + cgraph.getIncrement(), cls);
    }

    /**
     * Creates a new class from the given {@link com.thevoxelbox.vsl.api.INodeGraph}.
     * 
     * @param graph the graph to create the class from
     * @return the class as a byte array
     * @throws GraphCompilationException if there is an error within the graph
     */
    public byte[] createClass(BrushPartNodeGraph graph) throws GraphCompilationException
    {
        checkNotNull(graph, "Node graph cannot be null");
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv;
        FieldVisitor fv;

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "com/thevoxelbox/custom/" + graph.getName() + graph.getIncrement(), null, "java/lang/Object",
                new String[] { "com/voxelplugineering/voxelsniper/api/brushes/Brush" });

        cw.visitSource(graph.getName() + graph.getIncrement() + ".java", null);

        {
            fv = cw.visitField(ACC_PRIVATE, "next", "Lcom/thevoxelbox/vsl/api/IChainedRunnableGraph;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ACONST_NULL);
            mv.visitFieldInsn(PUTFIELD, "com/thevoxelbox/custom/" + graph.getName() + graph.getIncrement(), "next",
                    "Lcom/thevoxelbox/vsl/api/IChainedRunnableGraph;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getName", "()Ljava/lang/String;", null, null);
            mv.visitCode();
            mv.visitLdcInsn(graph.getName());
            mv.visitInsn(ARETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getRequiredVars", "()[Ljava/lang/String;", null, null);
            mv.visitCode();
            mv.visitIntInsn(BIPUSH, graph.getRequiredVars().length);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
            byte i = 0;
            for (String var : graph.getRequiredVars())
            {
                mv.visitInsn(DUP);
                mv.visitIntInsn(BIPUSH, i++);
                mv.visitLdcInsn(var);
                mv.visitInsn(AASTORE);
            }
            mv.visitInsn(ARETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC, "chain", "(Lcom/thevoxelbox/vsl/api/IChainedRunnableGraph;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, "com/thevoxelbox/custom/" + graph.getName() + graph.getIncrement(), "next",
                    "Lcom/thevoxelbox/vsl/api/IChainedRunnableGraph;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        { //create an empty `run(IVariableHolder vars)` method to satisfy the interface from VisualScripting
            mv = cw.visitMethod(ACC_PUBLIC, "run", "(Lcom/thevoxelbox/vsl/api/IVariableHolder;)V", null, null);
            mv.visitCode();
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "run",
                            "(Lcom/thevoxelbox/vsl/api/IVariableHolder;Lcom/voxelplugineering/voxelsniper/api/entity/living/Player;)V", null, null);
            mv.visitCode();

            int index = 3;
            ExecutableNode current = graph.getStart();
            while (current != null)
            {
                index = current.insert(mv, index);
                current = current.getNextNode();
            }
            /* Insert call to next graph in chain:
             * ===================================
             * 
                ALOAD 0: this
                GETFIELD this.next : IChainedRunnableGraph
                IFNULL L3
                ALOAD 0: this
                GETFIELD this.next : IChainedRunnableGraph
                ALOAD 1: vars
                INVOKEINTERFACE IChainedRunnableGraph.run (IVariableHolder) : void
               L3
             */

            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "com/thevoxelbox/custom/" + graph.getName() + graph.getIncrement(), "next",
                    "Lcom/thevoxelbox/vsl/api/IChainedRunnableGraph;");
            Label l3 = new Label();
            mv.visitJumpInsn(IFNULL, l3);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "com/thevoxelbox/custom/" + graph.getName() + graph.getIncrement(), "next",
                    "Lcom/thevoxelbox/vsl/api/IChainedRunnableGraph;");
            mv.visitTypeInsn(CHECKCAST, "com/voxelplugineering/voxelsniper/api/brushes/Brush");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEINTERFACE, "com/voxelplugineering/voxelsniper/api/brushes/Brush", "run",
                    "(Lcom/thevoxelbox/vsl/api/IVariableHolder;Lcom/voxelplugineering/voxelsniper/api/entity/living/Player;)V", true);
            mv.visitLabel(l3);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        cw.visitEnd();
        //CheckClassAdapter.verify(new ClassReader(cw.toByteArray()), true, new PrintWriter(System.out));
        return cw.toByteArray();
    }

}
