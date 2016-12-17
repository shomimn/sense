package com.mnm.sense;

import android.graphics.Color;
import android.hardware.Sensor;
import android.os.BatteryManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.push.BatteryFormatter;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.StepCounterData;
import com.ubhave.sensormanager.data.push.BatteryData;
import com.ubhave.sensormanager.sensors.*;

public class MainActivity extends AppCompatActivity
{

    public static final String PIPELINE_NAME = "default";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.getRootView().setBackgroundColor(Color.parseColor("#EEEEEE"));
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (SensorEnum s : SensorEnum.values())
        {
            Log.d("toString", s.toString());
            Log.d("SENSORENUM", s.getName());
        }

        class Tracker implements SensorDataListener
        {
            public int type;
            public int id;

            protected ESSensorManager sensorManager;

            public Tracker(int t) throws ESException
            {
                type = t;

                sensorManager = ESSensorManager.getSensorManager(SenseApp.context());
                id = sensorManager.subscribeToSensorData(type, this);
            }

            @Override
            public void onDataSensed(SensorData data)
            {
                try
                {
                    Repository.instance().logSensorData(data);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
            {

            }

            public void pause() throws ESException
            {
                sensorManager.pauseSubscription(id);
            }

            public void unpause() throws ESException
            {
                sensorManager.unPauseSubscription(id);
            }
        }

        try
        {
            Tracker batteryTracker = new Tracker(SensorUtils.SENSOR_TYPE_BATTERY);
            Tracker callTracker = new Tracker(SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DashboardFragment(), "Dashboard");
        adapter.addFragment(new TrackersFragment(), "Trackers");
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
    }
}
