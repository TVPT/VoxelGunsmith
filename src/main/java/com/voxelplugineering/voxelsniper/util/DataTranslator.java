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
package com.voxelplugineering.voxelsniper.util;

import com.voxelplugineering.voxelsniper.entity.EntityType;
import com.voxelplugineering.voxelsniper.service.registry.BiomeRegistry;
import com.voxelplugineering.voxelsniper.service.registry.EntityRegistry;
import com.voxelplugineering.voxelsniper.world.biome.Biome;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * A utility which handles translating data from one class to another if the transition requires
 * additional work than casting.
 */
public class DataTranslator
{

    private static BiomeRegistry<?> BIOME_REGISTRY;

    private static EntityRegistry<?> ENTITY_REGISTRY;

    private static Map<Class<?>, Map<Class<?>, Function<Object, Object>>> TRANSLATORS;

    /**
     * Initializes the {@link DataTranslator}.
     * 
     * @param context
     */
    public static void initialize(Context context)
    {
        BIOME_REGISTRY = context.get(BiomeRegistry.class).orElse(null);
        ENTITY_REGISTRY = context.get(EntityRegistry.class).orElse(null);

        TRANSLATORS = Maps.newHashMap();
        Map<Class<?>, Function<Object, Object>> string = Maps.newHashMap();
        string.put(Byte.class, s -> Byte.valueOf((String) s));
        string.put(Short.class, s -> Short.valueOf((String) s));
        string.put(Integer.class, s -> Integer.valueOf((String) s));
        string.put(Long.class, s -> Long.valueOf((String) s));
        string.put(Float.class, s -> Float.valueOf((String) s));
        string.put(Double.class, s -> Double.valueOf((String) s));
        string.put(Boolean.class, s -> Boolean.valueOf((String) s));
        if (BIOME_REGISTRY != null)
        {
            string.put(Biome.class, s -> BIOME_REGISTRY.getBiome((String) s).orElse(null));
        }
        if (ENTITY_REGISTRY != null)
        {
            string.put(EntityType.class, object -> ENTITY_REGISTRY.getEntityType((String) object).orElse(null));
        }
        TRANSLATORS.put(String.class, string);

    }

    /**
     * Attempts to translate the given object.
     * 
     * @param obj The object to translate
     * @param to The type to translate it to
     * @param <T> The end type
     * @return The translated object, if successful
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> attempt(Object obj, Class<T> to)
    {
        Class<?> from = obj.getClass();
        if (from == to || to.isPrimitive())
        {
            return Optional.of((T) obj);
        }
        if (!TRANSLATORS.containsKey(from))
        {
            return Optional.empty();
        }
        Map<Class<?>, Function<Object, Object>> inner = TRANSLATORS.get(from);
        if (!inner.containsKey(to))
        {
            return Optional.empty();
        }
        Function<Object, Object> translator = inner.get(to);
        try
        {
            return Optional.ofNullable(to.cast(translator.apply(obj)));
        } catch (NumberFormatException e)
        {
            return Optional.empty();
        }
    }
}
