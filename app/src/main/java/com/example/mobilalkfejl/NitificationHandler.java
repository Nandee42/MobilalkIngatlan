package com.example.mobilalkfejl;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NitificationHandler {
    private NotificationManager mManager;
    private Context mcontext;
    private static final String CHANNEL_ID = "default";

    public NitificationHandler(Context context) {
        mcontext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Default", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Succesful login");
            this.mManager.createNotificationChannel(channel);
        }
    }
    public void send(String message){
        createChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mcontext, CHANNEL_ID)
                .setSmallIcon(R.drawable.own)
                .setContentTitle("Login")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        this.mManager.notify(0, builder.build());
    }
}
