package com.mnm.sense.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.utils.Utils;
import com.mnm.sense.DepthPageTransformer;
import com.mnm.sense.GridItem;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.ViewPagerAdapter;
import com.mnm.sense.Visualization;
import com.mnm.sense.models.DashboardModel;
import com.mnm.sense.fragments.DashboardFragment;
import com.mnm.sense.fragments.TrackersFragment;
import com.mnm.sense.initializers.TrackerViewInitializer;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.sensors.*;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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

            while(iterator.hasNext())
            {
                GridItem item = iterator.next();
                DashboardModel data = (DashboardModel) item.data;

                if (data.tracker.type == tracker.type && forRemoval.contains(data.visualization))
                {
                    tracker.updateCallbacks.remove(data.visualization);
                    dashboardFragment.grid.removeItem(item);
                    iterator.remove();
                }
            }

            for (String key : forInsertion)
            {
                Visualization visualization = tracker.visualizations.get(key);

                dashboardFragment.addDashboardView(visualization.rows, visualization.cols,
                        new DashboardModel(tracker, key, tracker.getModel(key)));
            }

            dashboardFragment.layoutDashboard();
        }
    }
}
