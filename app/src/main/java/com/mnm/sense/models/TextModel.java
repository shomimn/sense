package com.mnm.sense.models;

import com.mnm.sense.trackers.Tracker;

public class TextModel extends BaseModel<String>
{
    public TextModel(Tracker tracker, String data)
    {
        super(tracker, data == null ? "No data yet" : data);
    }
}
