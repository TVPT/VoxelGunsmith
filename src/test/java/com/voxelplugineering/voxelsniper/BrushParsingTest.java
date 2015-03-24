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
package com.voxelplugineering.voxelsniper;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.thevoxelbox.vsl.api.node.Node;
import com.thevoxelbox.vsl.api.variables.VariableHolder;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.thevoxelbox.vsl.variables.ParentedVariableScope;
import com.voxelplugineering.voxelsniper.alias.CommonAliasRegistry;
import com.voxelplugineering.voxelsniper.api.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.api.brushes.Brush;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.brushes.BrushPartType;
import com.voxelplugineering.voxelsniper.api.commands.ArgumentParser;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataContainer;
import com.voxelplugineering.voxelsniper.brushes.BrushChain;
import com.voxelplugineering.voxelsniper.brushes.CommonBrushManager;
import com.voxelplugineering.voxelsniper.util.BrushParsing;

/**
 * A test for the {@link BrushParsing} utility.
 */
public class BrushParsingTest
{

    /**
     * 
     */
    @BeforeClass
    public static void setupGunsmith()
    {
        if (!Gunsmith.getServiceManager().isTesting())
        {
            Gunsmith.getServiceManager().setTesting(new CoreServiceProvider());
        }
    }

    /**
     * 
     */
    @Test
    public void test()
    {
        BrushManager manager = new CommonBrushManager();
        RunCheckBrush voxel = new RunCheckBrush();
        manager.loadBrush("voxel", voxel);
        RunCheckBrush material = new RunCheckBrush();
        manager.loadBrush("material", material);
        AliasRegistry alias = new CommonAliasRegistry("brush");

        BrushChain chain = BrushParsing.parse("voxel material", manager, alias).get();
        Brush[] brushes = chain.getBrushes();
        assertEquals(voxel, brushes[0]);
        assertEquals(material, brushes[1]);

        chain.run(new ParentedVariableScope());

        assertEquals(1, voxel.runtimes);
        assertEquals(1, material.runtimes);
    }

}

class RunCheckBrush implements Brush
{

    boolean hasRun = false;
    int runtimes = 0;

    @Override
    public void fromContainer(DataContainer container)
    {

    }

    @Override
    public DataContainer toContainer()
    {
        return null;
    }

    @Override
    public BrushPartType getType()
    {
        return null;
    }

    @Override
    public String getHelp()
    {
        return null;
    }

    @Override
    public void run(RuntimeState state)
    {
        this.hasRun = true;
        this.runtimes++;
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public void setHelp(String help)
    {

    }

    @Override
    public void setStartNode(Node node)
    {

    }

    @Override
    public void addArgument(String name, ArgumentParser<?> parser, String defaultValue, String... aliases)
    {

    }

    @Override
    public void setArgumentAsPrimary(String name)
    {

    }

    @Override
    public void parseArguments(String string, VariableHolder vars)
    {

    }

}
