package com.mnm.sense.trackers;


import android.location.Location;
import android.util.Pair;

import com.mnm.sense.Locator;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.MicrophoneLatLngAdapter;
import com.mnm.sense.adapters.MicrophoneLineAdapter;
import com.mnm.sense.adapters.MicrophoneTextAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.MicrophoneData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class MicrophoneTracker extends Tracker
{
    private static final String ATTRIBUTE_NOISE = "Noise";
    public MicrophoneTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_MICROPHONE);

        text = "Microphone";
        resource = R.drawable.ic_mic_black_48dp;
        isOn = false;

        attributes = new String[]{ATTRIBUTE_NOISE};

        limit = new Limit("Noise threshold", 70, 1, 90);
        limit.configurable = false;

        accent = R.color.redColorAccent;
        theme = R.style.RedTheme;

        build()
                .lineChart(new Visualization(2, 3, false))
                .map(new Visualization(2, 3, false))
                .attribute(ATTRIBUTE_NOISE)
                .adapters(
                        new MicrophoneLineAdapter(),
                        new MicrophoneLatLngAdapter());
    }

    @Override
    public void limitNotification(SensorData data)
    {
        MicrophoneData micData = (MicrophoneData)data;
        int average = micData.getAverageDecibels();

        if(average > limit.value)
        {
            Locator locator = Locator.instance();
            Location location = locator.locateAt(micData.getTimestamp());

            if (location == null)
            {
                synchronized (locator)
                {
                    try
                    {
                        locator.wait();
                        location = locator.lastLocation();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            micData.setLocation(Pair.create(location.getLatitude(), location.getLongitude()));
        }
    }

    @Override
    protected void attachLocation(SensorData data)
    {

    }
}
