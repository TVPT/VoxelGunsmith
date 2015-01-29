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
package com.voxelplugineering.voxelsniper.event.bus;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.api.event.EventPriority;

/**
 * A priority grouped list of subscribers.
 */
public class SubscriberList
{

    private final Map<EventPriority, List<Subscriber>> subs;
    private final SubscriberList parent;

    /**
     * Creates a new {@link SubscriberList}.
     * 
     * @param parent The parent list
     */
    public SubscriberList(SubscriberList parent)
    {
        this.subs = Maps.newEnumMap(EventPriority.class);
        this.parent = parent;
    }

    /**
     * Creates a new {@link SubscriberList} with no parent.
     */
    public SubscriberList()
    {
        this(null);
    }

    /**
     * Registers a new {@link Subscriber} with this list.
     * 
     * @param sub The new subscriber
     */
    public void register(Subscriber sub)
    {
        getOrCreateList(sub.getPriority()).add(sub);
    }

    /**
     * Unsubscribes the given subscriber from the list.
     * 
     * @param sub The subscriber
     */
    public synchronized void unregister(Subscriber sub)
    {
        if (this.subs.containsKey(sub.getPriority()))
        {
            this.subs.get(sub.getPriority()).remove(sub);
        }
    }

    /**
     * Registers a group of subscribers to this list.
     * 
     * @param list The subscriber group
     */
    public void register(Iterable<Subscriber> list)
    {
        for (Subscriber s : list)
        {
            register(s);
        }
    }

    private synchronized List<Subscriber> getOrCreateList(EventPriority p)
    {
        List<Subscriber> list;
        if (!this.subs.containsKey(p))
        {
            list = Lists.newArrayList();
            this.subs.put(p, list);
        } else
        {
            list = this.subs.get(p);
        }
        return list;
    }

    /**
     * Gets a list of subscribers ordered by priority. The ordering within
     * priority groups is undefined.
     * 
     * @return The list
     */
    public List<Subscriber> getOrderedSubscribers()
    {
        List<Subscriber> list = Lists.newArrayList();
        for (EventPriority p : EventPriority.values())
        {
            appendPriority(list, p);
        }
        return list;
    }

    private void appendPriority(List<Subscriber> list, EventPriority p)
    {
        if (this.subs.containsKey(p))
        {
            list.addAll(this.subs.get(p));
        }
        if (this.parent != null)
        {
            this.parent.appendPriority(list, p);
        }
    }

}
