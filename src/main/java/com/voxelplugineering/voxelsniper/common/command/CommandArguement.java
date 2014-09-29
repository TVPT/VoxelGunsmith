package com.voxelplugineering.voxelsniper.common.command;

public class CommandArguement<T>
{

    T value;
    
    public CommandArguement(T v)
    {
        this.value = v;
    }
    
    public T get()
    {
        return this.value;
    }
    
}
