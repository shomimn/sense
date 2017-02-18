package com.mnm.sense.adapters;

import android.net.wifi.WifiManager;
import android.util.Pair;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mnm.sense.Colors;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.WifiData;
import com.ubhave.sensormanager.data.pull.WifiScanResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WifiBarAdapter extends VisualizationAdapter<BarChart, BarData>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        return first(adaptAll(data));
    }

    @Override
    public BarData adaptOne(SensorData data)
    {
        return null;
    }

    @Override
    public ArrayList<BarData> adaptAll(ArrayList<SensorData> data)
    {
        HashMap<String, Pair<Integer, Integer>> averages = new HashMap<>();
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

                Pair<Integer, Integer> pair = averages.get(ssid);

                if (pair == null)
                    averages.put(ssid, Pair.create(level, 1));
                else
                    averages.put(ssid, Pair.create(pair.first + level, pair.second + 1));
            }
        }

        int i = 0;
        BarData barData = new BarData();

        for (Map.Entry<String, Pair<Integer, Integer>> entry : averages.entrySet())
        {
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
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
