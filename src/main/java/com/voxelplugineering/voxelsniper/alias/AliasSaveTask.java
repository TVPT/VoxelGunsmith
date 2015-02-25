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
package com.voxelplugineering.voxelsniper.alias;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.alias.AliasHandler;
import com.voxelplugineering.voxelsniper.service.persistence.JsonDataSource;

/**
 * A task for saving aliases.
 */
public class AliasSaveTask implements Runnable
{

    private List<AliasHandler> dirty;

    /**
     * Creates a new {@link AliasSaveTask}.
     */
    public AliasSaveTask()
    {
        this.dirty = Lists.newArrayList();
    }

    /**
     * Queues the given {@link AliasHandler} to be saved in the next pass.
     * 
     * @param alias The alias handler to queue
     */
    public synchronized void addDirty(AliasHandler alias)
    {
        checkNotNull(alias);
        if (!this.dirty.contains(alias))
        {
            this.dirty.add(alias);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void run()
    {
        for (Iterator<AliasHandler> it = this.dirty.iterator(); it.hasNext();)
        {
            AliasHandler alias = it.next();
            try
            {
                JsonDataSource data = new JsonDataSource(alias.getOwner().getAliasFile());
                data.write(alias);
            } catch (IOException e)
            {
                Gunsmith.getLogger().error(e, "Error saving aliases");
            }
            it.remove();
        }
    }

}
