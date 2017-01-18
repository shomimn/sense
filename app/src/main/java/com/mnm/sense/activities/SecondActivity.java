package com.mnm.sense.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.initializers.Initializer;
import com.mnm.sense.models.BaseModel;
import com.mnm.sense.trackers.Tracker;

public class SecondActivity extends AppCompatActivity
{
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        layout = (LinearLayout) findViewById(R.id.content_second);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Class viewClass = (Class) extras.get("view");
        Tracker tracker = SenseApp.instance().tracker(extras.getInt("tracker"));
        String visualization = extras.getString("visualization");

        BaseModel model = (BaseModel) tracker.getModel(visualization);
        model.shouldUpdate = false;

        Initializer.get(viewClass).injectIn(this, layout, model);
    }

}
