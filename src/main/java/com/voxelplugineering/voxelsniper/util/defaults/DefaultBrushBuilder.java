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
package com.voxelplugineering.voxelsniper.util.defaults;

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.brush.Brush;
import com.voxelplugineering.voxelsniper.brush.BrushManager;
import com.voxelplugineering.voxelsniper.brush.effect.MaterialBrush;
import com.voxelplugineering.voxelsniper.brush.effect.OldBlendBrush;
import com.voxelplugineering.voxelsniper.brush.effect.OldLinearBlendBrush;
import com.voxelplugineering.voxelsniper.brush.mask.MaterialMaskBrush;
import com.voxelplugineering.voxelsniper.brush.shape.BallBrush;
import com.voxelplugineering.voxelsniper.brush.shape.CylinderBrush;
import com.voxelplugineering.voxelsniper.brush.shape.DiscBrush;
import com.voxelplugineering.voxelsniper.brush.shape.EllipseBrush;
import com.voxelplugineering.voxelsniper.brush.shape.EllipsoidBrush;
import com.voxelplugineering.voxelsniper.brush.shape.SnipeBrush;
import com.voxelplugineering.voxelsniper.brush.shape.VoxelBrush;
import com.voxelplugineering.voxelsniper.brush.shape.VoxelDiscBrush;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * In lieu of having flat file brushes this will temporarily serve as a builder
 * for brushes at runtime for debugging during development.
 */
public final class DefaultBrushBuilder
{

    private DefaultBrushBuilder()
    {

    }

    /**
     * A map of all graphs created from this temporary utility.
     */
    private static final Map<String, Brush> GRAPHS = Maps.newHashMap();

    /**
     * Loads all the graphs from this utility into the global brush manager.
     * 
     * @param manager
     *            the brush manager to load the brushes into
     */
    public static void loadAll(BrushManager manager)
    {
        checkNotNull(manager);
        for (String name : GRAPHS.keySet())
        {
            Brush brush = GRAPHS.get(name);
            manager.loadBrush(name, brush);
        }
    }

    /**
     * Saves all the brushes from this utility into the given directory.
     * 
     * @param directory
     *            the directory to store the brush files in.
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
        // shape
        GRAPHS.put("ball", new BallBrush());
        GRAPHS.put("cylinder", new CylinderBrush());
        GRAPHS.put("disc", new DiscBrush());
        GRAPHS.put("ellipse", new EllipseBrush());
        GRAPHS.put("ellipsoid", new EllipsoidBrush());
        GRAPHS.put("snipe", new SnipeBrush());
        GRAPHS.put("voxel", new VoxelBrush());
        GRAPHS.put("voxeldisc", new VoxelDiscBrush());

        // mask
        GRAPHS.put("materialmask", new MaterialMaskBrush());
        // effect
        GRAPHS.put("material", new MaterialBrush());
        GRAPHS.put("blend", new OldBlendBrush());
        GRAPHS.put("linearblend", new OldLinearBlendBrush());
    }

}
