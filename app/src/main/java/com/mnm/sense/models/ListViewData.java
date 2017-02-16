package com.mnm.sense.models;

import android.graphics.drawable.Drawable;

public class ListViewData
{
    private final String[] names;
    private final Drawable[] icons;
    private final float[] foregroundTimes;
    private final long[] lastTimeUsages;

    public ListViewData()
    {
        this.names = new String[]{};
        this.icons = new Drawable[]{};
        this.foregroundTimes = new float[]{};
        this.lastTimeUsages = new long[]{};
    }

    public ListViewData(String[] names, Drawable[] icons, float[] foregroundTimes, long[] lastTimeUsages)
    {
        this.names = names;
        this.icons = icons;
        this.foregroundTimes = foregroundTimes;
        this.lastTimeUsages = lastTimeUsages;
    }

    public String[] getNames()
    {
        return names;
    }

    public Drawable[] getIcons()
    {
        return icons;
    }

    public String getNameAt(int i)
    {
      return names[i];
    }

    public Drawable getIconAt(int i)
    {
        return icons[i];

    }

    public long getLastTimeUsedAt(int i)
    {
        return lastTimeUsages[i];
    }

    public float getForegroundTimeAt(int i)
    {
        return foregroundTimes[i];
    }

    public float[] getForegroundTimes()
    {
        return foregroundTimes;
    }

    public long[] getLastTimeUsages()
    {
        return lastTimeUsages;
    }
}
