package com.mnm.sense;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

public class SenseApp extends Application
{
    private static SenseApp instance_;
    WeakReference<Object> visualizationData;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance_ = this;
    }

    public static SenseApp instance()
    {
        return instance_;
    }

    public static Context context()
    {
        return instance_.getApplicationContext();
    }
}
