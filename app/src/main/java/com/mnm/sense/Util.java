package com.mnm.sense;


import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.support.v4.widget.CompoundButtonCompat;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


public class Util
{
    public static int dp(int pixels)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, SenseApp.context().getResources().getDisplayMetrics());
    }

    public static void setAccent(TextView view, int accent)
    {
        view.setTextColor(view.getResources().getColor(accent));
    }

    public static void setAccent(Button view, int accent)
    {
        int color = view.getResources().getColor(accent);

        int states[][] = { { android.R.attr.state_pressed, android.R.attr.state_focused, android.R.attr.state_long_pressable } };
        int colors[] = { color };

        view.setBackgroundTintList(new ColorStateList(states, colors));
        view.setForegroundTintList(new ColorStateList(states, colors));
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        view.setTextColor(color);
    }

    public static void setAccent(Switch view, int accent)
    {
        int color = view.getResources().getColor(accent);

        int states[][] = { { android.R.attr.state_checked }, {} };
        int colors[] = { color, color };

        view.setButtonTintList(new ColorStateList(states, colors));
        view.setThumbTintList(new ColorStateList(states, colors));

        CompoundButtonCompat.setButtonTintList(view, new ColorStateList(states, colors));
    }
}
