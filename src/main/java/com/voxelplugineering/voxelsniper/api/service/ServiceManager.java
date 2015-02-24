package com.voxelplugineering.voxelsniper.api.service;

import com.google.common.base.Optional;


public interface ServiceManager
{

    void init();
    
    void stop();
    
    void registerServiceProvider(ServiceProvider provider);

    void registerService(String service);
    
    boolean hasService(String service);
    
    Optional<Service> getService(String service);
    
    void stopService(Service service);
    
}
