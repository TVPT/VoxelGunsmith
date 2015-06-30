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
package com.voxelplugineering.voxelsniper.core.util.defaults;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.shape.Shape;
import com.voxelplugineering.voxelsniper.api.world.Block;
import com.voxelplugineering.voxelsniper.api.world.Chunk;
import com.voxelplugineering.voxelsniper.api.world.biome.Biome;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.brush.Brush;
import com.voxelplugineering.voxelsniper.brush.BrushManager;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.defaults.BallBrush;
import com.voxelplugineering.voxelsniper.brush.defaults.MaterialBrush;
import com.voxelplugineering.voxelsniper.core.GunsmithLogger;

/**
 * In lieu of having flat file brushes this will temporarily serve as a builder
 * for brushes at runtime for debugging during development.
 */
public class DefaultBrushBuilder
{

    /**
     * A map of all graphs created from this temporary utility.
     */
    private static final Map<String, Brush> graphs = Maps.newHashMap();

    /**
     * Loads all the graphs from this utility into the global brush manager.
     * 
     * @param manager the brush manager to load the brushes into
     */
    public static void loadAll(BrushManager manager)
    {
        checkNotNull(manager);
        for (String name : graphs.keySet())
        {
            Brush brush = graphs.get(name);
            manager.loadBrush(name, brush);
        }
    }

    /**
     * Saves all the brushes from this utility into the given directory.
     * 
     * @param directory the directory to store the brush files in.
     */
    /*
     * public static void saveAll(File directory) { checkNotNull(directory); if
     * (!directory.exists()) { directory.mkdirs(); } for (String name :
     * graphs.keySet()) { DataOutputStream output = null; try { Brush brush =
     * graphs.get(name); File f = new File(directory, brush.getName() + ".br");
     * if (f.exists()) { f.delete(); } f.createNewFile(); output = new
     * DataOutputStream(new FileOutputStream(f)); output.writeInt(1);
     * output.writeInt(0); output.flush(); ObjectOutputStream objOut = new
     * ObjectOutputStream(output); objOut.writeObject(brush); objOut.flush();
     * objOut.close(); } catch (Exception e) { GunsmithLogger.getLogger()
     * .warn("Error saving brush " + name + ": " + e.getClass().getName() + " "
     * + e.getMessage()); } finally { if (output != null) { try {
     * output.close(); } catch (IOException ignored) { assert true; } } } } }
     */

    /**
     * Creates and loads all brushes into the global brush manager.
     */
    public static void buildBrushes()
    {
        graphs.put("ball", new BallBrush());
        graphs.put("material", new MaterialBrush());
    }

}
