package com.mnm.sense.views;

import android.content.Context;
import android.util.AttributeSet;

public class LimitView extends UpdateView
{
    public int step = 100;

    public LimitView(Context context)
    {
        super(context);
    }

    public LimitView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LimitView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public LimitView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void displayProgress(int progress)
    {
        int value = progress / step;
        value *= step;

        intervalValue.setText(String.valueOf(value));
        intervalSlider.setProgress(value);
    }
}
