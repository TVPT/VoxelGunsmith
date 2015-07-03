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

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class DataTranslator {

    private static Map<Class<?>, Map<Class<?>, Function<Object, Object>>> TRANSLATORS;

    static {
        TRANSLATORS = Maps.newHashMap();
        Map<Class<?>, Function<Object, Object>> string = Maps.newHashMap();
        string.put(Byte.class, new Function<Object, Object>() {

            @Override
            public Object apply(Object s) {
                return Byte.valueOf((String) s);
            }
        });
        string.put(Short.class, new Function<Object, Object>() {

            @Override
            public Object apply(Object s) {
                return Short.valueOf((String) s);
            }
        });
        string.put(Integer.class, new Function<Object, Object>() {

            @Override
            public Object apply(Object s) {
                return Integer.valueOf((String) s);
            }
        });
        string.put(Long.class, new Function<Object, Object>() {

            @Override
            public Object apply(Object s) {
                return Long.valueOf((String) s);
            }
        });
        string.put(Float.class, new Function<Object, Object>() {

            @Override
            public Object apply(Object s) {
                return Float.valueOf((String) s);
            }
        });
        string.put(Double.class, new Function<Object, Object>() {

            @Override
            public Object apply(Object s) {
                return Double.valueOf((String) s);
            }
        });
        string.put(Boolean.class, new Function<Object, Object>() {

            @Override
            public Object apply(Object s) {
                return Boolean.valueOf((String) s);
            }
        });
        TRANSLATORS.put(String.class, string);

    }

    public static <T> Optional<T> attempt(Object obj, Class<?> from, Class<T> to) {
        if (!TRANSLATORS.containsKey(from)) {
            return Optional.absent();
        }
        Map<Class<?>, Function<Object, Object>> inner = TRANSLATORS.get(from);
        if (!inner.containsKey(to)) {
            return Optional.absent();
        }
        Function<Object, Object> translator = inner.get(to);
        try {
            return Optional.of(to.cast(translator.apply(obj)));
        } catch (NumberFormatException e) {
            return Optional.absent();
        }
    }
}
