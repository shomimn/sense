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
    public LocationTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_LOCATION);

        text = "Location";
        resource = R.drawable.ic_my_location_black_48dp;
        isOn = false;

        attributes = new String[] { "LatLng" };

        visualizations.put(Visualization.MAP, new Visualization(2, 3, false));
        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));

        HashMap<String, VisualizationAdapter> latLngAdapters = new HashMap<>();
        latLngAdapters.put(Visualization.MAP, new LocationLatLngAdapter());
        latLngAdapters.put(Visualization.TEXT, new LocationTextAdapter());

        adapters.put("LatLng", latLngAdapters);
    }

    @Override
    public Object getModel(String visualizationType)
    {
        if (visualizationType.equals(Visualization.MAP))
            return new MapModel(this, (ArrayList<LatLng>) super.getModel(visualizationType));
        else
            return new TextModel(this, (String) super.getModel(visualizationType));
    }
}
