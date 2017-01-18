package com.mnm.sense.models;

import com.mnm.sense.trackers.Tracker;

public abstract class BaseModel<T>
{
    public Tracker tracker;
    public T data;
    public boolean shouldUpdate = true;

    public BaseModel(Tracker tracker, T data)
    {
        this.tracker = tracker;
        this.data = data;
    }
}
