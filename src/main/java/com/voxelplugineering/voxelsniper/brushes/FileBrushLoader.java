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
package com.voxelplugineering.voxelsniper.brushes;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.voxelplugineering.voxelsniper.Gunsmith;

/**
 * A brush loader to load brushes from the filesystem.
 */
public class FileBrushLoader extends CommonBrushLoader
{

    /**
     * The current file format version of this brush loader.
     */
    private static final int BRUSH_FILE_FORMAT_VERSION = 1;

    /*
     * Brush File Format Outline (Version 1)
     * 
     * 4 bytes - File format version number 4-bytes - Brush version number
     * remaining - serialized IRunnableGraph
     */

    /**
     * The default directory to search for a brush.
     */
    private File defaultDirectory;

    /**
     * Constructs a new FileBrushLoader with the default directory
     * 
     * @param defaultDir the directory to load brushes, cannot be null
     */
    public FileBrushLoader(File defaultDir)
    {
        checkNotNull(defaultDir, "Default directory cannot be null");
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
     * Loads the brush stored in the given file. The brush is loaded with the
     * given ClassLoader.
     * 
     * @param data the file to load the brush from
     * @return the brush
     */
    public BrushNodeGraph loadBrush(File data)
    {
        checkNotNull(data, "Brush file directory cannot be null");
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
            BrushNodeGraph loaded = loadBrush(brush);
            din.close();
            return loaded;
        } catch (Exception e)
        {
            // Gunsmith.getLogger().error(e, "Error loading brush " +
            // e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads the brush identified by the given name from the given directory.
     * The brush is loaded in with the given ClassLoader. Assumes the file
     * extension is not included. First attempts to load a .brush file with the
     * given name. If that file is not found but a .vsl script with the same
     * name exists the .vsl script is first converted to a .brush file.
     * <p>
     * TODO: add a boolean to ignore file extension and load as if it was a
     * .brush file.
     * 
     * @param name the name of the brush to load, cannot be null or empty
     * @param directory the directory to load the brush from, cannot be null
     * @return the brush
     */
    public BrushNodeGraph loadBrush(String name, File directory)
    {
        checkNotNull(name, "Name cannot be null!");
        checkArgument(!name.isEmpty(), "Name cannot be empty");
        checkNotNull(directory, "Brush directory directory cannot be null");
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
        return loadBrush(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BrushNodeGraph loadBrush(String ident)
    {
        return loadBrush(ident, this.defaultDirectory);
    }

    /**
     * Converts the given .vsl script to a .brush file.
     * 
     * @param serFile the .vsl script to convert, cannot be null
     * @return a success flag
     */
    public static boolean convertVSLGraph(File serFile)
    {
        // TODO convertVSLGraph
        throw new UnsupportedOperationException();
    }

    /**
     * For future file format changes to support converting old file formats to
     * the most recent file format.
     * 
     * @param old the old file, cannot be null
     * @return a success flag
     */
    public static boolean convertToVersion(File old)
    {
        // TODO convertToVersion
        return false;
    }

}
