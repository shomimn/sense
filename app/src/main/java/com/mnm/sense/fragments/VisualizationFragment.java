package com.mnm.sense.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mnm.sense.R;
import com.mnm.sense.initializers.Initializer;
import com.mnm.sense.models.BaseModel;
import com.mnm.sense.trackers.Tracker;

public class VisualizationFragment extends Fragment
{
    public Tracker tracker;
    public String visualization;
    public String attribute;
    public int mode;
    public boolean refreshScheduled = false;

    Object injected;

    public VisualizationFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.visualization_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        attribute = tracker.attributes[0];

        BaseModel model = (BaseModel) tracker.getModel(visualization);
        model.shouldUpdate = false;

        injected = Initializer.get(visualization).injectIn(getContext(), view, model);
    }

    public void refresh()
    {
        refreshScheduled = false;

        BaseModel model = (BaseModel) tracker.getModel(mode, attribute, visualization);
        model.shouldUpdate = false;

        Initializer.get(visualization).init(getContext(), injected, model);
    }
}
