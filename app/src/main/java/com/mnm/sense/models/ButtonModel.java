package com.mnm.sense.models;

import android.view.View;

public class ButtonModel
{
    public String text;
    public View.OnClickListener listener;

    public ButtonModel(String t, View.OnClickListener l)
    {
        text = t;
        listener = l;
    }
}
