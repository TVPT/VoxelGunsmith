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

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;

/**
 * A helper utility for checking annotations.
 */
public class AnnotationHelper
{

    private static Map<Class<?>, List<Class<? extends Annotation>>> annotationCache = new MapMaker().concurrencyLevel(8).weakKeys().weakValues()
            .makeMap();
    private static Map<Class<?>, Map<Class<? extends Annotation>, Annotation>> annotationValueCache = new MapMaker().concurrencyLevel(8).weakKeys()
            .weakValues().makeMap();

    /**
     * Gets whether the given annotation is applied to the given class, or any
     * of its super classes.
     * 
     * @param cls The class to check
     * @param annotation The annotation class to check for
     * @return Whether the annotation is found
     */
    public static boolean doesSuperHaveAnotation(Class<?> cls, Class<? extends Annotation> annotation)
    {
        if (!annotationCache.containsKey(cls))
        {
            List<Class<? extends Annotation>> list = Lists.newArrayList();
            Class<?> next = cls;
            while (next != null)
            {
                for (Annotation a : next.getAnnotations())
                {
                    if (!list.contains(a.annotationType()))
                    {
                        list.add(a.annotationType());
                    }
                }
                next = next.getSuperclass();
            }
            annotationCache.put(cls, list);
            return list.contains(annotation);
        }
        List<Class<? extends Annotation>> list = annotationCache.get(cls);
        return list.contains(annotation);
    }

    /**
     * Gets the annotation value of the given annotation type for the given
     * class.
     * 
     * @param cls The class to check
     * @param annotation The annotation type
     * @return The annotation value
     * @param <T> The annotation type
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> Optional<T> getSuperAnnotation(Class<?> cls, Class<T> annotation)
    {
        Map<Class<? extends Annotation>, Annotation> map;
        if (!annotationValueCache.containsKey(cls))
        {
            map = Maps.<Class<? extends Annotation>, Annotation>newConcurrentMap();
            annotationValueCache.put(cls, map);
        } else
        {
            map = annotationValueCache.get(cls);
        }
        if (doesSuperHaveAnotation(cls, annotation))
        {
            if (!map.containsKey(annotation))
            {
                T a = getSuperAnnotation_(cls, annotation);
                if (a != null)
                {
                    map.put(annotation, a);
                }
                return Optional.fromNullable(a);
            } else
            {
                return Optional.<T>fromNullable((T) map.get(annotation));
            }
        } else
        {
            return Optional.absent();
        }
    }

    private static <T extends Annotation> T getSuperAnnotation_(Class<?> cls, Class<T> annotation)
    {
        Class<?> next = cls;
        while (next != null)
        {
            if (next.isAnnotationPresent(annotation))
            {
                return next.getAnnotation(annotation);
            }
            next = next.getSuperclass();
        }
        return null;
    }

    /**
     * Cleans up and clears the caches for this utility.
     */
    public static void clean()
    {
        annotationCache.clear();
        annotationValueCache.clear();
    }

}
