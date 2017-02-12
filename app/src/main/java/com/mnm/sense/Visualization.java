package com.mnm.sense;

import com.google.android.gms.maps.model.LatLng;

public class Visualization
{
    public static final String NONE = "None";
    public static final String TEXT = "Text";
    public static final String MAP = "Map";
    public static final String BAR_CHART = "Bar Chart";
    public static final String PIE_CHART = "Pie Chart";
    public static final String LINE_CHART = "Line Chart";

    public int rows;
    public int cols;
    public boolean isDisplayed;

    public Visualization(int r, int c, boolean displayed)
    {
        rows = r;
        cols = c;
        isDisplayed = displayed;
    }
}