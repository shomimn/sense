package com.mnm.sense.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mnm.sense.DepthPageTransformer;
import com.mnm.sense.R;
import com.mnm.sense.Repository;
import com.mnm.sense.SenseApp;
import com.mnm.sense.Task;
import com.mnm.sense.TaskManager;
import com.mnm.sense.Timestamp;
import com.mnm.sense.ViewPagerAdapter;
import com.mnm.sense.ZoomOutPageTransformer;
import com.mnm.sense.fragments.VisualizationFragment;
import com.mnm.sense.initializers.Initializer;
import com.mnm.sense.models.BaseModel;
import com.mnm.sense.trackers.Tracker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SecondActivity extends AppCompatActivity
                            implements SlidingUpPanelLayout.PanelSlideListener
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
    Button confirmButton;
    TextView dateRange;

    long prevStart = 0;
    long prevEnd = 0;

    Tracker tracker;
    ArrayList<VisualizationFragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        tracker = SenseApp.instance().tracker(extras.getInt("tracker"));
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

        if (tracker.visualizations.size() == 1)
            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        startDate = (DatePicker) findViewById(R.id.start_date);
        endDate = (DatePicker) findViewById(R.id.end_date);
        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        confirmButton = (Button) findViewById(R.id.confirm_button);
        dateRange = (TextView) findViewById(R.id.date_range);

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

        slidingLayout.addPanelSlideListener(this);

        setupDatePickers();
    }

    private void setupViewPager(final ViewPager viewPager, final Tracker tracker, String defaultVisualization)
    {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        int pageToDisplay = 0;
        int page = 0;

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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                VisualizationFragment fragment = fragments.get(position);

                if (fragment.refreshScheduled)
                    fragment.refresh();
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l)
            {
                String attribute = tracker.attributes[j];
                int currentFragment = viewPager.getCurrentItem();

                for (int i = 0; i < fragments.size(); ++i)
                {
                    VisualizationFragment fragment = fragments.get(i);

                    if (fragment.attribute.equals(attribute))
                        return;

                    fragment.attribute = attribute;
                    fragment.refreshScheduled = true;

                    if (i == currentFragment)
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

    private void updateDatePickerIfNeeded(DatePicker datePicker, Calendar calendar)
    {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (datePicker.getYear() != year || datePicker.getMonth() != month || datePicker.getDayOfMonth() != day)
            datePicker.updateDate(year, month, day);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset)
    {

    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState)
    {
        if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED)
        {
            SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(prevStart);

            updateDatePickerIfNeeded(startDate, calendar);

            String begin = dateFormat.format(calendar.getTime());

            calendar.setTimeInMillis(prevEnd);

            updateDatePickerIfNeeded(endDate, calendar);

            String end = dateFormat.format(calendar.getTime());

            dateRange.setText(String.format("%s - %s", begin, end));

            if (confirmButton.getVisibility() == View.VISIBLE)
                confirmButton.setVisibility(View.INVISIBLE);
        }
    }

    public void setupDatePickers()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        prevStart = calendar.getTimeInMillis();
        prevEnd = prevStart;

//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        final SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        String date = dateFormat.format(calendar.getTime());

        dateRange.setText(String.format("%s - %s", date, date));

        startDate.init(year, month, day, new DatePicker.OnDateChangedListener()
        {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2)
            {
                if (confirmButton.getVisibility() == View.INVISIBLE)
                    confirmButton.setVisibility(View.VISIBLE);
            }
        });

        endDate.init(year, month, day, new DatePicker.OnDateChangedListener()
        {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2)
            {
                if (confirmButton.getVisibility() == View.INVISIBLE)
                    confirmButton.setVisibility(View.VISIBLE);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TaskManager.getMainHandler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }, 250);

                String baseUrl = Repository.baseUrl;
                try
                {
                    baseUrl = Repository.instance().getRemoteFor(tracker.type);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                long begin = 1487026800000L;
                long end = 1487113200000L;

                Timestamp startTs = Timestamp.startOf(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
                String beginStr = startTs.date();
                begin = startTs.millis();

                prevStart = begin;

                Timestamp endTs = Timestamp.endOf(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
                String endStr = endTs.date();
                end = endTs.millis();

                prevEnd = end;

                dateRange.setText(String.format("%s - %s", beginStr, endStr));

                Log.d("Download", "Fetching data from " + beginStr + " to " + endStr);

                String url = String.format("%s/%d/%d", baseUrl, begin, end);

                TaskManager.instance().executeAndPost(new Task.Progress(SecondActivity.this, tracker.type, url, "Downloading data")
                {
                    @Override
                    public void executeImpl(ArrayList<SensorData> data)
                    {
                        Log.d("Progress Task", "Data downloaded and displayed");
                        tracker.remoteData = data;

                        int currentFragment = viewPager.getCurrentItem();

                        for (int i = 0; i < fragments.size(); ++i)
                        {
                            VisualizationFragment fragment = fragments.get(i);

                            fragment.mode = Tracker.MODE_REMOTE;
                            fragment.refreshScheduled = true;

                            if (i == currentFragment)
                                fragment.refresh();
                        }
                    }
                });
            }
        });
    }
}
