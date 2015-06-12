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
package com.voxelplugineering.voxelsniper.core.commands;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.entity.Player;
import com.voxelplugineering.voxelsniper.api.service.command.CommandSender;
import com.voxelplugineering.voxelsniper.api.service.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.api.service.text.TextFormat;
import com.voxelplugineering.voxelsniper.api.world.queue.OfflineUndoHandler;
import com.voxelplugineering.voxelsniper.api.world.queue.UndoQueue;
import com.voxelplugineering.voxelsniper.core.util.Context;

/**
 * A command get fetching the help information for a brush.
 */
public class UndoOtherCommand extends Command
{

    private final PlayerRegistry<?> players;
    private final OfflineUndoHandler undoHandler;

    /**
     * Creates a new Command instance.
     */
    public UndoOtherCommand(Context context)
    {
        super("undoother", "Undoes another player's last n changes. Usage: /undoother [name] [n]", context);
        setAliases("uu");
        setPermissions("voxelsniper.command.undoother");
        this.players = context.getRequired(PlayerRegistry.class);
        this.undoHandler = context.getRequired(OfflineUndoHandler.class);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage(this.getHelpMsg());
            return true;
        }
        String name = args[0];
        UndoQueue queue = null;
        Optional<Player> target = this.players.getPlayer(name);
        if (!target.isPresent())
        {
            Optional<UndoQueue> attempt = this.undoHandler.get(name);
            if (attempt.isPresent())
            {
                queue = attempt.get();
            }
        } else
        {
            queue = target.get().getUndoHistory();
        }
        if (queue == null)
        {
            sender.sendMessage(TextFormat.RED + "Sorry, we could not find that player.");
            return true;
        }
        int n = 1;
        if (args.length > 1)
        {
            try
            {
                n = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored)
            {
                assert true;
            }
            if (n < 1)
            {
                n = 1;
            }
        }
        sender.sendMessage(TextFormat.GOLD + "Undoing " + name + "'s last " + n + " changes!");
        queue.undo(n);
        return true;
    }
}
