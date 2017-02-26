package com.mnm.sense.models;

import com.mnm.sense.map.AttributedFeature;
import com.mnm.sense.trackers.Tracker;

import java.util.ArrayList;

public class MapModel extends BaseModel<ArrayList<AttributedFeature>>
{
    public boolean scrollEnabled = true; // Always true except in DashboardViewInitializer
    public String attribute;

    public MapModel(Tracker tracker, ArrayList<AttributedFeature> data, String attr)
    {
        super(tracker, data == null ? new ArrayList<AttributedFeature>() : data);

        attribute = attr;
    }
}
