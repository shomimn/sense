package com.mnm.sense.models;

import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.trackers.Tracker;

import java.util.ArrayList;

public class MapModel extends BaseModel<ArrayList<LatLng>>
{
    public boolean scrollEnabled = true; // Always true except in DashboardViewInitializer

    public MapModel(Tracker tracker, ArrayList<LatLng> data)
    {
        super(tracker, data == null ? new ArrayList<LatLng>() : data);
    }
}
