package com.androidstudy.alfoneshub.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.LoginActivity;
import com.androidstudy.alfoneshub.R;
//import com.androidstudy.alfoneshub.models.DaoSession;
import com.androidstudy.alfoneshub.utils.AlfonesCommunication;
import com.androidstudy.alfoneshub.utils.Config;

public class ProfileActivity extends AppCompatActivity {
    TextView textViewProfileName, textViewProfileDob, textViewProfileNationalId, 
            textViewProfilePhone, textViewProfileAddress, textViewProfileEmail;
    
    Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Set up from shared prefences
        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_name = preferences.getString("user_name", "user_name");
        if(user_name.equals("user_name")){
            user_name = "";
        }

        String user_phone_number = preferences.getString("user_phone_number", "user_phone_number");
        if(user_phone_number.equals("user_phone_number")){
            user_phone_number = "";
        }

        String user_email = preferences.getString("user_email", "user_email");
        if(user_email.equals("user_email")){
            user_email = "";
        }

        //set up layout
        textViewProfileName = (TextView)findViewById(R.id.txt_profile_name);
        textViewProfileName.setText(user_name.toUpperCase());

        textViewProfilePhone = (TextView)findViewById(R.id.txt_profile_phone);
        textViewProfilePhone.setText(user_phone_number);

        textViewProfileEmail = (TextView)findViewById(R.id.txt_profile_email);
        textViewProfileEmail.setText(user_email);
        
        buttonLogout = (Button)findViewById(R.id.btn_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Logout!!")
                        .setContentText("Are you sure you want to log out")
                        .setConfirmText("YES")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                SharedPreferences preferences = ProfileActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                //Getting editor
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean(Config.LOGGED_IN_NEW_SHARED_PREF, false);
                                editor.putString(Config.WEATHER_ID, "");
                                editor.putString(Config.WEATHER_TYPE, "");
                                editor.putString(Config.WEATHER_DESCRIPTION, "");
                                editor.putString(Config.WEATHER_TEMPERATURE, "");
                                editor.putString(Config.WEATHER_LOCATION, "");
                                editor.putString(Config.USER_ID, "");
                                editor.putString(Config.USER_NAME, "");
                                editor.putString(Config.USER_PHONE_NUMBER, "");
                                editor.putString(Config.USER_EMAIL, "");
                                editor.putString(Config.USER_ROLE, "");
                                editor.putString(Config.PERMANENT_STATUS, "");


                                editor.commit();

//                                DaoSession daoSession = ((AlfonesCommunication) ProfileActivity.this.getApplicationContext()).getDaoSession();
//                                daoSession.getMerchandiseDao().deleteAll();
//                                daoSession.getProductDao().deleteAll();

                                //Fire Login intent
                                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                                ProfileActivity.this.finish();
                            }
                        })
                        .show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
