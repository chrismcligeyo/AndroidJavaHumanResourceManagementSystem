package com.androidstudy.alfoneshub.utils.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.androidstudy.alfoneshub.utils.Config;


//Class extending FirebaseInstanceIdService
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "AlfonesComm-FBID:";
    Config settings;

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        Log.d(TAG, "TOKEN: " + refreshedToken+":END");
        saveToken(refreshedToken);
    }

    private void saveToken(String token) {
        //save token in settings?
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.NEW_DEVICE_ID, token);
        editor.apply();
    }
}
