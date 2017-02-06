package com.mnm.sense.initializers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnm.sense.R;
import com.mnm.sense.Util;
import com.mnm.sense.Visualization;
import com.mnm.sense.activities.SecondActivity;
import com.mnm.sense.models.DashboardModel;
import com.mnm.sense.models.MapModel;
import com.mnm.sense.trackers.Tracker;
import com.mnm.sense.views.DashboardView;
import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;


public class DashboardViewInitializer extends ViewInitializer<DashboardView, DashboardModel>
{
    public DashboardViewInitializer()
    {
        super(DashboardView.class, DashboardModel.class);
    }

    @Override
    public void init(final Context context, final DashboardView view, final DashboardModel model)
    {
        final Tracker tracker = model.tracker;
        final AppCompatActivity activity = (AppCompatActivity) context;

        view.image.setImageResource(tracker.resource);

        // TODO: Better way for this
        if (model.data instanceof MapModel)
            ((MapModel) model.data).scrollEnabled = false;

        Initializer.get(model.visualization).injectIn(context, view, model.data);

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, SecondActivity.class);
                intent.putExtra("view", model.data.getClass());
                intent.putExtra("tracker", tracker.type);
                intent.putExtra("visualization", model.visualization);

                AppCompatActivity activity = (AppCompatActivity) context;
                activity.startActivity(intent);
            }
        });

        final String key = tracker.text + model.visualization + "Dashboard";

        if (model.shouldUpdate)
        {
            final TextView textView = addTextView(context, view);

            tracker.updateCallbacks.put(key, new Tracker.UpdateCallback()
            {
                @Override
                public void update(ArrayList<SensorData> with)
                {
                    final String value = (String) tracker.defaultAdapter(Visualization.TEXT).adapt(with);

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            textView.setText(value + " / " + String.valueOf(tracker.limit.value));
                        }
                    });

                }
            });
        }
    }

    public TextView addTextView(Context context, DashboardView view)
    {
        TextView textView = new TextView(context);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        textView.setTextColor(context.getResources().getColor(R.color.colorAccent));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        params.gravity = Gravity.END;
        params.rightMargin = Util.dp(10);

        view.topLayout.addView(textView, params);

        return textView;
    }
}
