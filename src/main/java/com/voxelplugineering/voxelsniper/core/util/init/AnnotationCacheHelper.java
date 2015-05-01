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
package com.voxelplugineering.voxelsniper.core.util.init;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;

/**
 * A cache for method annotations.
 */
@SuppressWarnings("rawtypes")
public class AnnotationCacheHelper
{

    private Map<Class<?>, Map<Class, List<Method>>> cache;

    /**
     * Creates a new {@link AnnotationCacheHelper}.
     */
    public AnnotationCacheHelper()
    {
        this.cache = new MapMaker().weakKeys().makeMap();
    }

    /**
     * Builds the cache for the given class.
     * 
     * @param target The target class
     */
    public void build(Class<?> target)
    {
        if (this.cache.containsKey(target))
        {
            return;
        }
        //System.out.println("Building cache for " + target.getName());
        Map<Class, List<Method>> anno = new MapMaker().weakKeys().makeMap();
        for (Method m : target.getMethods())
        {
            for (Annotation a : m.getAnnotations())
            {
                if (!anno.containsKey(a.annotationType()))
                {
                    anno.put(a.annotationType(), Lists.<Method>newArrayList());
                }
                //System.out.println("\t" + a.annotationType().getSimpleName() + " - " + m.toGenericString());
                anno.get(a.annotationType()).add(m);
            }
        }
        this.cache.put(target, anno);
    }

    /**
     * Gets all methods from the target class with the given annotation.
     * 
     * @param target The target class
     * @param annotation The requested annotation
     * @return All methods with the given annotation
     */
    public List<Method> get(Class<?> target, Class<?> annotation)
    {
        if (!annotation.isAnnotation())
        {
            return new ArrayList<Method>(0);
        }
        build(target);
        List<Method> methods = this.cache.get(target).get(annotation);
        if (methods == null)
        {
            return new ArrayList<Method>(0);
        }
        return methods;
    }

    /**
     * Gets all methods from the target class with the given annotation. Returns all methods which
     * also pass the given function.
     * 
     * @param target The target class
     * @param annotation The requested annotation
     * @param valid A function to check method validity
     * @return All applicable methods
     */
    public List<Method> get(Class<?> target, Class<?> annotation, Function<Method, Boolean> valid)
    {
        List<Method> methods = get(target, annotation);
        for (Iterator<Method> iter = methods.iterator(); iter.hasNext();)
        {
            if (!valid.apply(iter.next()))
            {
                iter.remove();
            }
        }
        return methods;
    }

}
