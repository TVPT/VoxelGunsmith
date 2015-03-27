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
package com.voxelplugineering.voxelsniper.world.queue;

import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.entity.Player;

/**
 * A task for executing pending change queues.
 */
public class ChangeQueueTask implements Runnable
{

    /**
     * Performs a set of changes fro all players with pending changes.
     */
    @Override
    public void run()
    {
        // long start = System.currentTimeMillis();
        int n = 0;
        for (Player p : Gunsmith.getPlayerRegistry().getPlayers())
        {
            if (p.hasPendingChanges())
            {
                n++;
            }
        }
        if (n == 0)
        {
            return;
        }
        int remaining = (Integer) Gunsmith.getConfiguration().get("blockChangesPerSecond").get();
        remaining /= 10;
        for (Player p : Gunsmith.getPlayerRegistry().getPlayers())
        {
            if (!p.hasPendingChanges())
            {
                continue;
            }
            int allocation = remaining / (n--);
            // For the frankly hilarious case where there are more pending
            // change queues than blocks allocated per tick we simple set the
            // allocation to a minimum of 1 which will at least do something
            // slowly rather than nothing
            if (allocation < 0)
            {
                allocation = 1;
            }
            int actual = 0;
            while (p.hasPendingChanges() && actual < allocation)
            {
                try
                {
                    actual += p.getNextPendingChange().get().perform(allocation);
                } catch (Exception e)
                {
                    Gunsmith.getLogger().error(e, "Error while performing change operation!");
                    p.clearNextPending();
                }
                if (p.getNextPendingChange().get().isFinished())
                {
                    p.clearNextPending();
                }
            }
            remaining -= actual;
            if (remaining <= 0)
            {
                break;
            }
        }
        // Gunsmith.getLogger().info("Change queue tick length: " +
        // (System.currentTimeMillis() - start) + " ms");
    }

}
