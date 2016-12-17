package com.mnm.sense;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ubhave.datahandler.config.DataStorageConfig;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractAsyncTransferLogger;
import com.ubhave.datahandler.transfer.DataUploadCallback;
import com.ubhave.sensormanager.ESException;

import java.util.HashMap;

public class Repository extends AbstractAsyncTransferLogger implements DataUploadCallback
{
    private static Repository _instance;

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
        return 30000;
    }

    @Override
    protected long getWaitForWiFiMillis()
    {
        return 1000L * 60 * 60 * 4;
    }

    @Override
    protected String getDataPostURL()
    {
        return "http://192.168.0.104/api/test";
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
        return "test-device-id";
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
}
