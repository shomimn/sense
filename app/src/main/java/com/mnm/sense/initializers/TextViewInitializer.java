package com.mnm.sense.initializers;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.models.TextModel;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;

public class TextViewInitializer extends ViewInitializer<TextView, TextModel>
{
    public TextViewInitializer()
    {
        super(TextView.class, TextModel.class, Visualization.TEXT);
    }

    @Override
    public void init(final Context context, final TextView view, final TextModel model)
    {
        final Tracker tracker = model.tracker;

        view.setText(model.data);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(context.getResources().getColor(R.color.colorAccent));

        if (model.shouldUpdate)
        {
            tracker.updateCallbacks.put(visualization, new Tracker.UpdateCallback()
            {
                @Override
                public void update(ArrayList<SensorData> with)
                {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    SensorData sensorData = with.get(with.size() - 1);
                    final String text = (String) tracker.adapters.get(visualization).adaptOne(sensorData);

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Log.d("SENSE", "Updating text to: " + text);

                            view.setText(text);
                        }
                    });
                }
            });
        }
    }
}
