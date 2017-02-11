package com.mnm.sense;

import com.google.android.gms.maps.model.LatLng;

public class Visualization
{
    public static final String NONE = "None";
    public static final String TEXT = "Text";
    public static final String MAP = "Map";
    public static final String BAR_CHART = "Bar Chart";
    public static final String PIE_CHART = "Pie Chart";
    public static final String LIST_VIEW = "List View";

    public int rows;
    public int cols;
    public boolean isDisplayed;

    public Visualization(int r, int c, boolean displayed)
    {
        rows = r;
        cols = c;
        isDisplayed = displayed;
    }

    Object data(String type)
    {
        if (type.equals(MAP))
            return new LatLng(43.3, 21.9);
        else if (type.equals(TEXT))
            return "43.3, 21.9";

        return "DATA";
    }
}