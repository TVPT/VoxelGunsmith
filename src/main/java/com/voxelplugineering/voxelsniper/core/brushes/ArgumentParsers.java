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
package com.voxelplugineering.voxelsniper.core.brushes;

import com.google.common.base.Optional;
import com.voxelplugineering.voxelsniper.api.entity.Player;
import com.voxelplugineering.voxelsniper.api.service.command.ArgumentParser;
import com.voxelplugineering.voxelsniper.api.service.registry.BiomeRegistry;
import com.voxelplugineering.voxelsniper.api.service.registry.MaterialRegistry;
import com.voxelplugineering.voxelsniper.api.service.registry.PlayerRegistry;
import com.voxelplugineering.voxelsniper.api.service.registry.WorldRegistry;
import com.voxelplugineering.voxelsniper.api.world.World;
import com.voxelplugineering.voxelsniper.api.world.biome.Biome;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.core.util.Context;

/**
 * A static library of standard argument parsers.
 */
@SuppressWarnings("javadoc")
public class ArgumentParsers
{

    public static ArgumentParser<Biome> BIOME_PARSER = null;
    public static ArgumentParser<Double> DOUBLE_PARSER = null;
    public static ArgumentParser<Integer> INTEGER_PARSER = null;
    public static ArgumentParser<Material> MATERIAL_PARSER = null;
    public static ArgumentParser<Player> PLAYER_PARSER = null;
    public static ArgumentParser<String> STRING_PARSER = null;
    public static ArgumentParser<World> WOLRD_PARSER = null;

    /**
     * Initialize
     */
    @SuppressWarnings("rawtypes")
    public static void init(Context context)
    {
        STRING_PARSER = new ArgumentParser.RawParser();

        Optional<BiomeRegistry> biome_reg = context.get(BiomeRegistry.class);
        if (biome_reg.isPresent())
        {
            BIOME_PARSER = new BiomeParser(biome_reg.get());
        }
        INTEGER_PARSER = new ArgumentParser<Integer>()
        {

            @Override
            public Optional<Integer> get(String arg)
            {
                return Optional.of(Integer.parseInt(arg));
            }

        };
        DOUBLE_PARSER = new ArgumentParser<Double>()
        {

            @Override
            public Optional<Double> get(String arg)
            {
                return Optional.of(Double.parseDouble(arg));
            }

        };
        Optional<WorldRegistry> world_reg = context.get(WorldRegistry.class);
        if (world_reg.isPresent())
        {
            WOLRD_PARSER = new WorldParser(world_reg.get());
        }
        Optional<MaterialRegistry> material_reg = context.get(MaterialRegistry.class);
        if (material_reg.isPresent())
        {
            MATERIAL_PARSER = new MaterialParser(material_reg.get());
        }
        Optional<PlayerRegistry> player_reg = context.get(PlayerRegistry.class);
        if (player_reg.isPresent())
        {
            PLAYER_PARSER = new PlayerParser(player_reg.get());
        }
    }

    private static class BiomeParser implements ArgumentParser<Biome>
    {

        private final BiomeRegistry<?> reg;

        public BiomeParser(BiomeRegistry<?> reg)
        {
            this.reg = reg;
        }

        @Override
        public Optional<Biome> get(String arg)
        {
            return this.reg.getBiome(arg);
        }

    }

    private static class PlayerParser implements ArgumentParser<Player>
    {

        private final PlayerRegistry<?> reg;

        public PlayerParser(PlayerRegistry<?> reg)
        {
            this.reg = reg;
        }

        @Override
        public Optional<Player> get(String arg)
        {
            return this.reg.getPlayer(arg);
        }

    }

    private static class WorldParser implements ArgumentParser<World>
    {

        private final WorldRegistry<?> reg;

        public WorldParser(WorldRegistry<?> reg)
        {
            this.reg = reg;
        }

        @Override
        public Optional<World> get(String arg)
        {
            return this.reg.getWorld(arg);
        }

    }

    private static class MaterialParser implements ArgumentParser<Material>
    {

        private final MaterialRegistry<?> reg;

        public MaterialParser(MaterialRegistry<?> reg)
        {
            this.reg = reg;
        }

        @Override
        public Optional<Material> get(String arg)
        {
            return this.reg.getMaterial(arg);
        }

    }
}
