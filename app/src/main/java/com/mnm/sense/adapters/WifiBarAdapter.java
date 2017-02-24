package com.mnm.sense.adapters;

import android.net.wifi.WifiManager;
import android.util.Pair;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mnm.sense.Colors;
import com.mnm.sense.Util;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.WifiData;
import com.ubhave.sensormanager.data.pull.WifiScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WifiBarAdapter extends VisualizationAdapter<BarChart, BarData>
{
    public static final int MAX_BARS = 16;

    class IntPair extends Pair<Integer, Integer> implements Comparable<IntPair>
    {
        public IntPair(int f, int s)
        {
            super(f, s);
        }

        @Override
        public int compareTo(IntPair intPair)
        {
            return second < intPair.second ? -1 : second.equals(intPair.second) ? 0 : 1;
        }
    }

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        return adaptOne(data.get(data.size() - 1));
    }

    @Override
    public BarData adaptOne(SensorData data)
    {
        HashMap<String, Integer> levels = new HashMap<>();

        WifiData wifiData = (WifiData) data;
        ArrayList<WifiScanResult> scanResults = wifiData.getWifiScanData();

        for (WifiScanResult scanResult : scanResults)
        {
            String ssid = scanResult.getSsid();
            int level = WifiManager.calculateSignalLevel(scanResult.getLevel(), 100);

            levels.put(ssid, level);
        }

        int i = 0;
        BarData barData = new BarData();

        for (Map.Entry<String, Integer> entry : levels.entrySet())
        {
            ArrayList<BarEntry> entries = new ArrayList<>();

            entries.add(new BarEntry(i, entry.getValue()));
            BarDataSet dataSet = new BarDataSet(entries, entry.getKey());
            dataSet.setColor(Colors.CUSTOM_COLORS[i % Colors.CUSTOM_COLORS.length]);

            barData.addDataSet(dataSet);
            ++i;
        }

        barData.setBarWidth(0.9f);
        barData.setValueTextSize(10f);
        barData.setValueTextSize(10f);

        return barData;
    }

    @Override
    public ArrayList<BarData> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    private BarData first(ArrayList<BarData> data)
    {
        return data.get(0);
    }

    @Override
    public VisualizationAdapter<BarChart, BarData> newInstance()
    {
        return new WifiBarAdapter();
    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        HashMap<String, IntPair> averages = new HashMap<>();
        ArrayList<BarData> result = new ArrayList<>();
//        WifiManager wifiManager = (WifiManager) SenseApp.context().getSystemService(Context.WIFI_SERVICE);

        for (SensorData sensorData : data)
        {
            WifiData wifiData = (WifiData) sensorData;
            ArrayList<WifiScanResult> scanResults = wifiData.getWifiScanData();

            for (WifiScanResult scanResult : scanResults)
            {
                String ssid = scanResult.getSsid();
                int level = WifiManager.calculateSignalLevel(scanResult.getLevel(), 100);

                IntPair pair = averages.get(ssid);

                if (pair == null)
                    averages.put(ssid, new IntPair(level, 1));
                else
                    averages.put(ssid, new IntPair(pair.first + level, pair.second + 1));
            }
        }

        int i = 0;
        BarData barData = new BarData();

        Map<String, IntPair> sortedData = Util.sortByValue(averages);

        for (Map.Entry<String, IntPair> entry : sortedData.entrySet())
        {
//            if (i == MAX_BARS)
//                break;

            ArrayList<BarEntry> entries = new ArrayList<>();
            Pair<Integer, Integer> pair = entry.getValue();
            int average = pair.first / pair.second;

            entries.add(new BarEntry(i, average));
            BarDataSet dataSet = new BarDataSet(entries, entry.getKey());
            dataSet.setColor(Colors.CUSTOM_COLORS[i % averages.size()]);

            barData.addDataSet(dataSet);
            ++i;
        }

        barData.setBarWidth(0.9f);
        barData.setValueTextSize(10f);
        barData.setValueTextSize(10f);

        result.add(barData);

        return result;
    }
}
