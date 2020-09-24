package com.cherish.firebasemessaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private  NotificationManagerCompat notificationManager;
    private static String ID = "APP";
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("token");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title =  remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        sendOnChannel(title, message);


//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
//                .setContentTitle(remoteMessage.getNotification().getTitle())
//                .setContentText(remoteMessage.getNotification().getBody())
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setStyle(new NotificationCompat.BigTextStyle())
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setAutoCancel(true);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendOnChannel(String title, String message) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        notificationManager = NotificationManagerCompat.from(this);
        Intent activityIntent = new Intent(this, HomePageActivity.class);
        activityIntent.putExtra(Alert.extra, title);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, ID)
                .setSmallIcon(R.drawable.man)
                .setContentTitle(title)
//                .setLargeIcon(bitmap)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                /*.setColor(Color.parseColor("#8bf6ff"))*/
                .setColor(getColor(R.color.colorPrimaryDark))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_home_black, "View", contentIntent)
                .build();
        notificationManager.notify(1, notification);
    }

    public void sendToFirebase(String token){
        HashMap<String, String> tokenId = new HashMap<>();
        tokenId.put("token", token);
        if (user != null){
            collectionReference.document(user.getUid()).set(tokenId);
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        sendToFirebase(s);
        super.onNewToken(s);
    }
}