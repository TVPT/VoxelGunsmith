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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.thevoxelbox.vsl.classloader.ASMClassLoader;
import com.voxelplugineering.voxelsniper.api.IBrush;

public class FileBrushLoader extends CommonBrushLoader
{

    public static final int BRUSH_FILE_FORMAT_VERSION = 1;

    /*
     * Brush File Format Outline (Version 1)
     * 
     * 4 bytes - File format version number 4-bytes - Brush version number
     * remaining - serialized IRunnableGraph
     */

    private File defaultDirectory;

    public FileBrushLoader(File defaultDir)
    {
        this.defaultDirectory = defaultDir;
    }

    public File getDefaultDirectory()
    {
        return this.defaultDirectory;
    }

    public Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, File data)
    {
        try
        {
            DataInputStream din = new DataInputStream(new FileInputStream(data));
            int fileVersion = din.readInt();
            if (fileVersion > BRUSH_FILE_FORMAT_VERSION)
            {
                din.close();
                throw new Exception("Unsupported version: " + fileVersion);
            } else if (fileVersion < BRUSH_FILE_FORMAT_VERSION)
            {
                din.close();
                boolean success = convertToVersion(data);
                if (!success)
                {
                    throw new Exception("Failed to convert to current version from version " + fileVersion);
                }
                din = new DataInputStream(new FileInputStream(data));
                fileVersion = din.readInt();
            }
            int brushVersion = din.readInt();
            //IBrush brush = Gunsmith.getBrushManager();
            //TODO
            din.close();
            return null;
        } catch (Exception e)
        {
            System.out.println("Error loading brush " + e.getMessage());
            return null;
        }
    }

    public Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, String ident, File directory)
    {
        File data = new File(directory, ident + ".brush");
        if (!data.exists())
        {
            File ser = new File(directory, ident + ".ser");
            if (ser.exists())
            {
                boolean success = FileBrushLoader.convertVSLGraph(ser);
                if (!success)
                {
                    System.out.println("Failed to convert " + ser.getAbsolutePath());
                    return null;
                }
            }
        }
        return loadBrush(classLoader, data);
    }

    @Override
    public Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, String ident)
    {
        return loadBrush(classLoader, ident, this.defaultDirectory);

    }

    public static boolean convertVSLGraph(File serFile)
    {
        throw new UnsupportedOperationException();
    }

    public static boolean convertToVersion(File old)
    {
        return false;
    }

}
