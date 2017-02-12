package com.mnm.sense.models;

import com.github.mikephil.charting.data.LineData;
import com.mnm.sense.trackers.Tracker;

public class LineChartModel extends BaseModel<LineData>
{
    public LineChartModel(Tracker tracker, LineData data)
    {
        super(tracker, data);
    }
}
