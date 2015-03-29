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
package com.voxelplugineering.voxelsniper.api.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A provider of services. May annotate methods with several annotations for
 * various hooks.
 * <p>
 * <strong>@Builder(value=[name])</strong> - A hook for providing the
 * implementation of a service. Only one builder per service will be called
 * which will be chosen by the highest builder available in order of Core then
 * Platform then Expansion. At this time the ordering within each level is
 * undefined, but a priority system may be added if required.
 * </p>
 * <p>
 * This annotation must annotate an instance method with the signature of
 * <code>public Service methodName();</code>. Key parts are that the method is
 * public, not static, has a return type of {@link Service}, and has no
 * parameters.
 * </p>
 * <p>
 * <strong>@InitHook(value=[name])</strong> - A hook called when a service is
 * initialized. Multiple initialization hooks may exist for each service.
 * Initialization hooks will be called in order of Core then Platform then
 * Expansion. At this time the ordering within each level is undefined, but a
 * priority system may be added if required.
 * </p>
 * <p>
 * This annotation must annotate an instance method with the signature of
 * <code>public void methodName(Service service);</code>. Key parts are that the
 * method is public, not static, and has a single parameter with the type
 * {@link Service}.
 * </p>
 * <p>
 * <strong>@PreInit</strong> - A hook called before the initialization of
 * services begins. The ordering of PreInit hooks is undefined.
 * </p>
 * <p>
 * This annotation must annotate an instance method with the signature of
 * <code>public void methodName();</code>. Key parts are that the method is
 * public, not static, and has no parameters.
 * </p>
 * <p>
 * <strong>@PostInit</strong> - A hook called after all services have been built
 * and initialized. The ordering of PostInit hooks is undefined. The annotated
 * method should follow the same signature guidelines as @Preinit.
 * </p>
 * <p>
 * <strong>@PreStop</strong> - A hook called after the shutdown sequence has
 * begun, but before any services have been stopped. The ordering of PreStop
 * hooks is undefined. The annotated method should follow the same signature
 * guidelines as @Preinit.
 * </p>
 */
public abstract class ServiceProvider
{

    private Type type;

    /**
     * Initializes this {@link ServiceProvider}. The given type will affect the
     * priority that the methods within this provider are called. The typical
     * ordering is Core then Platform then Expansion.
     * 
     * @param type The provider type
     */
    public ServiceProvider(Type type)
    {
        this.type = type;
    }

    /**
     * Gets the {@link Type} of this provider.
     * 
     * @return The type
     */
    public Type getType()
    {
        return this.type;
    }

    /**
     * A call for this provider to register all new services. Services must be
     * registered with the {@link ServiceManager} before they can be referenced
     * by Builder or InitHook annotated methods.
     * 
     * @param manager The manager to register services with
     */
    public abstract void registerNewServices(ServiceManager manager);

    /**
     * Represents various provider types.
     */
    public static enum Type
    {
        /**
         * The core of Gunsmith.
         */
        CORE,
        /**
         * Platforms are special expansions which implement or proxy gunsmith's
         * core into a specific platform.
         */
        PLATFORM,
        /**
         * Expansions are additional plugins or expansions which hook into
         * gunsmith to add or expand functionality.
         */
        EXPANSION;
    }

    /**
     * Annotates a method which will 'build' a service. The method will provide
     * an instance of a type implementing Service.
     * <p>
     * This annotation must annotate an instance method with the signature of
     * <code>public Service methodName();</code>. Key parts are that the method
     * is public, not static, has a return type of {@link Service}, and has no
     * parameters.
     * </p>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Builder
    {

        /**
         * The name of the service that this method will build.
         * 
         * @return The name
         */
        String value();
    }

    /**
     * Annotates a method to be called during the initialization phase of a
     * service.
     * <p>
     * This annotation must annotate an instance method with the signature of
     * <code>public void methodName(Service service);</code>. Key parts are that
     * the method is public, not static, and has a single parameter with the
     * type {@link Service}.
     * </p>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface InitHook
    {

        /**
         * The name of the service that this method is targeting.
         * 
         * @return The name
         */
        String value();
    }

    /**
     * Annotates a method to be called after all services have finished
     * initialization.
     * <p>
     * This annotation must annotate an instance method with the signature of
     * <code>public void methodName();</code>. Key parts are that the method is
     * public, not static, and has no parameters.
     * </p>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface PostInit
    {
    }

    /**
     * Annotates a method to be called before all services have finished
     * initialization.
     * <p>
     * This annotation must annotate an instance method with the signature of
     * <code>public void methodName();</code>. Key parts are that the method is
     * public, not static, and has no parameters.
     * </p>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface PreInit
    {
    }

    /**
     * Annotates a method to be called just before all services begin to stop.
     * <p>
     * This annotation must annotate an instance method with the signature of
     * <code>public void methodName();</code>. Key parts are that the method is
     * public, not static, and has no parameters.
     * </p>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface PreStop
    {
    }
}
