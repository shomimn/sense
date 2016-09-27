package com.mnm.sense;


import android.util.TypedValue;

public class Util
{
    static int dp(int pixels)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, SenseApp.context().getResources().getDisplayMetrics());
    }
}
