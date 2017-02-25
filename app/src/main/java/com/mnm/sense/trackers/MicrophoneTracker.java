package com.mnm.sense.trackers;


import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.MicrophoneTextAdapter;
import com.ubhave.sensormanager.ESException;
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

//        limit = new Limit("Noise threshold", );

        MicrophoneTextAdapter textAdapter = new MicrophoneTextAdapter();

        build()
                .text(new Visualization(2, 3, false))
                .attribute(ATTRIBUTE_NOISE)
                .adapters(textAdapter);

    }
}
