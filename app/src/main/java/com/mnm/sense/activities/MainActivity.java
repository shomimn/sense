package com.mnm.sense.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.utils.Utils;
import com.mnm.sense.DepthPageTransformer;
import com.mnm.sense.GridItem;
import com.mnm.sense.Locator;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.ViewPagerAdapter;
import com.mnm.sense.Visualization;
import com.mnm.sense.map.AttributedFeature;
import com.mnm.sense.models.DashboardModel;
import com.mnm.sense.fragments.DashboardFragment;
import com.mnm.sense.fragments.TrackersFragment;
import com.mnm.sense.initializers.TrackerViewInitializer;
import com.mnm.sense.models.MapModel;
import com.mnm.sense.trackers.LocationTracker;
import com.mnm.sense.trackers.MergedTracker;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.*;
import com.ubhave.sensormanager.sensors.pull.ActivityRecognitionSensor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public static final long BACK_THRESHOLD = 1000;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private long backTimestamp = 0;

    public DashboardFragment dashboardFragment;
    public TrackersFragment trackersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Utils.init(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try
        {
            ActivityRecognitionSensor ars = ActivityRecognitionSensor.getSensor(this);
        }
        catch (ESException e)
        {
            e.printStackTrace();
        }


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

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.BROADCAST_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.PACKAGE_USAGE_STATS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 1);

        checkRunningAppsPermission();

    }

    @TargetApi(22)
    public void checkRunningAppsPermission()
    {
        UsageStatsManager manager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        List<UsageStats> list = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        if (list.isEmpty())
        {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Locator.instance().requestLocationUpdates();
    }

    private void setupViewPager(ViewPager viewPager)
    {
        dashboardFragment = new DashboardFragment();
        trackersFragment = new TrackersFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(dashboardFragment, "Dashboard");
        adapter.addFragment(trackersFragment, "Trackers");
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result)
    {
        super.onActivityResult(requestCode, resultCode, result);

        if (requestCode == TrackerViewInitializer.REQUEST_CODE && resultCode == RESULT_OK)
        {
            Bundle bundle = result.getExtras();

            Tracker tracker = SenseApp.instance().tracker(bundle.getInt("tracker"));
            ArrayList<String> forInsertion = bundle.getStringArrayList("insert");
            ArrayList<String> forRemoval = bundle.getStringArrayList("remove");

            Iterator<GridItem> iterator = dashboardFragment.grid.items.iterator();

            while (iterator.hasNext())
            {
                GridItem item = iterator.next();
                DashboardModel data = (DashboardModel) item.data;

                if (data.tracker.type == tracker.type && forRemoval.contains(data.visualization))
                {
                    String dashboardKey = data.tracker.text + data.visualization + "Dashboard";

                    tracker.updateCallbacks.remove(data.visualization);
                    tracker.updateCallbacks.remove(dashboardKey);
                    tracker.clearCallbacks.remove(data.visualization);
                    tracker.clearCallbacks.remove(dashboardKey);

                    dashboardFragment.grid.removeItem(item);
                    iterator.remove();
                }
            }

           addDashboardViews(tracker, forInsertion);
        }
    }

    public void addDashboardViews(Tracker tracker, Collection<String> visualizations)
    {
        for (String key : visualizations)
        {
            Visualization visualization = tracker.visualizations.get(key);

            dashboardFragment.addDashboardView(visualization.rows, visualization.cols,
                    new DashboardModel(tracker, key, tracker.getModel(key)));
        }

        dashboardFragment.layoutDashboard();
    }

    @Override
    public void onBackPressed()
    {
        long now = System.currentTimeMillis();
        long diff = now - backTimestamp;

        Toast.makeText(this, "Press back again to exit the application and stop sensing.", Toast.LENGTH_SHORT).show();

        backTimestamp = now;

        if (diff <= BACK_THRESHOLD)
            super.onBackPressed();
    }
}
