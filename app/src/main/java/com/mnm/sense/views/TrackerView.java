package com.mnm.sense.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.mnm.sense.R;


public class TrackerView extends LinearLayout
{
    public TextView text;
    public ImageView image;
    public Switch switch_;

    public TrackerView(Context context)
{
    super(context);
    init();
}

    public TrackerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public TrackerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TrackerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init()
    {
        inflate(getContext(), R.layout.tracker_card, this);

        text = (TextView) findViewById(R.id.text);
        image = (ImageView) findViewById(R.id.image);
        switch_ = (Switch) findViewById(R.id.switch_);
    }
}
