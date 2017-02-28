package com.mnm.sense.views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mnm.sense.R;

public class BorderlessButton extends LinearLayout
{
    public Button buttonInstance;

    public BorderlessButton(Context context)
    {
        super(context);
        init();
    }

    public BorderlessButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public BorderlessButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BorderlessButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init()
    {
        inflate(getContext(), R.layout.borderless_button, this);

        buttonInstance = (Button) findViewById(R.id.underlying_button);
    }
}
