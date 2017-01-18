package com.mnm.sense.models;

import com.github.mikephil.charting.data.PieData;
import com.mnm.sense.trackers.Tracker;

public class PieChartModel extends BaseModel<PieData>
{
    public PieChartModel(Tracker tracker, PieData data)
    {
        super(tracker, data);
    }
}
