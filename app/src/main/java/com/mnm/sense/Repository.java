package com.mnm.sense;

import android.content.Context;
import android.util.Log;

import com.mnm.sense.trackers.Tracker;
import com.ubhave.datahandler.config.DataStorageConfig;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractAsyncTransferLogger;
import com.ubhave.datahandler.transfer.DataUploadCallback;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.HashMap;

import static com.ubhave.sensormanager.sensors.SensorUtils.getSensorName;

public class Repository extends AbstractAsyncTransferLogger implements DataUploadCallback
{
    public static final String baseUrl = "http://192.168.0.104/api";

    private static Repository _instance;

    private int updateInterval = 10 * 60000;
    private String deviceId = null;

    protected Repository(Context context) throws DataHandlerException, ESException
    {
        super(context, DataStorageConfig.STORAGE_TYPE_DB);
    }

    public static Repository instance() throws ESException, DataHandlerException
    {
        if (_instance == null)
            _instance = new Repository(SenseApp.context());

        return _instance;
    }

    @Override
    protected String getPostKey()
    {
        return "POST";
    }

    @Override
    protected long getDataLifeMillis()
    {
        return 1000;
    }

    @Override
    protected long getTransferAlarmLengthMillis()
    {
        return updateInterval;
    }

    @Override
    protected long getWaitForWiFiMillis()
    {
        return 1000L * 60 * 60 * 4;
    }

    @Override
    protected String getDataPostURL()
    {
        return String.format("%s/upload", baseUrl);
    }

    @Override
    protected String getSuccessfulPostResponse()
    {
        return "";
    }

    @Override
    protected HashMap<String, String> getPostParameters()
    {
        return new HashMap<String, String>();
    }

    @Override
    protected String getFileStorageName()
    {
        return "data2.db";
    }

    @Override
    protected String getUniqueUserId()
    {
        return "test-user-id";
    }

    @Override
    protected String getDeviceId()
    {
        if (deviceId == null)
            deviceId = SenseApp.deviceId();

        return deviceId;
    }

    @Override
    protected boolean shouldPrintLogMessages()
    {
        return true;
    }

    @Override
    protected String getEncryptionPassword()
    {
        return null;
    }

    public void flush() throws DataHandlerException
    {
        flush(this);
    }

    @Override
    public void onDataUploaded()
    {
    }

    @Override
    public void onDataUploadFailed()
    {
        Log.d("UPLOAD", "upload failed");
    }

    public void setUpdateInterval(int millis) throws DataHandlerException
    {
        updateInterval = millis;
        configureDataStorage();
    }

    public int getUpdateInterval()
    {
        return updateInterval / 1000;
    }

    public String getRemoteFor(int trackerType)
    {
        String remoteUrl = Repository.baseUrl;

        try
        {
            String sensorName = SensorUtils.getSensorName(trackerType);

            remoteUrl += String.format("/history/%s/%s/%s", getUniqueUserId(), getDeviceId(), sensorName);
        }
        catch (ESException e)
        {
            e.printStackTrace();
        }

        return remoteUrl;
    }
}
