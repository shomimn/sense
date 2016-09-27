package com.mnm.sense;

public class TrackerViewInitializer extends ViewInitializer<TrackerView, TrackerData>
{
    public TrackerViewInitializer()
    {
        super(TrackerView.class);
    }

    @Override
    public void init(TrackerView view, TrackerData data)
    {
        view.text.setText(data.text);
        view.image.setImageResource(data.resource);
        view.image.setColorFilter(SenseApp.context().getResources().getColor(R.color.colorAccent));
        view.switch_.setChecked(data.on);
    }
}
