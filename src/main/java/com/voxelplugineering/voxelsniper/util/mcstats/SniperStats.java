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
package com.voxelplugineering.voxelsniper.util.mcstats;

import com.voxelplugineering.voxelsniper.service.platform.PlatformProxy;

import com.google.common.collect.Maps;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Implementation of Sniper Stats.
 */
public final class SniperStats extends Metrics
{

    private static int snipesDone;
    private static long snipeCounterInitTimeStamp;
    private static Map<String, Integer> brushUsageCounter = Maps.newHashMap();

    /**
     * Increase the Snipes Counter.
     */
    public static void increaseSnipeCounter()
    {
        SniperStats.snipesDone++;
    }

    /**
     * Increase usage for a specific brush.
     * 
     * @param brushName Name of the Brush
     */
    public static void increaseBrushUsage(String brushName)
    {
        if (brushUsageCounter.get(brushName) == null)
        {
            brushUsageCounter.put(brushName, 0);
        }
        brushUsageCounter.put(brushName, brushUsageCounter.get(brushName));
    }

    /**
     * Set Initialization time for reference when calculating average Snipes per Minute.
     * 
     * @param currentTimeMillis Current time
     */
    public static void setSnipeCounterInitTimeStamp(final long currentTimeMillis)
    {
        SniperStats.snipeCounterInitTimeStamp = currentTimeMillis;
    }

    private final PlatformProxy platform;

    /**
     * Creates a new instance of SniperStats for Metrics.
     * 
     * @param pluginVersion The plugin version string marked by the platform
     * @throws IOException In the event Metrics is unable to start
     */
    public SniperStats(String pluginVersion, PlatformProxy platform) throws IOException
    {
        super("VoxelSniper", pluginVersion);
        this.platform = platform;
    }

    @Override
    public String getServerVersion()
    {
        return this.platform.getVersion();
    }

    @Override
    public int getPlayersOnline()
    {
        return this.platform.getNumberOfPlayersOnline();
    }

    @Override
    public File getConfigFile()
    {
        return null;// Gunsmith.getPlatformProxy().getMetricsFile();
    }

    /**
     * Initializes the stats manager.
     */
    public void init()
    {
        try
        {
            final Graph defaultGraph = createGraph("Default");
            defaultGraph.addPlotter(new Metrics.Plotter("Average Snipes per Minute")
            {

                @Override
                public int getValue()
                {
                    final int currentSnipes = snipesDone;
                    final long initializationTimeStamp = snipeCounterInitTimeStamp;
                    final double deltaTime = System.currentTimeMillis() - initializationTimeStamp;

                    double average;
                    if (deltaTime < 60000)
                    {
                        average = currentSnipes;
                    } else
                    {
                        final double timeRunning = deltaTime / 60000;
                        average = currentSnipes / timeRunning;
                    }

                    if (average > 10000)
                    {
                        average = 0;
                    }

                    return (int) Math.floor(average);
                }
            });

            final Graph brushUsageGraph = createGraph("Brush Usage");

            for (final Map.Entry<String, Integer> entry : brushUsageCounter.entrySet())
            {
                brushUsageGraph.addPlotter(new Metrics.Plotter(entry.getKey())
                {

                    @Override
                    public int getValue()
                    {
                        return entry.getValue();
                    }

                    @Override
                    public void reset()
                    {
                        brushUsageCounter.remove(entry.getKey());
                    }
                });
            }

            start();
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Disables the stats manager.
     */
    public void stop()
    {
        try
        {
            disable();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Restarts the stats manager.
     */
    public void restart()
    {
        stop();
        start();
    }
}
