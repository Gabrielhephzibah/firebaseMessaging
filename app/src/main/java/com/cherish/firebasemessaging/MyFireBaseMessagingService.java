package com.cherish.firebasemessaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

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
import java.util.Random;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private  NotificationManagerCompat notificationManager;
    private static String ID = "App";
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("token");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getData().get("title");
        String subtitle = remoteMessage.getData().get("message");
        Log.i(" TITLE",title);
        Log.i("Subtitle", subtitle);
        sendOnChannel(title, subtitle);


//        if (remoteMessage.getData().size() > 0) {
//            Log.d("PayLoad", "Message data payload: " + remoteMessage.getData());
//            sendOnChannel(remoteMessage.getData().get("message"));
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d("NOTIFICAYION", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            sendOnChannel(remoteMessage.getNotification().getBody());
//        }
//        Log.i("Message Recieved", "Message Recieved");
//        String title =  remoteMessage.getData().get("title");
//        String message = remoteMessage.getData().get("message");
//        Log.i("Title", title);
//        Log.i("MESSAGE", message);
//        sendOnChannel( message);


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

//    @Override
//    public void onMessageSent(@NonNull String s) {
//        super.onMessageSent(s);
//        Log.i("Message", "Message sent");
//    }

    public void sendOnChannel(String title, String message) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        notificationManager = NotificationManagerCompat.from(this);
        Intent activityIntent = new Intent(this, GetAllBooksActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        activityIntent.putExtra("collection", message);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent.FLAG_ONE_SHOT
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this, ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setLargeIcon(bitmap)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                /*.setColor(Color.parseColor("#8bf6ff"))*/
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setContentIntent(contentIntent)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_search_black, "View", contentIntent)
                .build();
        notificationManager.notify(new Random().nextInt(), notification);
    }


//    private void sendOnChannel(String message) {
//        Log.i("SEND", "Send On Channels");
//        Intent intent = new Intent(this, GetAllBooksActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_home_black)
//                .setContentTitle("FCM Message")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }



//    public void sendOnChannel(String title, String message) {
//        Log.i("SEND on Channle", "send on Chanmnel");
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
//        notificationManager = NotificationManagerCompat.from(this);
//        Intent activityIntent = new Intent(this, GetAllBooksActivity.class);
//        activityIntent.putExtra(Alert.extra, title);
//        PendingIntent contentIntent = PendingIntent.getActivity(this,
//                0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new NotificationCompat.Builder(this, ID)
//                .setSmallIcon(R.drawable.man)
//                .setContentTitle(title)
////                .setLargeIcon(bitmap)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                /*.setColor(Color.parseColor("#8bf6ff"))*/
////                .setColor(getColor(R.color.colorPrimaryDark))
//                .setContentIntent(contentIntent)
//                .setAutoCancel(true)
//                .setOnlyAlertOnce(true)
//                .addAction(R.drawable.ic_home_black, "View", contentIntent)
//                .build();
//        notificationManager.notify(1, notification);
//    }

    public void sendToFirebase(String token){
        Log.i("SendToFireBase", "ToFirebase");
        HashMap<String, String> tokenId = new HashMap<>();
        tokenId.put("token", token);
        if (user != null){
            collectionReference.document(user.getUid()).set(tokenId);

            Log.i("Token", collectionReference.document(user.getUid()).set(tokenId).toString());
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        sendToFirebase(s);
        Log.i("OnNewToken",s );
        super.onNewToken(s);
    }
}
