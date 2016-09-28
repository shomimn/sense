package com.mnm.sense;

import android.content.Context;

public class TrackerViewInitializer extends ViewInitializer<TrackerView, TrackerData>
{
    public TrackerViewInitializer()
    {
        super(TrackerView.class);
    }

    @Override
    public void init(Context context, TrackerView view, TrackerData data)
    {
        view.text.setText(data.text);
        view.image.setImageResource(data.resource);
        view.image.setColorFilter(context.getResources().getColor(R.color.colorAccent));
        view.switch_.setChecked(data.on);
    }
}
