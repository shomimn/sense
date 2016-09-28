package com.mnm.sense;

import android.content.Context;

import com.github.mikephil.charting.data.BarData;
import com.google.android.gms.maps.model.LatLng;

public class DashboardViewInitializer extends ViewInitializer<DashboardView, DashboardData>
{
    public DashboardViewInitializer()
    {
        super(DashboardView.class);
    }

    @Override
    public void init(Context context, DashboardView view, DashboardData data)
    {
        view.image.setImageResource(data.imageResource);

        InitializerRepository.get(data.type).injectIn(context, view, data.data);
    }
}
