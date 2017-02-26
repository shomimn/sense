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
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(intent.getData(), projection, null, null, null);
        cursor.moveToFirst();

        String imagePath = cursor.getString(cursor.getColumnIndex("_data"));

        cursor.close();
    }
}