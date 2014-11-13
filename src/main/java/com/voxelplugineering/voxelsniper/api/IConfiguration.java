package com.voxelplugineering.voxelsniper.api;

public interface IConfiguration
{
    
    void set(String name, Object value);
    
    Object get(String name);
    
    void registerContainer(Object container);

    Class<?> getContainer(String containerName);
    
    Class<?>[] getContainers();
    
}
