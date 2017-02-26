/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.sensormanager.data.pull;

import android.util.Pair;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;

public class MicrophoneData extends SensorData
{
	public static final int LARGEST_AMPLITUDE_VALUE = 32767;

	private int[] maxAmplitudeArray;
	private long[] timestampArray;
	private String mediaFilePath;
	private Pair<Double, Double> location = null;
	private int averageDecibels = 0;

	public MicrophoneData(long senseStartTimestamp, SensorConfig sensorConfig)
	{
		super(senseStartTimestamp, sensorConfig);
	}

	public void setMaxAmplitudeArray(int[] maxAmplitudeArray)
	{
		this.maxAmplitudeArray = maxAmplitudeArray;

		averageDecibels = 0;

		ArrayList<Integer> decibelsArray = getDecibelsArray();

		for(int decibel : decibelsArray)
			averageDecibels += decibel;

		averageDecibels /= decibelsArray.size();


	}

	public int[] getAmplitudeArray()
	{
		return maxAmplitudeArray;
	}

	public void setTimestampArray(long[] timestampArray)
	{
		this.timestampArray = timestampArray;
	}

	public long[] getTimestampArray()
	{
		return timestampArray;
	}

	public void setMediaFilePath(String mediaFilePath)
	{
		this.mediaFilePath = mediaFilePath;
	}

	public String getMediaFilePath()
	{
		return mediaFilePath;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_MICROPHONE;
	}

	public ArrayList<Integer> getDecibelsArray()
	{
		ArrayList<Integer> decibels = new ArrayList<>();

		decibels.add(0);

		for(int i = 1; i < maxAmplitudeArray.length; i++)
			if(maxAmplitudeArray[i] != 0)
				decibels.add(toDb(maxAmplitudeArray[i]));

		return decibels;
	}

	private int toDb(int amplitude)
	{
		return 90 + (int)(20 * Math.log10((float)amplitude / (float)LARGEST_AMPLITUDE_VALUE));
	}

	public Pair<Double, Double> getLocation()
	{
		return location;
	}

	public void setLocation(Pair<Double, Double> location)
	{
		this.location = location;
	}

	public int getAverageDecibels()
	{
		return averageDecibels;
	}

	public void setAverageDecibels(int averageDecibels)
	{
		this.averageDecibels = averageDecibels;
	}
}
