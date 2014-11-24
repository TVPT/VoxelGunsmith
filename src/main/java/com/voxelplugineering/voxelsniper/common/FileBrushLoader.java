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
import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrush;

/**
 * A brush loader to load brushes from the filesystem.
 */
public class FileBrushLoader extends CommonBrushLoader
{

    /**
     * The current format version number.
     */
    public static final int BRUSH_FILE_FORMAT_VERSION = 1;

    /*
     * Brush File Format Outline (Version 1)
     * 
     * 4 bytes - File format version number
     * 4-bytes - Brush version number
     * remaining - serialized IRunnableGraph
     */

    /**
     * The default directory to search for a brush.
     */
    private File defaultDirectory;

    /**
     * Constructs a new FileBrushLoader with the default directory
     *
     * @param defaultDir the directory to load brushes
     */
    public FileBrushLoader(File defaultDir)
    {
        this.defaultDirectory = defaultDir;
    }

    /**
     * Gets the default directory in the file system
     *
     * @return the default file directory
     */
    public File getDefaultDirectory()
    {
        return this.defaultDirectory;
    }

    /**
     * Loads the brush stored in the given file. The brush is loaded with the given ClassLoader.
     * 
     * @param classLoader the classloader to load the brush with
     * @param data the file to load the brush from
     * @return the brush
     */
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
            din.readInt(); // the brush version, currently unsupported
            int length = din.available();
            byte[] brush = new byte[length];
            din.read(brush, 0, length);
            Class<? extends IBrush> loaded = loadBrush(classLoader, brush);
            din.close();
            return loaded;
        } catch (Exception e)
        {
            Gunsmith.getLogger().error(e, "Error loading brush " + e.getMessage());
            return null;
        }
    }

    /**
     * Loads the brush identified by the given name from the given directory. The brush is loaded in with the given ClassLoader. Assumes the file
     * extension is not included. First attempts to load a .brush file with the given name. If that file is not found but a .vsl script with the same
     * name exists the .vsl script is first converted to a .brush file.
     * <p>
     * TODO: add a boolean to ignore file extension and load as if it was a .brush file.
     * 
     * @param classLoader the class loader to load the brush with
     * @param name the name of the brush to load
     * @param directory the directory to load the brush from
     * @return the brush
     */
    public Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, String name, File directory)
    {
        File data = new File(directory, name + ".br");
        if (!data.exists())
        {
            File ser = new File(directory, name + ".ser");
            if (ser.exists())
            {
                boolean success = FileBrushLoader.convertVSLGraph(ser);
                if (!success)
                {
                    Gunsmith.getLogger().warn("Failed to convert " + ser.getAbsolutePath());
                    return null;
                }
            } else
            {
                return null;
            }
        }
        return loadBrush(classLoader, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, String ident)
    {
        return loadBrush(classLoader, ident, this.defaultDirectory);

    }

    /**
     * Converts the given .vsl script to a .brush file.
     * 
     * @param serFile the .vsl script to convert
     * @return a success flag
     */
    public static boolean convertVSLGraph(File serFile)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * For future file format changes to support converting old file formats to the most recent file format.
     * 
     * @param old the old file
     * @return a success flag
     */
    public static boolean convertToVersion(File old)
    {
        return false;
    }

}
