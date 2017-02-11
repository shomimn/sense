package com.mnm.sense;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

public class NotificationCreator
{
    public static void create(int icon, String title, String message)
    {
        Context context = SenseApp.context();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(icon)
                .setTicker("Sense")
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public static void create(String title, String message)
    {
        create(R.mipmap.ic_launcher, title, message);
    }
}
