package com.mnm.sense.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mnm.sense.DepthPageTransformer;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.ViewPagerAdapter;
import com.mnm.sense.ZoomOutPageTransformer;
import com.mnm.sense.fragments.VisualizationFragment;
import com.mnm.sense.initializers.Initializer;
import com.mnm.sense.models.BaseModel;
import com.mnm.sense.trackers.Tracker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity
{
    CoordinatorLayout coordinatorLayout;
    TextView trackerTitle;
    ImageView trackerImage;
    ViewPager viewPager;
    TabLayout tabLayout;
    SlidingUpPanelLayout slidingLayout;
    DatePicker startDate;
    DatePicker endDate;
    Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Class viewClass = (Class) extras.get("view");
        Tracker tracker = SenseApp.instance().tracker(extras.getInt("tracker"));
        String visualization = extras.getString("visualization");

        setTheme(tracker.theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.content_second);
        trackerTitle = (TextView) findViewById(R.id.tracker_title);
        trackerImage = (ImageView) findViewById(R.id.tracker_img);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.getRootView().setBackgroundColor(Color.parseColor("#EEEEEE"));

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        startDate = (DatePicker) findViewById(R.id.start_date);
        endDate = (DatePicker) findViewById(R.id.end_date);
        typeSpinner = (Spinner) findViewById(R.id.type_spinner);

        long installedDate = SenseApp.instance().installedDate();
        startDate.setMinDate(installedDate);
        startDate.setMaxDate(System.currentTimeMillis());
        endDate.setMinDate(installedDate);
        endDate.setMaxDate(System.currentTimeMillis());

        trackerTitle.setText(tracker.text);
        trackerImage.setImageResource(tracker.resource);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, tracker.attributes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(adapter);

        setupViewPager(viewPager, tracker, visualization);
    }

    private void setupViewPager(ViewPager viewPager, final Tracker tracker, String defaultVisualization)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        int pageToDisplay = 0;
        int page = 0;
        final ArrayList<VisualizationFragment> fragments = new ArrayList<>();

        for (String visualization : tracker.visualizations.keySet())
        {
            VisualizationFragment fragment = new VisualizationFragment();
            fragment.tracker = tracker;
            fragment.visualization = visualization;

            adapter.addFragment(fragment, visualization);
            fragments.add(fragment);

            if (visualization.equals(defaultVisualization))
                pageToDisplay = page;

            ++page;
        }

        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        viewPager.setCurrentItem(pageToDisplay);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String attribute = tracker.attributes[i];

                for (VisualizationFragment fragment : fragments)
                {
                    if (fragment.attribute.equals(attribute))
                        return;

                    fragment.attribute = attribute;
                    fragment.refresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else
            super.onBackPressed();
    }
}
