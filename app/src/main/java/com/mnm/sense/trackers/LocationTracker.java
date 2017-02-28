package com.mnm.sense.trackers;

import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.adapters.CallsLatLngAdapter;
import com.mnm.sense.adapters.CameraLatLngAdapter;
import com.mnm.sense.adapters.RunningAppsLatLngAdapter;
import com.mnm.sense.adapters.SMSLatLngAdapter;
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
    public static final String ATTRIBUTE_HEATMAP = "Heat map";

    public LocationTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_LOCATION);

        text = "Location";
        resource = R.drawable.ic_my_location_black_48dp;
        isOn = false;

        attributes = new String[] { ATTRIBUTE_MARKER, ATTRIBUTE_PATH, ATTRIBUTE_HEATMAP };

        build()
            .map(new Visualization(2, 3, false))
            .attribute(ATTRIBUTE_MARKER)
            .adapters(new LocationLatLngAdapter())
            .attribute(ATTRIBUTE_PATH)
            .adapters(new LocationLatLngAdapter())
            .attribute(ATTRIBUTE_HEATMAP)
            .adapters(new LocationLatLngAdapter());
    }
}
