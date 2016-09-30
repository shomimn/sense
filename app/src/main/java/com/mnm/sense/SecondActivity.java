package com.mnm.sense;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

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
        Class viewClass = (Class) intent.getExtras().get("view");

        Initializer.get(viewClass).injectIn(this, layout, SenseApp.instance().visualizationData.get());
    }

}
