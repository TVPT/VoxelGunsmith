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
package com.voxelplugineering.voxelsniper.core;

/**
 * The main class for utility operations.
 */
public class GunsmithMain
{

    /**
     * The main method.
     * 
     * @param args The program arguments.
     */
    public static void main(String[] args)
    {
        if (args.length == 1 && args[0].equalsIgnoreCase("--testinit"))
        {
            System.out.println("Starting init -> stop cycle test.");
            Gunsmith.getServiceManager().start();
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException ignored)
            {
                // Interrupted, skip the wait and stop immediately
            }
            System.out.println();
            Gunsmith.getServiceManager().shutdown();
        } else
        {
            System.out.println("Usage: java -jar Gunsmith.jar <command> [args]\n"
                    + "\t--generate <directory>\t: Generates all default brushes into the given directory.\n"
                    + "\t--testinit\t\t: Performs a full initialization to shutdown cycle.\n");
        }
    }
}
