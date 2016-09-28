package com.mnm.sense;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.JsonElement;

import edu.mit.media.funf.json.IJsonObject;
import edu.mit.media.funf.probe.Probe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.support.v7.widget.GridLayout;

public class MainActivity extends AppCompatActivity implements Probe.DataListener
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

    @Override
    public void onDataReceived(IJsonObject probeConfig, IJsonObject data)
    {
    }

    @Override
    public void onDataCompleted(IJsonObject probeConfig, JsonElement checkpoint)
    {
        updateScanCount();
        // Re-register to keep listening after probe completes.
//        wifiProbe.registerPassiveListener(this);
//        locationProbe.registerPassiveListener(this);
    }

    private void updateScanCount()
    {
        // Query the pipeline db for the count of rows in the data table
//        SQLiteDatabase db = pipeline.getDb();
//        Cursor cursor = db.rawQuery("select * from " + NameValueDatabaseHelper.DATA_TABLE.name, null);
//        while (cursor.moveToNext())
//        {
//            String id = cursor.getString(0);
//            String device = cursor.getString(1);
//            long timestamp = cursor.getLong(2);
//            String value = cursor.getString(3);
//
//            int x = 5;
//        }
    }
}
