package com.mnm.sense.initializers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mnm.sense.activities.SecondActivity;
import com.mnm.sense.models.DashboardModel;
import com.mnm.sense.models.MapModel;
import com.mnm.sense.views.DashboardView;


public class DashboardViewInitializer extends ViewInitializer<DashboardView, DashboardModel>
{
    public DashboardViewInitializer()
    {
        super(DashboardView.class, DashboardModel.class);
    }

    @Override
    public void init(final Context context, DashboardView view, final DashboardModel model)
    {
        view.image.setImageResource(model.tracker.resource);

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
                intent.putExtra("tracker", model.tracker.type);
                intent.putExtra("visualization", model.visualization);

                AppCompatActivity activity = (AppCompatActivity) context;
                activity.startActivity(intent);
            }
        });
    }
}
