package com.ubhave.dataformatter.json.pull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Pair;

import com.ubhave.dataformatter.json.PullSensorJSONFormatter;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderListData;

public abstract class AbstractContentReaderFormatter extends PullSensorJSONFormatter
{
	private static final String CONTENT_LIST = "contentList";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	
	public AbstractContentReaderFormatter(final Context context, int type)
	{
		super(context, type);
	}

	@Override
	protected void addSensorSpecificData(JSONObject json, SensorData data) throws JSONException
	{
		AbstractContentReaderListData crData = (AbstractContentReaderListData) data;
		ArrayList<AbstractContentReaderEntry> contentList = crData.getContentList();

		JSONArray jsonArray = new JSONArray();
		for (AbstractContentReaderEntry contentMap : contentList)
		{
			JSONObject jsonMapObject = new JSONObject();
			for (String key : contentMap.getKeys())
			{
				jsonMapObject.put(key, contentMap.get(key));
			}

			Pair<Double, Double> location = contentMap.getLocation();

			if (location != null)
			{
				jsonMapObject.put(LATITUDE, location.first);
				jsonMapObject.put(LONGITUDE, location.second);
			}

			jsonArray.put(jsonMapObject);
		}
		json.put(CONTENT_LIST, jsonArray);
	}

	@Override
	protected void addSensorSpecificConfig(JSONObject json, SensorConfig config)
	{
		// nothing to add
	}
	
	protected abstract AbstractContentReaderListData getData(long senseStartTime, SensorConfig config);
	
	protected abstract AbstractContentReaderEntry getNewEntry();

	@Override
	public SensorData toSensorData(String jsonString)
	{
		JSONObject jsonData = super.parseData(jsonString);
		if (jsonData != null)
		{
			long senseStartTimestamp = super.parseTimeStamp(jsonData);
			SensorConfig sensorConfig = super.getGenericConfig(jsonData);
			
			try
			{
				AbstractContentReaderListData data = getData(senseStartTimestamp, sensorConfig);
				JSONArray jsonArray = (JSONArray) jsonData.get(CONTENT_LIST);
				for (int i = 0; i < jsonArray.length(); i++)
				{
					Pair<Double, Double> location = null;
					HashMap<String, String> contentMap = new HashMap<String, String>();
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					Iterator<String> keyIterator = jsonObject.keys();
					while (keyIterator.hasNext())
					{
						String key = keyIterator.next();

						if (key.equals(LATITUDE) || key.equals(LONGITUDE))
							continue;

						contentMap.put(key, (String) jsonObject.get(key));
					}

					if (jsonObject.has(LATITUDE) && jsonObject.has(LONGITUDE))
						location = Pair.create(
								jsonObject.getDouble(LATITUDE),
								jsonObject.getDouble(LONGITUDE));

					AbstractContentReaderEntry entry = getNewEntry();
					entry.setContentMap(contentMap);

					if (location != null)
						entry.setLocation(location);

					data.addContent(entry);
				}
				return data;
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			
		}
		return null;
	}

}
