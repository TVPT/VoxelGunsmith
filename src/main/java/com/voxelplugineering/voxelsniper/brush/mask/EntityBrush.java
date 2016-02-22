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
package com.voxelplugineering.voxelsniper.brush.mask;

import com.voxelplugineering.voxelsniper.brush.Brush;
import com.voxelplugineering.voxelsniper.brush.BrushInfo;
import com.voxelplugineering.voxelsniper.brush.BrushKeys;
import com.voxelplugineering.voxelsniper.brush.BrushParam;
import com.voxelplugineering.voxelsniper.brush.BrushPartType;
import com.voxelplugineering.voxelsniper.brush.BrushVars;
import com.voxelplugineering.voxelsniper.brush.ExecutionResult;
import com.voxelplugineering.voxelsniper.config.VoxelSniperConfiguration;
import com.voxelplugineering.voxelsniper.entity.EntityType;
import com.voxelplugineering.voxelsniper.entity.Player;
import com.voxelplugineering.voxelsniper.util.math.Vector3d;
import com.voxelplugineering.voxelsniper.world.Block;
import com.voxelplugineering.voxelsniper.world.queue.EntityChangeQueue;

import java.util.Optional;

// TODO add disc based spawning area to choose from.
@BrushInfo(name = "entity",
        type = BrushPartType.MISC,
        params = {@BrushParam(name = BrushKeys.ENTITY_TYPE, desc = "The entity type to spawn"),
                  @BrushParam(name = BrushKeys.ENTITY_COUNT, desc = "The amount of entities")},
        permission = "voxelsniper.brush.entity")
public class EntityBrush extends Brush {

    @Override
    public ExecutionResult run(Player player, BrushVars args) {
        final double size = args.get(BrushKeys.BRUSH_SIZE, Double.class).get();
        Optional<EntityType> entityType = args.get(BrushKeys.ENTITY_TYPE, EntityType.class);
        if (!entityType.isPresent()) {
            player.sendMessage(VoxelSniperConfiguration.missingEntity, "RANDOM");
            return ExecutionResult.abortExecution();
        }
        final int count = args.get(BrushKeys.ENTITY_COUNT, Integer.class).orElse(1);
        final Optional<Block> vector3d = args.get(BrushKeys.TARGET_BLOCK, Block.class);
        final Vector3d position = vector3d.get().getLocation().toVector();
        new EntityChangeQueue(player, player.getWorld(), entityType.get(), position, count, size).flush();
        return ExecutionResult.continueExecution();
    }
}
