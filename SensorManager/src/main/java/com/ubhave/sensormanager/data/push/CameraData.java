package com.ubhave.sensormanager.data.push;


import android.util.Pair;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class CameraData extends SensorData
{
    private String imagePath;
    private Pair<Double, Double> location;

    public CameraData(long sensorTimestamp, SensorConfig config)
    {
        super(sensorTimestamp, config);
    }

    @Override
    public int getSensorType()
    {
        return SensorUtils.SENSOR_TYPE_CAMERA;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public Pair<Double, Double> getLocation()
    {
        return location;
    }

    public void setLocation(Pair<Double, Double> location)
    {
        this.location = location;
    }
}
