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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Common string utilities class for Gunsmith.
 */
public final class StringUtilities
{

    private StringUtilities()
    {

    }

    /**
     * This method uses a region to check case-insensitive equality. This means the internal array
     * does not need to be copied like a toLowerCase() call would.
     * 
     * @param string String to check
     * @param prefix Prefix of string to compare
     * @return True if provided string starts with, ignoring case, the prefix provided
     * @throws NullPointerException If prefix is null
     * @throws IllegalArgumentException If string is null
     */
    public static boolean startsWithIgnoreCase(final String string, final String prefix)
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
     * @return The collection provided that would have the elements copied into
     * @throws UnsupportedOperationException If the collection is immutable and originals contains a
     *             string which starts with the specified search string.
     * @throws IllegalArgumentException If any parameter is is null
     * @throws IllegalArgumentException If originals contains a null element. <b>Note: the
     *             collection may be modified before this is thrown</b>
     */
    public static <T extends Collection<? super String>> T copyPartialMatches(final String token, final Iterable<String> originals,
            final T collection)
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
     * @param partial The partial to look for
     * @param candidates The candidates to parse
     * @return A new list of partial matches
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
     * Returns the array positions between start and end (both inclusive) concatenated as a space
     * separated string.
     * 
     * @param array The array
     * @param start The start point (inclusive)
     * @param end The end point (inclusive)
     * @return The concatenated section
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
     * Returns an array which is equivalent to source[o:start-1] + insert[o:length] +
     * source[end+1:length].
     * 
     * @param source The source array
     * @param insert The array to insert
     * @param start The start point (inclusive)
     * @param end The end point (inclusive)
     * @return The result array
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

    /**
     * Joins all elements of the string with the given deliminator.
     * 
     * @param array The array
     * @param delim The deliminator
     * @return The joined string
     */
    public static String join(String[] array, String delim)
    {
        String result = "";
        for (int i = 0; i < array.length; i++)
        {
            result += array[i];
            if (i < array.length - 1)
            {
                result += delim;
            }
        }
        return result;
    }

}
