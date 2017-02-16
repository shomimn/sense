package com.ubhave.sensormanager.data.pull;


import android.graphics.drawable.Drawable;

public class RunningApplicationData
{
    private String name;
    private long foregroundTime;
    private Drawable icon;
    private long lastTimeUsed;
    private long beginningTimeRange;
    private long endTimeRange;

    public RunningApplicationData(String n, long ft, Drawable i, long ltu, long btr, long etr)
    {
        this.name = n;
        this.foregroundTime = ft;
        this.icon = i;
        this.lastTimeUsed = ltu;
        this.beginningTimeRange = btr;
        this.endTimeRange = etr;
    }

    public String getName()
    {
        return name;
    }

    public long getForegroundTime()
    {
        return foregroundTime;
    }

    public float getForegroundTimeMins()
    {
        return (float)foregroundTime / 60f / 60f;
    }

    public Drawable getIcon()
    {
        return icon;
    }

    public long getLastTimeUsed()
    {
        return lastTimeUsed;
    }

    public long getBeginningTimeRange()
    {
        return beginningTimeRange;
    }

    public long getEndTimeRange()
    {
        return endTimeRange;
    }

    public void setForegroundTime(long foregroundTime)
    {
        this.foregroundTime = foregroundTime;
    }
}
