package com.mnm.sense.adapters;

import android.widget.TextView;

public class SMSPersonTextAdapter extends SMSTextAdapter
{
    public SMSPersonTextAdapter()
    {
        super("person");
    }

    @Override
    public VisualizationAdapter<TextView, String> newInstance()
    {
        return new SMSPersonTextAdapter();
    }
}
