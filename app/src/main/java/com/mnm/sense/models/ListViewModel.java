package com.mnm.sense.models;

import com.mnm.sense.trackers.Tracker;

public class ListViewModel extends BaseModel<ListViewData>
{
    public ListViewModel(Tracker tracker, ListViewData data)
    {
        super(tracker, data);
    }
}
