package com.voxelplugineering.voxelsniper.api.service;


public interface Service
{

    String getName();
    
    int getPriority();

    boolean isStarted();
    
    void start();
    
    void stop();
    
}
