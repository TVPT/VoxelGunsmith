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
package com.voxelplugineering.voxelsniper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.voxelplugineering.voxelsniper.api.event.EventHandler;
import com.voxelplugineering.voxelsniper.api.event.EventPriority;
import com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy;
import com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy.ThreadingPolicy;
import com.voxelplugineering.voxelsniper.api.service.event.EventBus;
import com.voxelplugineering.voxelsniper.core.service.ConfigurationService;
import com.voxelplugineering.voxelsniper.core.service.eventbus.AsyncEventBus;
import com.voxelplugineering.voxelsniper.core.service.eventbus.DeadEvent;
import com.voxelplugineering.voxelsniper.core.service.eventbus.Event;
import com.voxelplugineering.voxelsniper.util.ContextTestUtil;

/**
 * Tests for the {@link AsyncEventBus} implementation.
 */
public class EventBusTest
{

    /**
     * 
     */
    @Test
    public void basicTest()
    {
        EventBus bus = new AsyncEventBus(ContextTestUtil.create(new ConfigurationService(ContextTestUtil.create())));
        bus.start();

        TestHandler handler = new TestHandler();
        bus.register(handler);
        bus.post(new TestEvent());
        assertEquals(true, handler.found);
        bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void priorityTest()
    {
        EventBus bus = new AsyncEventBus(ContextTestUtil.create(new ConfigurationService(ContextTestUtil.create())));
        bus.start();

        PriorityHandler handler = new PriorityHandler();
        bus.register(handler);
        bus.post(new TestEvent());
        assertEquals("abcde", handler.order);
        bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void superEventhandling()
    {
        EventBus bus = new AsyncEventBus(ContextTestUtil.create(new ConfigurationService(ContextTestUtil.create())));
        bus.start();

        SuperHandler handler = new SuperHandler();
        bus.register(handler);
        bus.post(new SubEvent());
        assertEquals(3, handler.count);
        bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void superEventhandling2()
    {
        EventBus bus = new AsyncEventBus(ContextTestUtil.create(new ConfigurationService(ContextTestUtil.create())));
        bus.start();

        SuperHandler handler = new SuperHandler();
        bus.register(handler);
        bus.post(new TestEvent());
        assertEquals(2, handler.count);
        bus.unregister(handler);
    }

    /**
     * @throws InterruptedException if error
     */
    @Test
    public void testDeadEvent() throws InterruptedException
    {
        EventBus bus = new AsyncEventBus(ContextTestUtil.create(new ConfigurationService(ContextTestUtil.create())));
        bus.start();

        DeadEventHandler handler = new DeadEventHandler();
        bus.register(handler);
        bus.post(new TestEvent());
        // This timeout is required as AsyncEventBus#post will return
        // immediately upon posting the dead event,
        // so we must wait for the dead event to be handled.
        long timeout = 1000;
        while (timeout > 0 && !handler.dead)
        {
            Thread.sleep(50);
            timeout -= 50;
        }
        assertEquals(true, handler.dead);
        bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void testNotAsync()
    {
        EventBus bus = new AsyncEventBus(ContextTestUtil.create(new ConfigurationService(ContextTestUtil.create())));
        bus.start();

        SyncHandler handler = new SyncHandler();
        bus.register(handler);
        SyncEvent e = new SyncEvent();
        bus.post(e);
        assertEquals(Thread.currentThread(), handler.thread);
        bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void testAsync()
    {
        EventBus bus = new AsyncEventBus(ContextTestUtil.create(new ConfigurationService(ContextTestUtil.create())));
        bus.start();

        AsyncHandler handler = new AsyncHandler();
        bus.register(handler);
        bus.post(new AsyncEvent());
        assertNotEquals(Thread.currentThread(), handler.thread);
        bus.unregister(handler);
    }

    /**
     * 
     */
    public static class TestHandler
    {

        protected boolean found = false;

        /**
         * @param event The event
         */
        @EventHandler
        public void onTestEvent(TestEvent event)
        {
            this.found = true;
        }

    }

    /**
     * 
     */
    public static class AsyncHandler
    {

        protected Thread thread = null;

        /**
         * @param event The event
         */
        @EventHandler
        public void onTestEvent(AsyncEvent event)
        {
            this.thread = Thread.currentThread();
        }

    }

    /**
     * 
     */
    public static class SyncHandler
    {

        protected Thread thread = null;

        /**
         * @param event The event
         */
        @EventHandler
        public void onTestEvent(SyncEvent event)
        {
            this.thread = Thread.currentThread();
        }
    }

    /**
     * 
     */
    public static class PriorityHandler
    {

        protected String order = "";

        /**
         * @param event The event
         */
        @EventHandler(EventPriority.HIGHEST)
        public void onTestEventa(TestEvent event)
        {
            this.order += "a";
        }

        /**
         * @param event The event
         */
        @EventHandler(EventPriority.HIGH)
        public void onTestEventb(TestEvent event)
        {
            this.order += "b";
        }

        /**
         * @param event The event
         */
        @EventHandler
        public void onTestEventc(TestEvent event)
        {
            this.order += "c";
        }

        /**
         * @param event The event
         */
        @EventHandler(EventPriority.LOW)
        public void onTestEventd(TestEvent event)
        {
            this.order += "d";
        }

        /**
         * @param event The event
         */
        @EventHandler(EventPriority.LOWEST)
        public void onTestEvente(TestEvent event)
        {
            this.order += "e";
        }
    }

    /**
     * 
     */
    public static class SuperHandler
    {

        protected int count = 0;

        /**
         * @param event The event
         */
        @EventHandler
        public void onTestEvent(SubEvent event)
        {
            this.count++;
        }

        /**
         * @param event The event
         */
        @EventHandler
        public void onTestEvent(TestEvent event)
        {
            this.count++;
        }

        /**
         * @param event The event
         */
        @EventHandler
        public void onTestEvent(Event event)
        {
            this.count++;
        }
    }

    /**
     * 
     */
    public static class DeadEventHandler
    {

        protected boolean dead = false;

        /**
         * @param event The event
         */
        @EventHandler
        public void onDeadEvent(DeadEvent event)
        {
            this.dead = true;
        }
    }

    /**
     * 
     */
    public static class TestEvent extends Event
    {

    }

    /**
     * 
     */
    public static class SubEvent extends TestEvent
    {

    }

    /**
     * 
     */
    @EventThreadingPolicy(ThreadingPolicy.ASYNCHRONOUS)
    public static class AsyncEvent extends Event
    {

    }

    /**
     * 
     */
    @EventThreadingPolicy(ThreadingPolicy.SYNCHRONIZED)
    public static class SyncEvent extends Event
    {

    }

}
