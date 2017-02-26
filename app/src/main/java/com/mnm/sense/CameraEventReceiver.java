package com.mnm.sense;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class CameraEventReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("CAMERA", "PHOTO TAKEN");
        try
        {
            Cursor cursor = context.getContentResolver().query(intent.getData(), null,null, null, null);
            cursor.moveToFirst();
            String image_path = cursor.getString(cursor.getColumnIndex("_data"));
            Log.d("CameraEventReceiver: ", "New Photo is Saved as : -" + image_path);
        }
        catch(Exception e)
        {
            Log.d("CameraEventReceiver: ", "New Photo without image path");
        }
    }
}