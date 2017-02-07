package com.mnm.sense.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.Util;
import com.mnm.sense.Visualization;
import com.mnm.sense.trackers.Tracker;
import com.mnm.sense.views.BottomBorderedLinearLayout;
import com.mnm.sense.views.LimitView;
import com.mnm.sense.views.UpdateView;

import java.util.ArrayList;
import java.util.Map;

public class SensorSettingsActivity extends AppCompatActivity
{
    TextView trackerTitle;
    ImageView trackerImage;
    LinearLayout visualizationLayout;
    UpdateView updateView;
    CardView limitCard;
    LimitView limitView;

    Tracker tracker;
    ArrayList<String> forRemoval = new ArrayList<>();
    ArrayList<String> forInsertion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        int trackerType = getIntent().getExtras().getInt("tracker");
        tracker = SenseApp.instance().tracker(trackerType);

        setTheme(tracker.theme);

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sensor_settings);

        trackerTitle = (TextView) findViewById(R.id.tracker_title);
        trackerImage = (ImageView) findViewById(R.id.tracker_img);
        visualizationLayout = (LinearLayout) findViewById(R.id.visualization_layout);
        updateView = (UpdateView) findViewById(R.id.update_view);
        limitCard = (CardView) findViewById(R.id.limit_card);
        limitView = (LimitView) findViewById(R.id.limit_view);

        trackerTitle.getRootView().setBackgroundColor(Color.parseColor("#EEEEEE"));

        trackerTitle.setText(tracker.text + " Tracker Settings");
        trackerImage.setImageResource(tracker.resource);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        addVisualizations();
        setupLimits();
        setupUpdateView();
    }

    public void addVisualizations()
    {
        int colorAccent = getResources().getColor(tracker.accent);

        for (Map.Entry<String, Visualization> entry : tracker.visualizations.entrySet())
        {
            final String key = entry.getKey();
            final Visualization visualization = entry.getValue();
            final boolean isDisplayed = visualization.isDisplayed;

            LinearLayout layout = new BottomBorderedLinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

            TextView textView = new TextView(this);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(key);
            textView.setTextColor(colorAccent);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setGravity(Gravity.CENTER_VERTICAL);

//            textView.setTextColor(getResources().getColor(tracker.accent));

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText("Display on dashboard");
            checkBox.setTextColor(colorAccent);
            checkBox.setGravity(Gravity.CENTER_VERTICAL);
            checkBox.setChecked(isDisplayed);

//            checkBox.setTextColor(getResources().getColor(tracker.accent));

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    if (b)
                    {
                        if (!isDisplayed)
                            forInsertion.add(key);
                        forRemoval.remove(key);
                    }
                    else
                    {
                        forInsertion.remove(key);
                        if (isDisplayed)
                        forRemoval.add(key);
                    }

                    visualization.isDisplayed = b;
                }
            });

            layout.addView(textView, params);
            layout.addView(checkBox, params);

            visualizationLayout.addView(layout, 2);
        }
    }

    public void setupUpdateView()
    {
        updateView.viewTitle.setText("Sense Interval");

        int interval = (int) tracker.getSenseInterval();

        if (interval == 0)
        {
            updateView.intervalSlider.setEnabled(false);
            updateView.intervalValue.setText("Sensed on change");
            return;
        }

        updateView.intervalSlider.setMax(6 * 60 * 60);
        updateView.intervalSlider.setProgress(interval);

        updateView.cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                updateView.animateSlider((int) tracker.getSenseInterval());
                updateView.hideButtons();
            }
        });

        updateView.confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tracker.setSenseInterval(updateView.intervalSlider.getProgress() * 1000);
                tracker.restart();
                updateView.hideButtons();
            }
        });
    }

    public void setupLimits()
    {
        if (!tracker.hasLimit())
        {
            limitCard.setVisibility(View.GONE);
            return;
        }

        limitView.step = tracker.limit.step;

        limitView.viewTitle.setText(tracker.limit.title);

        limitView.intervalSlider.setMax(tracker.limit.maxValue);
        limitView.intervalSlider.setProgress(tracker.limit.value);

        limitView.cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                limitView.animateSlider(tracker.limit.value);
                limitView.hideButtons();
            }
        });

        limitView.confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tracker.limit.value = limitView.intervalSlider.getProgress();
                limitView.hideButtons();
            }
        });
    }

    public void setAccent()
    {
        Resources res = getResources();
        int color = res.getColor(tracker.accent);

        trackerImage.setColorFilter(color);
        trackerTitle.setTextColor(color);

        updateView.viewTitle.setTextColor(color);
        updateView.cancel.setTextColor(color);
        updateView.confirm.setTextColor(color);

        limitView.viewTitle.setTextColor(color);
//        limitView.cancel.setTextColor(color);
//        limitView.confirm.setTextColor(color);
        Util.setAccent(limitView.cancel, tracker.accent);
        Util.setAccent(limitView.confirm, tracker.accent);
    }

    @Override
    public void finish()
    {
        Intent result = new Intent();
        result.putExtra("tracker", tracker.type);
        result.putExtra("insert", forInsertion);
        result.putExtra("remove", forRemoval);

        setResult(RESULT_OK, result);
        super.finish();
    }
}
