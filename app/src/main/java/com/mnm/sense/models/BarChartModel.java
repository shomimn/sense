package com.mnm.sense.models;

import com.github.mikephil.charting.data.BarData;
import com.mnm.sense.trackers.Tracker;

public class BarChartModel extends BaseModel<BarData>
{
    public BarChartModel(Tracker tracker, BarData data)
    {
        super(tracker, data);
    }
}
