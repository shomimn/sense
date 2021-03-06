package com.mnm.sense.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnm.sense.R;

public class DashboardView extends LinearLayout
{
    public ImageView image;
    public TextView text;
    public View divider;
    public LinearLayout topLayout;

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
        text = (TextView) findViewById(R.id.text);
        divider = (View) findViewById(R.id.divider);
        topLayout = (LinearLayout) findViewById(R.id.top_layout);
    }
}
