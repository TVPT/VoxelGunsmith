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
package com.voxelplugineering.voxelsniper.util.schematic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.entity.MessageReceiver;
import com.voxelplugineering.voxelsniper.api.shape.MaterialShape;
import com.voxelplugineering.voxelsniper.api.util.schematic.SchematicLoader;
import com.voxelplugineering.voxelsniper.api.util.text.TextFormat;
import com.voxelplugineering.voxelsniper.api.world.material.Material;
import com.voxelplugineering.voxelsniper.shape.NamedWorldSection;
import com.voxelplugineering.voxelsniper.shape.csg.CuboidShape;
import com.voxelplugineering.voxelsniper.util.math.Vector3i;
import com.voxelplugineering.voxelsniper.util.nbt.ByteArrayTag;
import com.voxelplugineering.voxelsniper.util.nbt.CompoundTag;
import com.voxelplugineering.voxelsniper.util.nbt.IntTag;
import com.voxelplugineering.voxelsniper.util.nbt.ListTag;
import com.voxelplugineering.voxelsniper.util.nbt.NBTInputStream;
import com.voxelplugineering.voxelsniper.util.nbt.NBTOutputStream;
import com.voxelplugineering.voxelsniper.util.nbt.ShortTag;
import com.voxelplugineering.voxelsniper.util.nbt.StringTag;
import com.voxelplugineering.voxelsniper.util.nbt.Tag;

/**
 * A {@link SchematicLoader} for NBT stored schematics
 */
public class NBTSchematicLoader implements SchematicLoader
{

    /* TODO unit tests for this
     * 
     */

    /**
     * Creates a new {@link NBTSchematicLoader}.
     */
    public NBTSchematicLoader()
    {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MaterialShape load(File file) throws IOException
    {
        FileInputStream stream = new FileInputStream(file);
        NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(stream));
        // Entire schematic is contained within main compound tag
        CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
        nbtStream.close();

        if (!schematicTag.getName().equals("Schematic"))
        {
            throw new UnsupportedOperationException("Tag \"Schematic\" does not exist or is not first");
        }
        // check that the schematic contains data
        if (!schematicTag.contains("Blocks"))
        {
            throw new UnsupportedOperationException("Schematic file is missing a \"Blocks\" tag");
        }
        if (!schematicTag.contains("MaterialDictionary"))
        {
            return new LegacyConverter(file, schematicTag).convert();
        }
        // extract dimensions
        short width = schematicTag.getChildTag("Width", ShortTag.class).get().getValue();
        short length = schematicTag.getChildTag("Length", ShortTag.class).get().getValue();
        short height = schematicTag.getChildTag("Height", ShortTag.class).get().getValue();
        String materials = schematicTag.getChildTag("Materials", StringTag.class).get().getValue();
        if (!materials.equals("Alpha"))
        {
            throw new UnsupportedOperationException("Schematic file is not an Alpha schematic");
        }
        // extract block id and data values, addId is used if the schematic
        // contains block ids above 255
        byte[] blockId = schematicTag.getChildTag("Blocks", ByteArrayTag.class).get().getValue();
        byte[] addId = new byte[0];
        short[] blocks = new short[blockId.length];
        if (schematicTag.contains("AddBlocks"))
        {
            addId = schematicTag.getChildTag("AddBlocks", ByteArrayTag.class).get().getValue();
        }
        // combine blockId and addId into a single short array blocks
        for (int index = 0; index < blockId.length; index++)
        {
            if ((index >> 1) >= addId.length)
            {
                blocks[index] = (short) (blockId[index] & 0xFF);
            } else
            {
                if ((index & 1) == 0)
                {
                    blocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blockId[index] & 0xFF));
                } else
                {
                    blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blockId[index] & 0xFF));
                }
            }
        }
        // Need to pull out tile entities from this list of compound tags
        List<Tag> tileEntities = schematicTag.getChildTag("TileEntities", ListTag.class).get().getValue();
        // this is the map we will populate with all extracted time entities
        Map<Vector3i, CompoundTag> tileEntitiesMap = Maps.newHashMap();
        for (Tag tag : tileEntities)
        {
            if (!(tag instanceof CompoundTag))
            {
                continue;
            }
            CompoundTag tileEntity = (CompoundTag) tag;
            int x = 0;
            int y = 0;
            int z = 0;

            if (tileEntity.contains("x"))
            {
                x = tileEntity.getChildTag("x", IntTag.class).get().getValue();
            }
            if (tileEntity.contains("y"))
            {
                y = tileEntity.getChildTag("y", IntTag.class).get().getValue();
            }
            if (tileEntity.contains("z"))
            {
                z = tileEntity.getChildTag("z", IntTag.class).get().getValue();
            }

            Vector3i vec = new Vector3i(x, y, z);
            tileEntitiesMap.put(vec, tileEntity);
        }

        List<Tag> entities = schematicTag.getChildTag("Entities", ListTag.class).get().getValue();

        // this is the map we will populate with all extracted time entities
        Map<Vector3i, CompoundTag> entitiesMap = new HashMap<Vector3i, CompoundTag>();
        for (Tag tag : entities)
        {
            if (!(tag instanceof CompoundTag))
            {
                continue;
            }
            CompoundTag entity = (CompoundTag) tag;
            int x = 0;
            int y = 0;
            int z = 0;

            if (entity.contains("x"))
            {
                x = entity.getChildTag("x", IntTag.class).get().getValue();
            }
            if (entity.contains("y"))
            {
                y = entity.getChildTag("y", IntTag.class).get().getValue();
            }
            if (entity.contains("z"))
            {
                z = entity.getChildTag("z", IntTag.class).get().getValue();
            }
            Vector3i vec = new Vector3i(x, y, z);
            entitiesMap.put(vec, entity);
        }

        //Load material dictionary
        List<Tag> dict = schematicTag.getChildTag("MaterialDictionary", ListTag.class).get().getValue();
        Map<Short, Material> materialDict = Maps.newHashMap();
        for (Tag tag : dict)
        {
            if (!(tag instanceof CompoundTag))
            {
                continue;
            }
            CompoundTag material = (CompoundTag) tag;
            short key = material.getChildTag("Key", ShortTag.class).get().getValue();
            String mat = material.getChildTag("Name", StringTag.class).get().getValue();
            materialDict.put(key, Gunsmith.getDefaultMaterialRegistry().getMaterial(mat).or(Gunsmith.getDefaultMaterialRegistry().getAirMaterial()));
        }
        // create region and return
        NamedWorldSection region;
        if (schematicTag.contains("WEOffsetX") && schematicTag.contains("WEOffsetY") && schematicTag.contains("WEOffsetZ"))
        {
            int offsetX = schematicTag.getChildTag("WEOffsetX", IntTag.class).get().getValue();
            int offsetY = schematicTag.getChildTag("WEOffsetY", IntTag.class).get().getValue();
            int offsetZ = schematicTag.getChildTag("WEOffsetZ", IntTag.class).get().getValue();
            region = new NamedWorldSection(new CuboidShape(width, height, length, new Vector3i(offsetX, offsetY, offsetZ)), materialDict);
        } else
        {
            region = new NamedWorldSection(new CuboidShape(width, height, length, new Vector3i(0, 0, 0)), materialDict);
        }
        String name;
        if (schematicTag.contains("name"))
        {
            name = schematicTag.getChildTag("name", StringTag.class).get().getValue();
        } else
        {
            name = file.getName().replace(".schematic", "");
        }

        // region.setBlocks(blockData, blocks);
        region.setTileEntityMap(tileEntitiesMap);
        region.setEntitiesMap(entitiesMap);
        region.setName(name);
        return region;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(File file, MaterialShape shape, MessageReceiver owner) throws IOException
    {
        int width = shape.getWidth();
        int height = shape.getHeight();
        int length = shape.getLength();

        // length, width and height cannot be larger than the size of an
        // unsigned short
        if (width > 65535 || height > 65535 || length > 65535)
        {
            if (owner == null)
            {
                Gunsmith.getLogger().warn("Failed to save schematic to " + file.getName() + ": Dimensions exceeded max size supported.");
            } else
            {
                owner.sendMessage(TextFormat.RED + "Could not save schematic, dimensions exceeded maximum supported size!");
            }
            return;
        }

        // the map of tags for the main schematic compound tag
        Map<String, Tag> schematic = new HashMap<String, Tag>();
        // Store basic schematic data
        schematic.put("Width", new IntTag("Width", (short) width));
        schematic.put("Height", new IntTag("Height", (short) height));
        schematic.put("Length", new IntTag("Length", (short) length));
        schematic.put("Materials", new StringTag("Materials", "Alpha"));

        // Extract tile entities and load into the main schematic map

        List<CompoundTag> tiles = Lists.newArrayList();
        List<CompoundTag> entities = Lists.newArrayList();

        if (shape instanceof NamedWorldSection)
        {
            NamedWorldSection complex = (NamedWorldSection) shape;
            if (complex.hasTileEntities())
            {
                for (Vector3i v : complex.getTileEntities().keySet())
                {
                    CompoundTag tile = complex.getTileEntities().get(v);
                    tiles.add(tile);
                }
            }
            if (complex.hasEntities())
            {
                for (Vector3i v : complex.getEntities().keySet())
                {
                    CompoundTag entity = complex.getEntities().get(v);
                    entities.add(entity);
                }
            }
        }

        // save the region reference point in the WorldEdit style for
        // compatibility
        Vector3i origin = shape.getOrigin();
        schematic.put("WEOffsetX", new IntTag("WEOffsetX", origin.getX()));
        schematic.put("WEOffsetY", new IntTag("WEOffsetY", origin.getY()));
        schematic.put("WEOffsetZ", new IntTag("WEOffsetZ", origin.getZ()));

        // store the lower byte of the block Ids
        schematic.put("Blocks", new ByteArrayTag("Blocks", shape.getLowerMaterialData()));
        if (shape.hasExtraData())
        {
            schematic.put("AddBlocks", new ByteArrayTag("AddBlocks", shape.getUpperMaterialData()));
        }
        if (shape instanceof NamedWorldSection)
        {
            NamedWorldSection complex = (NamedWorldSection) shape;
            if (complex.hasTileEntities())
            {
                schematic.put("TileEntities", new ListTag("TileEntities", CompoundTag.class, tiles));
            }
            if (complex.hasEntities())
            {
                schematic.put("Entities", new ListTag("Entities", CompoundTag.class, entities));
            }
        }

        Map<Short, Material> materialDict = shape.getMaterialsDictionary();
        List<CompoundTag> materials = Lists.newArrayList();
        for (Map.Entry<Short, Material> entry : materialDict.entrySet())
        {
            Map<String, Tag> material = Maps.newHashMap();
            material.put("Key", new ShortTag("Key", entry.getKey()));
            material.put("Name", new StringTag("Name", entry.getValue().getName()));
            CompoundTag tag = new CompoundTag("Material", material);
            materials.add(tag);
        }
        schematic.put("MaterialDictionary", new ListTag("MaterialDictionary", CompoundTag.class, materials));

        CompoundTag schematicTag = new CompoundTag("Schematic", schematic);
        NBTOutputStream stream = new NBTOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
        stream.writeTag(schematicTag);
        stream.close();
    }

    /**
     * A converter for converting MCEdit schematics to the new format.
     */
    public static class LegacyConverter implements SchematicLoader.Converter
    {

        /**
         * Creates a new {@link LegacyConverter}.
         * 
         * @param file The schematic file
         * @param schematicTag The schematic root NBT node
         */
        public LegacyConverter(File file, CompoundTag schematicTag)
        {
            // TODO
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MaterialShape convert()
        {
            // TODO
            return null;
        }

    }

}
