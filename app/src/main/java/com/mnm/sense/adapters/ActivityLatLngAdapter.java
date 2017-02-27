package com.mnm.sense.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.R;
import com.mnm.sense.Timestamp;
import com.mnm.sense.map.AttributedFeature;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.CameraData;

import java.io.File;
import java.util.ArrayList;

public class ActivityLatLngAdapter extends VisualizationAdapter<GoogleMap, ArrayList<AttributedFeature>>
{

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if(data.size() == 0)
            return null;

        ArrayList<ArrayList<AttributedFeature>> result = adaptAll(data);

        if(result.size() == 0)
            return 0;
        return result.get(0);
    }

    @Override
    public ArrayList<AttributedFeature> adaptOne(SensorData data)
    {
        return null;
    }

    @Override
    public ArrayList<ArrayList<AttributedFeature>> adaptAll(ArrayList<SensorData> dataList)
    {
        ArrayList<AttributedFeature> result = new ArrayList<>();

        for(SensorData data : dataList)
        {
            CameraData cameraData = (CameraData) data;

            Pair<Double, Double> location = cameraData.getLocation();

            if(location != null)
            {
                LatLng latLng = new LatLng(location.first, location.second);

                File image = new File(cameraData.getImagePath());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap icon = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
                icon = Bitmap.createScaledBitmap(icon, 50, 50, true);

                result.add(new AttributedFeature()
                        .origin(R.drawable.ic_camera_alt_black_48dp)
                        .icon(icon)
                        .text("Image taken at")
                        .latLng(latLng)
                        .custom("Date:", Timestamp.from(cameraData.getTimestamp()).date())
                        .custom("Time:", Timestamp.from(cameraData.getTimestamp()).time()));
            }
        }

        ArrayList<ArrayList<AttributedFeature>> finalResult = new ArrayList<>();
        finalResult.add(result);

        return finalResult;
    }

    @Override
    public VisualizationAdapter<GoogleMap, ArrayList<AttributedFeature>> newInstance()
    {
        return null;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
