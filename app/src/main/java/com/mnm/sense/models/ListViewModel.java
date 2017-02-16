package com.mnm.sense.models;

import android.graphics.drawable.Drawable;

import com.mnm.sense.trackers.Tracker;

public class ListViewModel extends BaseModel<ListViewData>
{
    public ListViewModel(Tracker tracker, ListViewData data)
    {
        super(tracker, data == null ? new ListViewData() : data);
    }
}
