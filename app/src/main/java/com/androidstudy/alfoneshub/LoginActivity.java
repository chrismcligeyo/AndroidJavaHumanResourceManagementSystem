package com.androidstudy.alfoneshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.ServerCalls;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    TextView btnSignUp, btnForgotCredentials;
    EditText editTextEmail, editTextPassword;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set up layout widgets
        btnForgotCredentials = (TextView)findViewById(R.id.btn_forgot_credentials);
        btnLogin = (Button)findViewById(R.id.btn_login);

        editTextEmail = (EditText) findViewById(R.id.edit_text_email);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        btnForgotCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotCredentialsActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    FirebaseInstanceId.getInstance().getToken();
                    Log.d("Token",String.valueOf(FirebaseInstanceId.getInstance().getToken()));
                }catch (Exception e){
                    Log.d("Error","Token not generated");
                }

                String input_email = editTextEmail.getText().toString();
                // Check if user provided expense
                if (TextUtils.isEmpty(input_email)) {
                    editTextEmail.setError("Enter Email");
                    return;
                }

                String input_password = editTextPassword.getText().toString();
                // Check if user provided expense
                if (TextUtils.isEmpty(input_email)) {
                    editTextPassword.setError("Enter Password");
                    return;
                }

                ServerCalls serverCalls = new ServerCalls(LoginActivity.this);
                serverCalls.loginProcess(input_email, input_password);


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        Boolean loggedIn = sharedPreferences.getBoolean(Config.LOGGED_IN_NEW_SHARED_PREF, false);
        //If we will get true
        if (loggedIn) {

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }
    }
}
