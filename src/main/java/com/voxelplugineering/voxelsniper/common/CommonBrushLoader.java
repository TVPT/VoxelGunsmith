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
package com.voxelplugineering.voxelsniper.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.thevoxelbox.vsl.classloader.ASMClassLoader;
import com.thevoxelbox.vsl.error.GraphCompilationException;
import com.thevoxelbox.vsl.node.NodeGraph;
import com.voxelplugineering.voxelsniper.api.IBrush;
import com.voxelplugineering.voxelsniper.api.IBrushLoader;

public class CommonBrushLoader implements IBrushLoader
{

    @SuppressWarnings("unchecked")
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
