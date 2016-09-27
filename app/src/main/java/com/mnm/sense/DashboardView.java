package com.mnm.sense;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DashboardView extends LinearLayout
{
    ImageView image;
    ImageButton button;

    public DashboardView(Context context)
    {
        super(context);
        init();
    }

    public DashboardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init()
    {
        inflate(getContext(), R.layout.dashboard_card, this);

        image = (ImageView) findViewById(R.id.image);
        button = (ImageButton) findViewById(R.id.button);
    }
}
