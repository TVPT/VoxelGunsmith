package com.voxelplugineering.voxelsniper.api.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class ServiceProvider
{

    private Type type;

    public ServiceProvider(Type type)
    {
        this.type = type;
    }

    public Type getType()
    {
        return this.type;
    }

    public abstract void registerNewServices(ServiceManager manager);

    public static enum Type
    {
        CORE, PLATFORM, EXPANSION;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Builder
    {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface InitHook
    {
        String value();
    }
}
