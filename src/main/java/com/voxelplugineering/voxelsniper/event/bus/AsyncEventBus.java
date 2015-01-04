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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.voxelplugineering.voxelsniper.Gunsmith;
import com.voxelplugineering.voxelsniper.api.event.EventHandler;
import com.voxelplugineering.voxelsniper.api.event.EventPriority;
import com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy.ThreadingPolicy;
import com.voxelplugineering.voxelsniper.api.event.bus.EventBus;
import com.voxelplugineering.voxelsniper.event.DeadEvent;
import com.voxelplugineering.voxelsniper.event.Event;

/**
 * An {@link EventBus} implementation supporting all {@link ThreadingPolicy} types. Optionally takes an {@link ExecutorService} to use for
 * asynchronous task delegation.
 * <p>
 * This class is safe for concurrent use.
 * </p>
 */
public class AsyncEventBus implements EventBus
{

    private final ExecutorService executor;
    private final Map<Class<? extends Event>, SubscriberList> registry;

    /**
     * Creates a new {@link AsyncEventBus}.
     * 
     * @param executorService The {@link ExecutorService} to use for asynchronous task delegation
     */
    public AsyncEventBus(ExecutorService executorService)
    {
        this.executor = executorService;
        this.registry = new MapMaker().concurrencyLevel(4).makeMap();
    }

    /**
     * Creates a new {@link AsyncEventBus}. This defaults to using {@link Executors#newCachedThreadPool()} for event handler delegation.
     */
    public AsyncEventBus()
    {
        this(Executors.newCachedThreadPool());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(Object eventHandler)
    {
        Class<?> cls = eventHandler.getClass();
        List<Subscriber> found = Lists.newArrayList();
        for (Method m : cls.getDeclaredMethods())
        {
            if (m.isAnnotationPresent(EventHandler.class))
            {
                Class<?>[] parameters = m.getParameterTypes();
                if (parameters.length == 1 && Event.class.isAssignableFrom(parameters[0]))
                {
                    EventPriority pri = m.getAnnotation(EventHandler.class).value();
                    @SuppressWarnings("unchecked")
                    Subscriber s = new Subscriber(eventHandler, m, (Class<? extends Event>) parameters[0], pri);
                    found.add(s);
                }
            }
        }

        for (Subscriber s : found)
        {
            registerSubscriber(s);
        }
    }

    private void registerSubscriber(Subscriber s)
    {
        SubscriberList list = getListForEventType(s.getEventType());
        list.register(s);
        //System.out.println("Registered " + s.getMethod().getName() + " for " + s.getEventType().getName() + " with priority " + s.getPriority().name());
    }

    private SubscriberList getListForEventType(Class<? extends Event> event)
    {
        if (this.registry.containsKey(event))
        {
            return this.registry.get(event);
        }
        if (event == Event.class)
        {
            SubscriberList list = new SubscriberList();
            this.registry.put(event, list);
            return list;
        } else
        {
            @SuppressWarnings("unchecked")
            SubscriberList list = new SubscriberList(getListForEventType((Class<? extends Event>) event.getSuperclass()));
            this.registry.put(event, list);
            return list;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister(Object eventHandler)
    {
        Class<?> cls = eventHandler.getClass();
        List<Subscriber> found = Lists.newArrayList();
        for (Method m : cls.getDeclaredMethods())
        {
            if (m.isAnnotationPresent(EventHandler.class))
            {
                Class<?>[] parameters = m.getParameterTypes();
                if (parameters.length == 1 && Event.class.isAssignableFrom(parameters[0]))
                {
                    EventPriority pri = m.getAnnotation(EventHandler.class).value();
                    @SuppressWarnings("unchecked")
                    Subscriber s = new Subscriber(eventHandler, m, (Class<? extends Event>) parameters[0], pri);
                    found.add(s);
                }
            }
        }

        for (Subscriber s : found)
        {
            unregisterSubscriber(s);
        }
    }

    private void unregisterSubscriber(Subscriber s)
    {
        SubscriberList list;
        if (this.registry.containsKey(s.getEventType()))
        {
            list = getListForEventType(s.getEventType());
            list.unregister(s);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void post(Event event)
    {
        if (event.getThreadingPolicy() == ThreadingPolicy.ASYNCHRONOUS)
        {
            postAsync(event);
        } else if (event.getThreadingPolicy() == ThreadingPolicy.SYNCHRONIZED)
        {
            postSync(event);
        } else
        {
            postAsyncSeq(event);
        }

    }

    private void postSync(Event event)
    {
        List<Subscriber> subs = getListForEventType(event.getClass()).getOrderedSubscribers();
        if (subs.isEmpty() && !event.getClass().equals(DeadEvent.class))
        {
            post(new DeadEvent(event));
            return;
        }
        for (Subscriber s : subs)
        {
            try
            {
                s.getMethod().invoke(s.getContainer(), event);
            } catch (Exception e)
            {
                Gunsmith.getLogger().error(e,
                        "Error executing event handler in " + s.getContainer().getClass().getName() + " " + s.getMethod().getName());
                continue;
            }
        }
    }

    private void postAsyncSeq(Event event)
    {
        List<Subscriber> subs = getListForEventType(event.getClass()).getOrderedSubscribers();
        if (subs.isEmpty() && !event.getClass().equals(DeadEvent.class))
        {
            post(new DeadEvent(event));
            return;
        }
        for (Subscriber s : subs)
        {
            try
            {
                this.executor.submit(new EventCallable(event, s)).get();
            } catch (InterruptedException e)
            {
                Gunsmith.getLogger().error(e,
                        "Error executing event handler in " + s.getContainer().getClass().getName() + " " + s.getMethod().getName());
                continue;
            } catch (ExecutionException e)
            {
                Gunsmith.getLogger().error(e,
                        "Error executing event handler in " + s.getContainer().getClass().getName() + " " + s.getMethod().getName());
                continue;
            }
        }
    }

    private void postAsync(Event event)
    {
        List<Subscriber> subs = getListForEventType(event.getClass()).getOrderedSubscribers();
        if (subs.isEmpty() && !event.getClass().equals(DeadEvent.class))
        {
            post(new DeadEvent(event));
            return;
        }
        for (Subscriber s : subs)
        {
            this.executor.submit(new EventCallable(event, s));
        }
    }

}

class EventCallable implements Callable<Event>
{
    private final Event event;
    private final Subscriber sub;

    public EventCallable(Event e, Subscriber s)
    {
        this.event = e;
        this.sub = s;
    }

    @Override
    public Event call() throws Exception
    {
        this.sub.getMethod().invoke(this.sub.getContainer(), this.event);
        return this.event;
    }

}

class Subscriber
{

    private final Object container;
    private final Method exec;
    private final Class<? extends Event> eventType;
    private final EventPriority priority;

    public Subscriber(Object container, Method exec, Class<? extends Event> eventType, EventPriority priority)
    {
        this.container = container;
        this.exec = exec;
        this.eventType = eventType;
        this.priority = priority;
        this.exec.setAccessible(true);
    }

    public Object getContainer()
    {
        return this.container;
    }

    public Method getMethod()
    {
        return this.exec;
    }

    public Class<? extends Event> getEventType()
    {
        return this.eventType;
    }

    public EventPriority getPriority()
    {
        return this.priority;
    }
}

class SubscriberList
{
    private final Map<EventPriority, List<Subscriber>> subs;
    private final SubscriberList parent;

    public SubscriberList(SubscriberList parent)
    {
        this.subs = Maps.newEnumMap(EventPriority.class);
        this.parent = parent;
    }

    public SubscriberList()
    {
        this(null);
    }

    public void register(Subscriber s)
    {
        getOrCreateList(s.getPriority()).add(s);
    }

    public synchronized void unregister(Subscriber s)
    {
        if (this.subs.containsKey(s.getPriority()))
        {
            this.subs.get(s.getPriority()).remove(s);
        }
    }

    public void register(Iterable<Subscriber> list)
    {
        for (Subscriber s : list)
        {
            register(s);
        }
    }

    public synchronized List<Subscriber> getOrCreateList(EventPriority p)
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
