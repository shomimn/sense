package com.mnm.sense.models;

import android.graphics.drawable.Drawable;

public class ListViewData
{
    private final String[] names;
    private final Drawable[] icons;

    public ListViewData(String[] names, Drawable[] icons)
    {
        this.names = names;
        this.icons = icons;
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
}
