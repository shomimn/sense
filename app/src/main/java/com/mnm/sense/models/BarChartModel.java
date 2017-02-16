package com.mnm.sense.models;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.mnm.sense.trackers.Tracker;

public class BarChartModel extends BaseModel<BarData>
{
    public String attribute;

    public BarChartModel(Tracker tracker, BarData data, String attr)
    {
        super(tracker, data);

        attribute = attr;
    }
}

