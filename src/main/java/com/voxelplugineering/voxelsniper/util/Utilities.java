package com.voxelplugineering.voxelsniper.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Utilities
{
    /**
     * This method uses a region to check case-insensitive equality. This
     * means the internal array does not need to be copied like a
     * toLowerCase() call would.
     *
     * @param string String to check
     * @param prefix Prefix of string to compare
     * @return true if provided string starts with, ignoring case, the prefix
     * provided
     * @throws NullPointerException     if prefix is null
     * @throws IllegalArgumentException if string is null
     */
    public static boolean startsWithIgnoreCase(final String string, final String prefix) throws IllegalArgumentException, NullPointerException
    {
        checkNotNull(string, "Cannot check a null string for a match");
        if (string.length() < prefix.length())
        {
            return false;
        }
        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    /**
     * Copies all elements from the iterable collection of originals to the
     * collection provided.
     *
     * @param token      String to search for
     * @param originals  An iterable collection of strings to filter.
     * @param collection The collection to add matches to
     * @return the collection provided that would have the elements copied
     * into
     * @throws UnsupportedOperationException if the collection is immutable
     *                                       and originals contains a string which starts with the specified
     *                                       search string.
     * @throws IllegalArgumentException      if any parameter is is null
     * @throws IllegalArgumentException      if originals contains a null element.
     *                                       <b>Note: the collection may be modified before this is thrown</b>
     */
    public static <T extends Collection<? super String>> T copyPartialMatches(final String token, final Iterable<String> originals, final T collection) throws UnsupportedOperationException, IllegalArgumentException
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

    public static List<String> findMatches(String partial, List<String> candidates)
    {
        if (partial == null || partial.isEmpty()) return candidates;

        List<String> ret = new ArrayList<String>();
        copyPartialMatches(partial, candidates, ret);
        return ret;
    }
}
