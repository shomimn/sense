package com.mnm.sense;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.ESException;

public class TrackerViewInitializer extends ViewInitializer<TrackerView, Tracker>
{
    public TrackerViewInitializer()
    {
        super(TrackerView.class, Tracker.class);
    }

    @Override
    public void init(Context context, TrackerView view, final Tracker data)
    {
        view.text.setText(data.text);
        view.image.setImageResource(data.resource);
        view.image.setColorFilter(context.getResources().getColor(R.color.colorAccent));
        view.switch_.setChecked(data.isOn);

        view.switch_.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                try
                {
                    if (data.subscribed())
                    {
                        if (isChecked)
                            data.unpause();
                        else
                            data.pause();
                    }
                    else
                        data.start();
                }
                catch (ESException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
