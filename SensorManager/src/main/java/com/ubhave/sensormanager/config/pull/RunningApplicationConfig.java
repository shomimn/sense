package com.ubhave.sensormanager.config.pull;

import com.ubhave.sensormanager.config.SensorConfig;

public class RunningApplicationConfig
{
    private static final long DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS = 10 * 1000L;
    private static final long DEFAULT_SLEEP_INTERVAL = 1 * 60 * 1000;
    private static final int RUNNING_APPLICATION_SAMPLING_CYCLES = 1;

    public static SensorConfig getDefault()
    {
        SensorConfig sensorConfig = new SensorConfig();
        sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, DEFAULT_SLEEP_INTERVAL);
        sensorConfig.setParameter(PullSensorConfig.NUMBER_OF_SENSE_CYCLES, RUNNING_APPLICATION_SAMPLING_CYCLES);
        sensorConfig.setParameter(PullSensorConfig.SENSE_WINDOW_LENGTH_MILLIS, DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS);


        return sensorConfig;
    }
}
