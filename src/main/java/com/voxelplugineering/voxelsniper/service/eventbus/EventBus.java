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

import com.voxelplugineering.voxelsniper.service.Service;
import com.voxelplugineering.voxelsniper.service.event.DeadEvent;
import com.voxelplugineering.voxelsniper.service.event.Event;
import com.voxelplugineering.voxelsniper.service.eventbus.EventThreadingPolicy.Policy;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * An event bus handles the posting of events and the registration of handlers for events.
 * 
 * <p> Supports priority based event handling where event subscribers are executed in order of
 * priority. This replaces guava's EventBus in order to provide subscriber prioritization and
 * greater flexibility for event handler threading options. </p>
 * 
 * <h2>Receiving Events</h2>
 * 
 * <p> To receive events from this EventBus you: </p>
 * 
 * <ol> <li>first expose a public, non-static method which accepts a single parameter of the
 * {@link Event} type that you wish to listen for.</li> <li>Annotate the method with the
 * {@link EventHandler} annotation which takes an optional parameter for the {@link EventPriority}
 * .</li> <li>Pass the object containing your method to the EventBus's {@link #register(Object)}
 * method.</li> </ol>
 * 
 * <p> Event handlers should not typically throw any Exceptions. Any thrown exceptions will be
 * caught and reported by the EventBus. </p>
 * 
 * <p> Event handlers for {@link Policy#SYNCHRONIZED} events should attempt to return as quickly as
 * possibly, delegating and/or queuing additional threads to perform long running or intensive
 * tasks. </p>
 * 
 * <h2>Posting Events</h2>
 * 
 * <p> To post an event you pass the event object to the EventBus's {@link #post(Event)} method.
 * </p>
 * 
 * <p> The EventBus will accumulate all event handlers handling any class for which the event is
 * <em>assignable</em> (eg. All superclasses, superinterfaces, and implemented interfaces of the
 * event class or any superclass). These EventHandlers are then sorted by their
 * {@link EventPriority}. </p>
 * 
 * <p> The ordering of handlers within an {@link EventPriority} level is undefined. </p>
 * 
 * <p> Posting events with a {@link Policy} of {@link Policy#SYNCHRONIZED} or
 * {@link Policy#ASYNCHRONOUS_SEQUENTIAL} will wait until all event subscribers have been executed
 * before returning. While posting an event with {@link Policy#ASYNCHRONOUS} will return immediately
 * without waiting. </p>
 */
public interface EventBus extends Service
{

    /**
     * Registers all methods within the given object annotated by {@link EventHandler}.
     * 
     * @param eventHandler The event handler
     */
    void register(Object eventHandler);

    /**
     * Unregisters all event handlers within the given object.
     * 
     * @param eventHandler The event handler
     */
    void unregister(Object eventHandler);

    /**
     * Posts the given event to the event bus. Depending on the {@link Policy} of the event this
     * method may return immediately or wait for all handlers to finish executing.
     * 
     * <p> If no handlers have been registered for this event, and it is not already a
     * {@link DeadEvent}, then it will be wrapped in a DeadEvent and reposted. </p>
     * 
     * @param event The event to post
     * @return A future for the task
     */
    ListenableFuture<Event> post(Event event);

}
