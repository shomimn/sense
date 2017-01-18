package com.mnm.sense.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class InterceptingLinearLayout extends LinearLayout
{
    public boolean intercept = true;

    public InterceptingLinearLayout(Context context)
    {
        super(context);
    }

    public InterceptingLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public InterceptingLinearLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public InterceptingLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return intercept;
    }
}
