package com.voxelplugineering.voxelsniper.api;

import java.io.ObjectOutputStream;

import com.thevoxelbox.vsl.classloader.ASMClassLoader;

public interface IBrushLoader
{

    /**
     * Loads a brush specified by the given byte array. The Class is loaded by the given class loader.
     * 
     * The format for the serialization is as follows:
     * 4-bytes format version
     * 4-bytes brush version
     * {@link NodeGraph} serialized by java's {@link ObjectOutputStream}
     * 
     * @param classLoader the class loader to use to load the compiled class.
     * @param serialized the serialized version of the brush.
     * @return
     */
    Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, byte[] serialized);

    Class<? extends IBrush> loadBrush(ASMClassLoader classLoader, String identifier);

}
