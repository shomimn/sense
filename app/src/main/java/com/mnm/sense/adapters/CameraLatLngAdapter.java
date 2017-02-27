package com.mnm.sense.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.KeyEvent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.R;
import com.mnm.sense.Timestamp;
import com.mnm.sense.Util;
import com.mnm.sense.map.AttributedFeature;
import com.mnm.sense.map.SensePoint;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.CameraData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CameraLatLngAdapter extends VisualizationAdapter<GoogleMap, ArrayList<AttributedFeature>>
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
                Matrix matrix = new Matrix();
                matrix.postRotate(correctExifOrientation(cameraData.getImagePath()));

                DisplayMetrics displayMetrics = new DisplayMetrics();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inScaled = true;
                bmOptions.inSampleSize = 8;

                Bitmap thumbnail = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
                Bitmap icon = Bitmap.createScaledBitmap(thumbnail, Util.dp(50), Util.dp(50), true);

                result.add(new AttributedFeature()
                        .origin(R.drawable.ic_camera_alt_black_48dp)
                        .image(thumbnail)
                        .icon(icon)
                        .text("Image taken at")
//                        .latLng(latLng)
                        .geometry(SensePoint.make(latLng))
                        .custom("Date:", Timestamp.from(cameraData.getTimestamp()).date())
                        .custom("Time:", Timestamp.from(cameraData.getTimestamp()).time()));
            }
        }

        ArrayList<ArrayList<AttributedFeature>> finalResult = new ArrayList<>();
        finalResult.add(result);

        return finalResult;
    }

    @Override
    public CameraLatLngAdapter newInstance()
    {
        return new CameraLatLngAdapter();
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }

    private int correctExifOrientation(String filePath)
    {
        int orientation = 0;

        try
        {
            ExifInterface ei = new ExifInterface(filePath);

            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        switch(orientation)
        {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;

            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;

            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return orientation;
        }
    }
}
