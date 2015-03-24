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
package com.voxelplugineering.voxelsniper.api.alias;

import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceProvider;
import com.voxelplugineering.voxelsniper.api.service.persistence.DataSourceReader;
import com.voxelplugineering.voxelsniper.service.persistence.JsonDataSourceReader;
import com.voxelplugineering.voxelsniper.service.persistence.MemoryContainer;

/**
 * An owner of an alias handler.
 */
public interface AliasOwner
{

    /**
     * Gets the data file for alias storage.
     * 
     * @return The data folder
     */
    public DataSourceReader getAliasSource();

    /**
     * The alias owner for gunsmith's global alias handler.
     */
    public static class GunsmithAliasOwner implements AliasOwner
    {

        /**
         * {@inheritDoc}
         */
        @Override
        public DataSourceReader getAliasSource()
        {
            DataSourceProvider provider = Gunsmith.getPlatformProxy().getRootDataSourceProvider();
            return provider.getWithReader("aliases.json", JsonDataSourceReader.class, new MemoryContainer("")).get();
        }

    }

}
