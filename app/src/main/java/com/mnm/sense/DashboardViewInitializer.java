package com.mnm.sense;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import java.lang.ref.WeakReference;


class DashboardData
{
    public static final int NULL = 0;
    public static final int BAR_CHART = 1;
    public static final int TEXT = 2;
    public static final int MAP = 3;

    int type;
    Object data;
    int imageResource;

    DashboardData(int t, int res, Object d)
    {
        type = t;
        imageResource = res;
        data = d;
    }
}

public class DashboardViewInitializer extends ViewInitializer<DashboardView, DashboardData>
{
    public DashboardViewInitializer()
    {
        super(DashboardView.class, DashboardData.class);
    }

    @Override
    public void init(final Context context, DashboardView view, final DashboardData data)
    {
        view.image.setImageResource(data.imageResource);

        Initializer.get(data.type).injectIn(context, view, data.data);

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, SecondActivity.class);
                intent.putExtra("view", data.data.getClass());
                SenseApp.instance().visualizationData = new WeakReference<>(data.data);

                AppCompatActivity activity = (AppCompatActivity) context;
                activity.startActivity(intent);
            }
        });
    }
}
