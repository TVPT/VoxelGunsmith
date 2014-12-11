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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * Common utilities class for Gunsmith
 */
public class Utilities
{
    /**
     * This method uses a region to check case-insensitive equality. This means the internal array does not need to be copied like a toLowerCase()
     * call would.
     *
     * @param string String to check
     * @param prefix Prefix of string to compare
     * @return true if provided string starts with, ignoring case, the prefix provided
     * @throws NullPointerException if prefix is null
     * @throws IllegalArgumentException if string is null
     */
    public static boolean startsWithIgnoreCase(final String string, final String prefix) throws IllegalArgumentException, NullPointerException
    {
        checkNotNull(string, "Cannot check a null string for a match");
        return string.length() >= prefix.length() && string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    /**
     * Copies all elements from the iterable collection of originals to the collection provided.
     *
     * @param token String to search for
     * @param originals An iterable collection of strings to filter.
     * @param collection The collection to add matches to
     * @param <T> the collection to use
     * @return the collection provided that would have the elements copied into
     * @throws UnsupportedOperationException if the collection is immutable and originals contains a string which starts with the specified search
     *             string.
     * @throws IllegalArgumentException if any parameter is is null
     * @throws IllegalArgumentException if originals contains a null element. <b>Note: the collection may be modified before this is thrown</b>
     */
    public static <T extends Collection<? super String>> T
            copyPartialMatches(final String token, final Iterable<String> originals, final T collection) throws UnsupportedOperationException,
                    IllegalArgumentException
    {
        checkNotNull(token, "Search token cannot be null");
        checkNotNull(collection, "Collection cannot be null");
        checkNotNull(originals, "Originals cannot be null");

        for (String string : originals)
        {
            if (startsWithIgnoreCase(string, token))
            {
                collection.add(string);
            }
        }

        return collection;
    }

    /**
     * Finds all matching strings in the given list of candidates.
     *
     * @param partial the partial to look for
     * @param candidates the candidates to parse
     * @return a new list of partial matches
     */
    public static List<String> findMatches(String partial, List<String> candidates)
    {
        if (partial == null || partial.isEmpty())
        {
            return candidates;
        }

        List<String> ret = new ArrayList<String>();
        copyPartialMatches(partial, candidates, ret);
        return ret;
    }

    /**
     * Returns the array positions between start and end (both inclusive) concatenated as a space separated string.
     * 
     * @param array the array
     * @param start the start point (inclusive)
     * @param end the end point (inclusive)
     * @return the concatenated section
     */
    public static String getSection(String[] array, int start, int end)
    {
        checkNotNull(array, "Array cannot be null.");
        checkArgument(start >= 0, "Start point cannot be negative.");
        checkArgument(end >= start, "End point cannot be before start point.");
        checkArgument(array.length > end, "End point must be less than the array length.");

        String ret = "";
        for (int i = start; i <= end; i++)
        {
            ret += array[i];
            if (i < end)
            {
                ret += " ";
            }
        }

        return ret;
    }

    /**
     * Returns an array which is equivalent to source[o:start-1] + insert[o:length] + source[end+1:length].
     * 
     * @param source the source array
     * @param insert the array to insert
     * @param start the start point (inclusive)
     * @param end the end point (inclusive)
     * @return the result array
     */
    public static String[] replaceSection(String[] source, String[] insert, int start, int end)
    {
        checkNotNull(source, "Source cannot be null.");
        checkNotNull(insert, "Insert cannot be null.");
        checkArgument(start >= 0, "Start point cannot be negative.");
        checkArgument(end >= start, "End point cannot be before start point.");
        checkArgument(source.length > end, "End point must be less than the array length.");

        String[] ret = new String[source.length - (end - start + 1) + insert.length];
        int i = 0;

        for (; i < start; i++)
        {
            ret[i] = source[i];
        }
        for (int o = 0; o < insert.length; o++)
        {
            ret[i++] = insert[o];
        }
        for (int o = end + 1; o < source.length; o++)
        {
            ret[i++] = source[o];
        }
        
        return ret;
    }
}
