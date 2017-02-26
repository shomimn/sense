package com.mnm.sense.trackers;

import android.location.Location;
import android.util.Pair;

import com.mnm.sense.Locator;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.CameraLatLngAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.CameraData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class CameraTracker extends Tracker
{
    private final static String ATTRIBUTE_IMAGE = "Image";

    public CameraTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_CAMERA);

        text = "Camera";
        resource = R.drawable.ic_camera_alt_black_48dp;
        isOn = false;

        attributes = new String[]{ATTRIBUTE_IMAGE};

        build()
                .map(new Visualization(2, 3, false))
                .attribute(ATTRIBUTE_IMAGE)
                .adapters(
                        new CameraLatLngAdapter());
    }

    @Override
    protected void attachLocation(SensorData data)
    {
        CameraData cameraData = (CameraData) data;
        Locator locator = Locator.instance();
        Location location = locator.locateAt(cameraData.getTimestamp());

        if (location == null)
        {
            synchronized (locator)
            {
                try
                {
//                    locator.wait();
                    location = locator.lastLocation();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        cameraData.setLocation(Pair.create(location.getLatitude(), location.getLongitude()));
    }
}
