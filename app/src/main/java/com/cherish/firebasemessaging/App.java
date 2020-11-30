package com.cherish.firebasemessaging;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    private  static String ID = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           NotificationChannel channel = new NotificationChannel(ID, "Tutorial", NotificationManager.IMPORTANCE_HIGH);
           channel.setDescription("FireBase Messaging");
           NotificationManager manager = getSystemService(NotificationManager.class);
           manager.createNotificationChannel(channel);

       }
    }
}
