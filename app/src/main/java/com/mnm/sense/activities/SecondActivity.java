package com.mnm.sense.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.initializers.Initializer;
import com.mnm.sense.models.BaseModel;
import com.mnm.sense.trackers.Tracker;

public class SecondActivity extends AppCompatActivity
{
    CoordinatorLayout coordinatorLayout;
    TextView trackerTitle;
    ImageView trackerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.content_second);
        trackerTitle = (TextView) findViewById(R.id.tracker_title);
        trackerImage = (ImageView) findViewById(R.id.tracker_img);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Class viewClass = (Class) extras.get("view");
        Tracker tracker = SenseApp.instance().tracker(extras.getInt("tracker"));
        String visualization = extras.getString("visualization");

        trackerTitle.setText(tracker.text);
        trackerImage.setImageResource(tracker.resource);

        BaseModel model = (BaseModel) tracker.getModel(visualization);
        model.shouldUpdate = false;

        Initializer.get(viewClass).injectIn(this, coordinatorLayout, model);
    }

}
