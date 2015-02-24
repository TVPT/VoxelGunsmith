package com.voxelplugineering.voxelsniper.api.service;


public abstract class AbstractService implements Service
{
    
    private boolean started = false;
    private final int priority;
    
    public AbstractService(int priority)
    {
        this.priority = priority;
    }
    
    @Override
    public int getPriority()
    {
        return this.priority;
    }

    @Override
    public final boolean isStarted()
    {
        return this.started;
    }

    @Override
    public final void start()
    {
        if(this.started)
        {
            throw new IllegalStateException();
        }
        this.started = true;
        init();
    }
    
    protected abstract void init();

    @Override
    public final void stop()
    {
        if(!this.started)
        {
            throw new IllegalStateException("Service " + getName() + " was attempted to be stopped while not started!");
        }
        destroy();
        this.started = false;
    }
    
    protected abstract void destroy();

    public void check()
    {
        if(!this.started)
        {
            throw new IllegalStateException("Tried to perform operation against " + getName() + " while it was stopped.");
        }
    }
    
}
