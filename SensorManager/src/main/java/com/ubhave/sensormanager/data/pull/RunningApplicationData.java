package com.ubhave.sensormanager.data.pull;


import android.graphics.drawable.Drawable;

import java.util.concurrent.TimeUnit;

public class RunningApplicationData
{
    private String name;
    private long foregroundTime;
    private Drawable icon;
    private long lastTimeUsed;
    private String packageName;

    public RunningApplicationData(String packageName, String n, long ft, Drawable i, long ltu)
    {
        this.packageName = packageName;
        this.name = n;
        this.foregroundTime = ft;
        this.icon = i;
        this.lastTimeUsed = ltu;
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
        return TimeUnit.MILLISECONDS.toMinutes(foregroundTime);
    }

    public Drawable getIcon()
    {
        return icon;
    }

    public long getLastTimeUsed()
    {
        return lastTimeUsed;
    }

    public void setForegroundTime(long foregroundTime)
    {
        this.foregroundTime = foregroundTime;
    }

    public void setLastTimeUsed(long lastTimeUsed)
    {
        this.lastTimeUsed = lastTimeUsed;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setIcon(Drawable i)
    {
        icon = i;
    }
}
