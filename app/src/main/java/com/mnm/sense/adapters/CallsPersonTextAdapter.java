package com.mnm.sense.adapters;

import android.widget.TextView;

public class CallsPersonTextAdapter extends CallsTextAdapter
{
    public CallsPersonTextAdapter()
    {
        super("person");
    }

    @Override
    public VisualizationAdapter<TextView, String> newInstance()
    {
        return new CallsPersonTextAdapter();
    }
}
