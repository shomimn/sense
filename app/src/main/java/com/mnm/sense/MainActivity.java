package com.mnm.sense;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

import com.mnm.sense.trackers.BatteryTracker;
import com.mnm.sense.trackers.BluetoothTracker;
import com.mnm.sense.trackers.CallLogTracker;
import com.mnm.sense.trackers.LightTracker;
import com.mnm.sense.trackers.PassiveLocationTracker;
import com.mnm.sense.trackers.ProximityTracker;
import com.mnm.sense.trackers.SMSContentTracker;
import com.mnm.sense.trackers.ScreenTracker;
import com.mnm.sense.trackers.StepsTracker;
import com.mnm.sense.trackers.Tracker;
import com.mnm.sense.trackers.WifiTracker;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.*;

public class MainActivity extends AppCompatActivity
                          implements PermissionHandler
{

    public static final String PIPELINE_NAME = "default";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static HashMap<Integer, Tracker> trackers = new HashMap<>();

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

        try
        {
            trackers.put(SensorUtils.SENSOR_TYPE_STEP_COUNTER, new StepsTracker(this));
            trackers.put(SensorUtils.SENSOR_TYPE_WIFI, new WifiTracker(this));
            trackers.put(SensorUtils.SENSOR_TYPE_BATTERY, new BatteryTracker(this));
            trackers.put(SensorUtils.SENSOR_TYPE_PASSIVE_LOCATION, new PassiveLocationTracker(this));
            trackers.put(SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER, new SMSContentTracker(this));
            trackers.put(SensorUtils.SENSOR_TYPE_BLUETOOTH, new BluetoothTracker(this));
            trackers.put(SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER, new CallLogTracker(this));
            trackers.put(SensorUtils.SENSOR_TYPE_LIGHT, new LightTracker(this));
            trackers.put(SensorUtils.SENSOR_TYPE_PROXIMITY, new ProximityTracker(this));
            trackers.put(SensorUtils.SENSOR_TYPE_SCREEN, new ScreenTracker(this));
        }
        catch (ESException e)
        {
            e.printStackTrace();
        }

//        addTracker(SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER);
//        addTracker(SensorUtils.SENSOR_TYPE_BATTERY);
//        addTracker(SensorUtils.SENSOR_TYPE_PASSIVE_LOCATION);
//        addTracker(SensorUtils.SENSOR_TYPE_SCREEN);
//        addTracker(SensorUtils.SENSOR_TYPE_ACCELEROMETER);
//        addTracker(SensorUtils.SENSOR_TYPE_BLUETOOTH);
//        addTracker(SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER);
//        addTracker(SensorUtils.SENSOR_TYPE_SMS);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.BROADCAST_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_PHONE_STATE
                }, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        Tracker tracker = trackers.get(requestCode);
//
//        if (tracker != null)
//        {
//            boolean result = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
//
//            try
//            {
//                tracker.onPermissionResponse(result);
//            }
//            catch (ESException e)
//            {
//                e.printStackTrace();
//            }
//        }
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

    private void addTracker(Tracker type)
    {
//        try
//        {
//            trackers.put(type, new Tracker(, type));
//        }
//        catch (ESException e)
//        {
//            e.printStackTrace();
//        }
    }

    @Override
    public void request(int requestCode, String[] permissions)
    {
        ActivityCompat.requestPermissions(MainActivity.this, permissions, requestCode);
    }
}
