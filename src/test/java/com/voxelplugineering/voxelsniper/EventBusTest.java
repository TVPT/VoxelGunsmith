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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.voxelplugineering.voxelsniper.api.event.EventHandler;
import com.voxelplugineering.voxelsniper.api.event.EventPriority;
import com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy;
import com.voxelplugineering.voxelsniper.api.event.EventThreadingPolicy.ThreadingPolicy;
import com.voxelplugineering.voxelsniper.api.event.bus.EventBus;
import com.voxelplugineering.voxelsniper.event.DeadEvent;
import com.voxelplugineering.voxelsniper.event.Event;
import com.voxelplugineering.voxelsniper.event.bus.AsyncEventBus;
import com.voxelplugineering.voxelsniper.logging.PrintStreamLogger;

/**
 * Tests for the {@link AsyncEventBus} implementation.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Gunsmith.class)
public class EventBusTest
{

    private EventBus bus;

    /**
     * 
     */
    @BeforeClass
    public static void setupClass()
    {
        PrintStreamLogger logger = new PrintStreamLogger(System.out);
        PowerMockito.mockStatic(Gunsmith.class);
        Mockito.when(Gunsmith.getLogger()).thenReturn(logger);
    }

    /**
     * 
     */
    @Before
    public void setup()
    {
        this.bus = new AsyncEventBus();
    }

    /**
     * 
     */
    @Test
    public void basicTest()
    {
        TestHandler handler = new TestHandler();
        this.bus.register(handler);
        this.bus.post(new TestEvent());
        assertEquals(true, handler.found);
        this.bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void priorityTest()
    {
        PriorityHandler handler = new PriorityHandler();
        this.bus.register(handler);
        this.bus.post(new TestEvent());
        assertEquals("abcde", handler.order);
        this.bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void superEventhandling()
    {
        SuperHandler handler = new SuperHandler();
        this.bus.register(handler);
        this.bus.post(new SubEvent());
        assertEquals(3, handler.count);
        this.bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void superEventhandling2()
    {
        SuperHandler handler = new SuperHandler();
        this.bus.register(handler);
        this.bus.post(new TestEvent());
        assertEquals(2, handler.count);
        this.bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void testDeadEvent()
    {
        DeadEventHandler handler = new DeadEventHandler();
        this.bus.register(handler);
        this.bus.post(new TestEvent());
        assertEquals(true, handler.dead);
        this.bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void testNotAsync()
    {
        SyncHandler handler = new SyncHandler();
        this.bus.register(handler);
        SyncEvent e = new SyncEvent();
        this.bus.post(e);
        assertEquals(Thread.currentThread(), handler.thread);
        this.bus.unregister(handler);
    }

    /**
     * 
     */
    @Test
    public void testAsync()
    {
        AsyncHandler handler = new AsyncHandler();
        this.bus.register(handler);
        this.bus.post(new AsyncEvent());
        assertNotEquals(Thread.currentThread(), handler.thread);
        this.bus.unregister(handler);
    }

    //=======================================================================

    /**
     * 
     */
    public static class TestHandler
    {

        protected boolean found = false;

        /**
         * @param event
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
         * @param event
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
         * @param event
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
         * @param event
         */
        @EventHandler(EventPriority.HIGHEST)
        public void onTestEventa(TestEvent event)
        {
            this.order += "a";
        }

        /**
         * @param event
         */
        @EventHandler(EventPriority.HIGH)
        public void onTestEventb(TestEvent event)
        {
            this.order += "b";
        }

        /**
         * @param event
         */
        @EventHandler
        public void onTestEventc(TestEvent event)
        {
            this.order += "c";
        }

        /**
         * @param event
         */
        @EventHandler(EventPriority.LOW)
        public void onTestEventd(TestEvent event)
        {
            this.order += "d";
        }

        /**
         * @param event
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
         * @param event
         */
        @EventHandler
        public void onTestEvent(SubEvent event)
        {
            this.count++;
        }

        /**
         * @param event
         */
        @EventHandler
        public void onTestEvent(TestEvent event)
        {
            this.count++;
        }

        /**
         * @param event
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
         * @param event
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
