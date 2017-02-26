package com.mnm.sense.models;

import com.mnm.sense.map.AttributedPosition;
import com.mnm.sense.trackers.Tracker;

import java.util.ArrayList;

public class MapModel extends BaseModel<ArrayList<AttributedPosition>>
{
    public boolean scrollEnabled = true; // Always true except in DashboardViewInitializer
    public String attribute;

    public MapModel(Tracker tracker, ArrayList<AttributedPosition> data, String attr)
    {
        super(tracker, data == null ? new ArrayList<AttributedPosition>() : data);

        attribute = attr;
    }
}
