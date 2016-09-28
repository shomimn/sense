package com.mnm.sense;


public class DashboardData
{
    public static final int BAR_CHART = 0;
    public static final int TEXT = 1;
    public static final int MAP = 2;

    int type;
    Object data;
    int imageResource;

    DashboardData(int t, int res, Object d)
    {
        type = t;
        imageResource = res;
        data = d;
    }
}
