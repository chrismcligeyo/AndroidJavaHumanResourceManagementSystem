package com.androidstudy.alfoneshub.utils.firebase;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.androidstudy.alfoneshub.ProjectManagerMainActivity;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.MainActivity;
import com.androidstudy.alfoneshub.utils.Config;


import org.json.JSONException;
import org.json.JSONObject;



public class FirebaseNotificationService extends FirebaseMessagingService {
    private static final String TAG = "ALFONES-NOTIF:";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if we have data payload....we are not sending notifications
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "NOTIFICATION PAYLOAD: " + remoteMessage.getData()); //data present..
            //Create adverts to be fetch using advert_id
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                getMessageData(this.getApplicationContext(), json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }


        } else {
            Log.d(TAG, "NOTIFICATION PAYLOAD: EMPTY"); //How is that?
        }
    }

    private void getMessageData(Context context, JSONObject json) {
        JSONObject data = null;
        String title = "";
        String message = "";
        try {
            data = json.getJSONObject("data");
            title = data.getString("title");
            message = data.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        Boolean loggedIn = sharedPreferences.getBoolean(Config.LOGGED_IN_NEW_SHARED_PREF, false);
        //If we will get true
        if (loggedIn) {

            String user_role = sharedPreferences.getString("user_role","user_role");
            int role = Integer.parseInt(user_role);
            if(role==3){
                //Campaign manager
                Intent intent = new Intent(this, ProjectManagerMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message", message);
                intent.putExtra("title", title);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.icon))
                        .setContentTitle("TrueBlaq " +title)
                        .setContentText("You have a new message from TrueBlaq.\nClick to see")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri) //set sound..
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());

            }
            if(role==4){
                //Campaign team leader
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message", message);
                intent.putExtra("title", title);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.icon))
                        .setContentTitle("TrueBlaq " +title)
                        .setContentText("You have a new message from TrueBlaq.\nClick to see")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri) //set sound..
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
            }

        }



    }

}