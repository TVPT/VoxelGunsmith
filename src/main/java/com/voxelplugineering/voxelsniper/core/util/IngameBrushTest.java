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
package com.voxelplugineering.voxelsniper.core.util;

import java.util.Map;

import com.google.common.collect.Maps;
import com.thevoxelbox.vsl.api.variables.VariableScope;
import com.thevoxelbox.vsl.variables.ParentedVariableScope;
import com.voxelplugineering.voxelsniper.api.alias.AliasRegistry;
import com.voxelplugineering.voxelsniper.api.brushes.BrushManager;
import com.voxelplugineering.voxelsniper.api.entity.Player;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormat;
import com.voxelplugineering.voxelsniper.api.world.Location;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.core.Gunsmith;
import com.voxelplugineering.voxelsniper.core.brushes.BrushChain;
import com.voxelplugineering.voxelsniper.core.brushes.CommonBrushManager;
import com.voxelplugineering.voxelsniper.core.service.alias.CommonAliasRegistry;
import com.voxelplugineering.voxelsniper.core.shape.csg.CuboidShape;
import com.voxelplugineering.voxelsniper.core.util.defaults.DefaultBrushBuilder;
import com.voxelplugineering.voxelsniper.core.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.core.world.queue.ShapeChangeQueue;

/**
 * A test of all brushes to be excuted from ingame.
 */
public class IngameBrushTest implements Runnable
{

    private final Player sender;
    private static final BrushTest[] tests;
    private static final BrushManager manager;
    private static final AliasRegistry alias;

    static
    {
        manager = new CommonBrushManager();
        alias = new CommonAliasRegistry("brush");
        DefaultBrushBuilder.loadAll(manager);
        tests = new BrushTest[1];
        tests[0] = new BrushTest("voxel")
        {

            @Override
            public void execute(Player player, Location target)
            {
                VariableScope vars = new ParentedVariableScope();
                vars.setCaseSensitive(false);
                vars.set("brushsize", 1.0);
                Material material = player.getWorld().getMaterialRegistry().getMaterial("stone").get();
                vars.set("setMaterial", material);
                vars.set("maskmaterial", material);
                BrushChain chain = BrushParsing.parse("voxel material", manager, alias).get();
                this.executeBrush(player, target, chain, Maps.<String, String>newHashMap(), vars);
            }

            @Override
            public boolean validate(Player player, Location target)
            {
                return assertSingleMaterialArea(player.getWorld(), 3, 3, 3, target.add(-1, -1, -1), player.getWorld().getMaterialRegistry()
                        .getMaterial("stone").get());
            }

        };
    }

    /**
     * Creates a new {@link IngameBrushTest}.
     * 
     * @param sender The player who is executing the test
     */
    public IngameBrushTest(Player sender)
    {
        this.sender = sender;
    }

    /**
     * Runs the test.
     */
    @Override
    public void run()
    {
        this.sender.sendMessage(TextFormat.AQUA + "Starting unit tests. Found " + tests.length + " tests.");
        MaterialShape save =
                this.sender.getWorld().getShapeFromWorld(this.sender.getLocation().add(5, 0, 0), new CuboidShape(3, 3, 3, new Vector3i(-1, -1, -1)));
        int successes = 0;
        for (BrushTest test : tests)
        {
            boolean success;
            try
            {
                success = test.run(this.sender);
            } catch (Exception e)
            {
                success = false;
                Gunsmith.getLogger().error(e, "Error running test for " + test.name);
                this.sender.sendMessage("Error running test: " + e.getMessage());
            }
            if (success)
            {
                successes++;
                this.sender.sendMessage(TextFormat.DARK_GREEN + test.name + " test was executed successfully.");
            } else
            {
                this.sender.sendMessage(TextFormat.DARK_RED + test.name + " test failed.");
            }
        }
        this.sender.addPending(new ShapeChangeQueue(this.sender, this.sender.getLocation().add(5, 0, 0), save));
        this.sender.sendMessage(TextFormat.AQUA + "Finished unit tests. " + successes + "/" + tests.length + " executed successfully.");
    }

}

abstract class BrushTest
{

    private final String playerSysvar = Gunsmith.getConfiguration().get("playerSysVarName", String.class).or("__PLAYER__");

    private final String originVariable = Gunsmith.getConfiguration().get("originVariable", String.class).or("origin");
    private final String yawVariable = Gunsmith.getConfiguration().get("yawVariable", String.class).or("yaw");
    private final String pitchVariable = Gunsmith.getConfiguration().get("pitchVariable", String.class).or("pitch");
    private final String targetBlockVariable = Gunsmith.getConfiguration().get("targetBlockVariable", String.class).or("targetBlock");
    private final String lengthVariable = Gunsmith.getConfiguration().get("lengthVariable", String.class).or("length");

    final String name;

    public BrushTest(String name)
    {
        this.name = name;
    }

    public boolean run(Player player) throws Exception
    {
        player.sendMessage("Starting " + this.name + " test");
        Location target = player.getLocation().add(5, 0, 0);
        execute(player, target);
        player.sendMessage("Waiting on changes.");
        while (player.hasPendingChanges())
        {
            Thread.sleep(20);
        }
        return validate(player, target);
    }

    public abstract void execute(Player player, Location target);

    public abstract boolean validate(Player player, Location target);

    public void executeBrush(Player player, Location target, BrushChain brush, Map<String, String> args, VariableScope vars)
    {
        player.sendMessage("Executing brush: " + brush.getName());
        VariableScope brushVariables = new ParentedVariableScope(vars);
        brushVariables.setCaseSensitive(false);
        brushVariables.set(this.originVariable, target);
        brushVariables.set(this.yawVariable, 0);
        brushVariables.set(this.pitchVariable, 0);
        brushVariables.set(this.targetBlockVariable, target.getWorld().getBlock(target).get());
        brushVariables.set(this.lengthVariable, 5);
        brushVariables.set(this.playerSysvar, player);
        brush.run(brushVariables);
    }

    protected boolean assertSingleMaterialArea(World world, int x, int y, int z, Location target, Material material)
    {
        for (int x0 = 0; x0 < x; x0++)
        {
            for (int y0 = 0; y0 < y; y0++)
            {
                for (int z0 = 0; z0 < z; z0++)
                {
                    if (!world.getBlock(target.getFlooredX() + x0, target.getFlooredY() + y0, target.getFlooredZ() + z0).get().getMaterial()
                            .equals(material))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected boolean assertMaterialArea(World world, int x, int y, int z, Location target, Material... material)
    {
        int i = 0;
        for (int x0 = 0; x0 < x; x0++)
        {
            for (int z0 = 0; z0 < z; z0++)
            {
                for (int y0 = 0; y0 < y; y0++)
                {
                    if (!world.getBlock(target.getFlooredX() + x0, target.getFlooredY() + y0, target.getFlooredZ() + z0).get().getMaterial()
                            .equals(material[i]))
                    {
                        return false;
                    }
                    i++;
                }
            }
        }
        return true;
    }
}
