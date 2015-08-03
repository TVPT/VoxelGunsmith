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
package com.voxelplugineering.voxelsniper.service.eventbus;

import static com.google.common.base.Preconditions.checkNotNull;

import com.voxelplugineering.voxelsniper.GunsmithLogger;
import com.voxelplugineering.voxelsniper.config.BaseConfiguration;
import com.voxelplugineering.voxelsniper.service.AbstractService;
import com.voxelplugineering.voxelsniper.service.event.DeadEvent;
import com.voxelplugineering.voxelsniper.service.event.Event;
import com.voxelplugineering.voxelsniper.service.eventbus.EventThreadingPolicy.ThreadingPolicy;
import com.voxelplugineering.voxelsniper.util.Context;
import com.voxelplugineering.voxelsniper.util.FutureFutureCallable;

import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * An {@link EventBus} implementation supporting all {@link ThreadingPolicy} types. Optionally takes
 * an {@link ExecutorService} to use for asynchronous task delegation. <p> This class is safe for
 * concurrent use. </p>
 */
public class AsyncEventBus extends AbstractService implements EventBus
{

    private ListeningExecutorService executor;
    private Map<Class<? extends Event>, SubscriberList> registry;
    private boolean built;
    private boolean explicitExecutor;

    /**
     * Creates a new {@link AsyncEventBus}.
     * 
     * @param context The context
     * @param executorService The {@link ExecutorService} to use for asynchronous task delegation
     */
    public AsyncEventBus(Context context, ExecutorService executorService)
    {
        this(context);
        checkNotNull(executorService);
        this.executor = MoreExecutors.listeningDecorator(executorService);
        this.explicitExecutor = true;
    }

    /**
     * Creates a new {@link AsyncEventBus}. This defaults to using
     * {@link Executors#newCachedThreadPool()} for event handler delegation.
     * 
     * @param context The context
     */
    public AsyncEventBus(Context context)
    {
        super(context);
    }

    @Override
    public void _init()
    {
        if (this.built)
        {
            return;
        }
        this.registry = new MapMaker().concurrencyLevel(4).makeMap();
        if (!this.explicitExecutor)
        {
            final String prefix = BaseConfiguration.eventBusThreadPrefix;
            this.executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(new ThreadFactory()
            {

                private final ThreadGroup group;
                private int count;

                {
                    this.group = Thread.currentThread().getThreadGroup();
                }

                @Override
                public Thread newThread(Runnable r)
                {
                    Thread thr = new Thread(this.group, r, prefix + this.count++);
                    return thr;
                }
            }));
        }
        this.built = true;
    }

    @Override
    public void _shutdown()
    {
        if (!this.built)
        {
            return;
        }
        this.registry = null;
        if (!this.explicitExecutor)
        {
            this.executor = null;
        }

        this.built = false;
    }

    @Override
    public void register(Object eventHandler)
    {
        checkNotNull(eventHandler);
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
    }

    private SubscriberList getListForEventType(Class<? extends Event> event)
    {
        checkNotNull(event);
        if (this.registry.containsKey(event))
        {
            return this.registry.get(event);
        }
        if (event == Event.class)
        {
            SubscriberList list = new SubscriberList();
            this.registry.put(event, list);
            return list;
        }
        @SuppressWarnings("unchecked")
        SubscriberList list = new SubscriberList(getListForEventType((Class<? extends Event>) event.getSuperclass()));
        this.registry.put(event, list);
        return list;
    }

    @Override
    public void unregister(Object eventHandler)
    {
        checkNotNull(eventHandler);
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

    @Override
    public ListenableFuture<Event> post(Event event)
    {
        checkNotNull(event);
        if (event.getThreadingPolicy() == ThreadingPolicy.ASYNCHRONOUS)
        {
            return postAsync(event);
        } else if (event.getThreadingPolicy() == ThreadingPolicy.SYNCHRONIZED)
        {
            return postSync(event);
        } else
        {
            return postAsyncSeq(event);
        }

    }

    private ListenableFuture<Event> postSync(Event event)
    {
        List<Subscriber> subs = getListForEventType(event.getClass()).getOrderedSubscribers();
        if (subs.isEmpty() && !event.getClass().equals(DeadEvent.class))
        {
            post(new DeadEvent(event));
            return Futures.immediateFuture(event);
        }
        for (Subscriber s : subs)
        {
            try
            {
                s.getMethod().invoke(s.getContainer(), event);
            } catch (Exception e)
            {
                GunsmithLogger.getLogger().error(e,
                        "Error executing event handler in " + s.getContainer().getClass().getName() + " " + s.getMethod().getName());
                continue;
            }
        }
        return Futures.immediateFuture(event);
    }

    private ListenableFuture<Event> postAsyncSeq(Event event)
    {
        List<Subscriber> subs = getListForEventType(event.getClass()).getOrderedSubscribers();
        if (subs.isEmpty() && !event.getClass().equals(DeadEvent.class))
        {
            post(new DeadEvent(event));
            return Futures.immediateFuture(event);
        }
        for (Subscriber s : subs)
        {
            try
            {
                this.executor.submit(new EventCallable(event, s)).get();
            } catch (InterruptedException e)
            {
                GunsmithLogger.getLogger().error(e,
                        "Error executing event handler in " + s.getContainer().getClass().getName() + " " + s.getMethod().getName());
                continue;
            } catch (ExecutionException e)
            {
                GunsmithLogger.getLogger().error(e,
                        "Error executing event handler in " + s.getContainer().getClass().getName() + " " + s.getMethod().getName());
                continue;
            }
        }
        return Futures.immediateFuture(event);
    }

    private ListenableFuture<Event> postAsync(Event event)
    {
        List<Subscriber> subs = getListForEventType(event.getClass()).getOrderedSubscribers();
        if (subs.isEmpty() && !event.getClass().equals(DeadEvent.class))
        {
            post(new DeadEvent(event));
            return Futures.immediateFuture(event);
        }
        List<Future<Event>> futures = Lists.newArrayList();
        for (Subscriber s : subs)
        {
            futures.add(this.executor.submit(new EventCallable(event, s)));
        }
        return this.executor.submit(new FutureFutureCallable(futures, event));
    }

    /**
     * A {@link Callable} for posting an event to a subscriber.
     */
    private static class EventCallable implements Callable<Event>
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

}
