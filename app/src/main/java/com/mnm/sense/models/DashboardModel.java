package com.mnm.sense.models;

import com.mnm.sense.trackers.Tracker;

public class DashboardModel extends BaseModel<Object>
{
    public String visualization;

    public DashboardModel(Tracker tracker, String visualization, Object data)
    {
        super(tracker, data);

        this.visualization = visualization;
    }
}
