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
package com.voxelplugineering.voxelsniper.api.entity;

import com.voxelplugineering.voxelsniper.api.service.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.api.service.alias.AliasOwner;
import com.voxelplugineering.voxelsniper.api.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.api.world.queue.ChangeQueueOwner;
import com.voxelplugineering.voxelsniper.brush.BrushHolder;

/**
 * Representation of a user within Gunsmith. Holds all state information relevant to the user.
 */
public interface Player extends CommandSender, Living, BrushHolder, ChangeQueueOwner, AliasOwner
{

    /**
     * Returns this sniper's personal alias handler.
     * 
     * @return the alias handler
     */
    AliasHandler getAliasHandler();

}