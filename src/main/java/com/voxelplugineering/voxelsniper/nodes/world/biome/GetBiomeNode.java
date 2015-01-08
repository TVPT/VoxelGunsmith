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
package com.voxelplugineering.voxelsniper.nodes.world.biome;

import com.google.common.base.Optional;
import com.thevoxelbox.vsl.node.Node;
import com.thevoxelbox.vsl.util.Provider;
import com.thevoxelbox.vsl.util.RuntimeState;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.entity.living.Player;
import com.voxelplugineering.voxelsniper.api.world.biome.Biome;

/**
 * A node for getting a {@link Biome} by name.
 */
public class GetBiomeNode extends Node
{

    private final Provider<String> name;
    private final Provider<Biome> biome;

    /**
     * Creates a new node.
     * 
     * @param name The name provider
     */
    public GetBiomeNode(Provider<String> name)
    {
        this.name = name;
        this.biome = new Provider<Biome>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec(RuntimeState state)
    {
        Optional<Biome> b = Gunsmith.getBiomeRegistry().getBiome(this.name.get(state));
        if (b.isPresent())
        {
            this.biome.set(b.get(), state.getUUID());
        } else
        {
            state.getVars().<Player>get("__PLAYER__", Player.class).get().sendMessage("Unknown biome: " + this.name.get(state));
        }
    }

    /**
     * Gets biome provider.
     * 
     * @return The biome
     */
    public Provider<Biome> getBiome()
    {
        return this.biome;
    }

}
