package com.voxelplugineering.voxelsniper.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.thevoxelbox.vsl.classloader.ASMClassLoader;
import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.NodeGraph;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.api.IBrushLoader;
import com.voxelplugineering.voxelsniper.api.ISniper;

public class CommonBrushLoader implements IBrushLoader
{

    public Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, byte[] serialized)
    {
        ByteArrayInputStream stream = new ByteArrayInputStream(serialized);
        try
        {
            ObjectInputStream ois = new ObjectInputStream(stream);
            NodeGraph brush = (NodeGraph) ois.readObject();
            Class<? extends IBrush> compiled = (Class<? extends IBrush>) brush.compile(classLoader);
            return compiled;
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            return null;
        } catch (GraphCompilationException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, String identifier)
    {
        throw new UnsupportedOperationException("Brush loading by identifier only is not supported by the base BrushLoader");
    }

}
