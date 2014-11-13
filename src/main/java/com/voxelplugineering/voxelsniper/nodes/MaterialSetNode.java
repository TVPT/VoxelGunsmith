package com.voxelplugineering.voxelsniper.nodes;

import org.objectweb.asm.MethodVisitor;

import com.thevoxelbox.vsl.IOType;
import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.ExecutableNode;

public class MaterialSetNode extends ExecutableNode
{

    public MaterialSetNode()
    {
        super("MaterialSet", "world");
        addInput("Material", IOType.WILD, true, null);
        addInput("World", IOType.WILD, true, null);
        addInput("Location", IOType.WILD, true, null);
    }

    @Override
    protected int insertLocal(MethodVisitor mv, int localsIndex) throws GraphCompilationException
    {
        //world.getBlock(location).setMaterial(material);
        return 0;
    }

}
