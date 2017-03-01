package com.mnm.sense;

import android.location.Location;
import android.util.Log;
import android.util.Pair;

import com.mnm.sense.Locator;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderListData;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ContentLocator
{
    public void attachLocation(AbstractContentReaderListData data)
    {
        Locator locator = Locator.instance();

        for (AbstractContentReaderEntry entry : data.getContentList())
        {
            long timestamp = Long.parseLong(entry.get(ContentReaderConfig.SMS_CONTENT_DATE_KEY));

            Location location = locator.locateAt(timestamp);

            if (location == null)
                continue;

            Log.d("Content attachLocation", location.toString());

            entry.setLocation(Pair.create(location.getLatitude(), location.getLongitude()));
        }
    }
}
