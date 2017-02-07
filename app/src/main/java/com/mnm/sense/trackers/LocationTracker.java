package com.mnm.sense.trackers;

import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.MapModel;
import com.mnm.sense.adapters.LocationLatLngAdapter;
import com.mnm.sense.adapters.LocationTextAdapter;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.models.TextModel;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationTracker extends Tracker
{
    public static final String ATTRIBUTE_MARKER = "Marker";
    public static final String ATTRIBUTE_PATH = "Path";

    public LocationTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_LOCATION);

        text = "Location";
        resource = R.drawable.ic_my_location_black_48dp;
        isOn = false;

        attributes = new String[] { ATTRIBUTE_MARKER, ATTRIBUTE_PATH };

        visualizations.put(Visualization.MAP, new Visualization(2, 3, false));
        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));

        HashMap<String, VisualizationAdapter> markerAdapters = new HashMap<>();
        markerAdapters.put(Visualization.MAP, new LocationLatLngAdapter());
        markerAdapters.put(Visualization.TEXT, new LocationTextAdapter());

        HashMap<String, VisualizationAdapter> pathAdapters = new HashMap<>();
        pathAdapters.put(Visualization.MAP, new LocationLatLngAdapter());
        pathAdapters.put(Visualization.TEXT, new LocationTextAdapter());

        adapters.put(ATTRIBUTE_MARKER, markerAdapters);
        adapters.put(ATTRIBUTE_PATH, pathAdapters);
    }
}
