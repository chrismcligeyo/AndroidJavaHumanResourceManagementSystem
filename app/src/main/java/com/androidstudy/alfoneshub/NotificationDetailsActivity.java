package com.androidstudy.alfoneshub;

import android.content.Intent;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidstudy.alfoneshub.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationDetailsActivity extends AppCompatActivity {

    String id = "", message="", date="", ba_id="", read="";
    TextView textViewMessage, textViewDate, textViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Notifications");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = this.getIntent();
        if (null != intent) {
            id = intent.getStringExtra("id");
            message = intent.getStringExtra("message");
            date = intent.getStringExtra("date");
            ba_id = intent.getStringExtra("ba_id");
            read = intent.getStringExtra("read");
        }

        textViewDate = (TextView)findViewById(R.id.txt_date);
        textViewDate.setText(date);

        textViewMessage = (TextView)findViewById(R.id.txt_message);
        textViewMessage.setText(message);

        textViewProfile = (TextView)findViewById(R.id.text_view_profile);
        textViewProfile.setText(message = String.valueOf(message.charAt(0)).toUpperCase());
        
        updateReadStatus();
    }

    private void updateReadStatus() {
        Log.d("String", id);
        AndroidNetworking.post(URLs.NOTIFICATION_READ_STATUS_URL)
                .addBodyParameter("id", id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("notification response", response.toString());
                        try {

                            int success = response.getInt("success");
                            if (success==1){
                                Log.d("Notification", "Status changed successful");
                            }else{
                                Log.d("Notification", "Status changed failed");
                            }
                            
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("notification", "Result " + anError);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
