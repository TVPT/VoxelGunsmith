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
